package de.upb.wdqa.wdvd.processors.decorators;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.DatamodelConverter;
import org.wikidata.wdtk.datamodel.implementation.DataObjectFactoryImpl;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonItemDocument;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.JsonNormalizer;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.OldJacksonItemDocument;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.OldJacksonRedirectDocument;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.wdtk.MapDeserializerModifier;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

// executed in parallel by several threads
public class JsonProcessor implements RevisionProcessor {
	final Logger logger;
	
	static ObjectMapper newFormatMapper = new ObjectMapper();
	static{
		// Initialize ObjectMapper only once to improve performance
		// see http://wiki.fasterxml.com/JacksonBestPracticesPerformance
		
		// Workaround to cope with empty objects {} that are wrongly serialized as [],
		// e.g. for labels, descriptions, claims and sitelinks.
		SimpleModule module = new SimpleModule();
		module.setDeserializerModifier(new MapDeserializerModifier());
		newFormatMapper.registerModule(module);		
	}
	
	// ObjectMapper is thread safe after it has been configured
	static ObjectMapper oldFormatMapper = new ObjectMapper();	
	static ObjectMapper redirectMapper = new ObjectMapper();
	
	private RevisionProcessor processor;
	
	// parsing problems
	private SummaryStatistics emptyJsonStatistics = new SummaryStatistics();
	private SummaryStatistics inconsistentJsonXMLStatistics = new SummaryStatistics();
	private SummaryStatistics jsonExceptionStatistics = new SummaryStatistics();
	private SummaryStatistics nullPointerExceptionStatistics = new SummaryStatistics();
	
	// parsing successfull
	private SummaryStatistics newJsonStatistics = new SummaryStatistics();
	private SummaryStatistics oldJsonStatistics = new SummaryStatistics();
	private SummaryStatistics redirectStatistics = new SummaryStatistics();
	
	public JsonProcessor(RevisionProcessor processor, int number) {
		this.processor = processor;
		this.logger = LoggerFactory.getLogger("" + JsonProcessor.class + number);
	}

	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		
		if(processor!=null){
			processor.startRevisionProcessing();
		}
	}

	@Override
	public void processRevision(Revision revision) {
		
		
		// Issue in the database dump: sometimes the text element is empty.
		// Those itemDocuments are discarded.
		if(revision.getText().equals("")){
			emptyJsonStatistics.addValue(revision.getRevisionId());
			logger.debug("Empty text element: Revision" + revision.getRevisionId());
		}
		else{
			ParsingResult parsingResult;
			
			try{
				// Might throw a JSONException or NullPointerException (see below)
				parsingResult = parseJson(revision.getText());
				
				// Does the item document represent a redirect?
				// Those itemDocuments are discarded.
				if (parsingResult.jsonVersion == JsonVersion.REDIRECT){
					redirectStatistics.addValue(revision.getRevisionId());
				}
				else {
					// Issue in the database dump: sometimes the item id in the JSON contradicts the item id in the XML.
					// Those itemDocuments are discarded.
					int jsonItemId = Revision.getItemIdFromString(parsingResult.itemDocument.getItemId().getId());
					if (jsonItemId != revision.getItemId()){
						inconsistentJsonXMLStatistics.addValue(revision.getRevisionId());
						logger.debug("Inconsistent JSON: Revision " + revision.getRevisionId() + 
								": XML item id Q" + revision.getItemId() + " <-> JSON item id Q" + jsonItemId );
					}
					// Everything is fine: set this item document
					else{
						if (parsingResult.jsonVersion == JsonVersion.NEW){
							newJsonStatistics.addValue(revision.getRevisionId());
						}
						else if (parsingResult.jsonVersion == JsonVersion.OLD){
							oldJsonStatistics.addValue(revision.getRevisionId());
						}
						
						revision.setItemDocument(parsingResult.itemDocument);		
					}
				}
				
				
			}
			// Issue in the database dump: sometimes the JSON contains an invalid globe coordinate.
			// Those itemDocuments are discarded.
			catch(NullPointerException e){
				nullPointerExceptionStatistics.addValue(revision.getRevisionId());
				logger.debug("NullPointerException: Revision " + revision.getRevisionId() + ": "
						+ e.getMessage() + ": \n"
						+ revision.toString() + "\n"
						+ revision.getText(), e);
			}
			// Issue in the database dump: sometimes the JSON in the text element cannot be parsed.
			// Those itemDocuments are discarded.			
			catch(JSONException e){
				jsonExceptionStatistics.addValue(revision.getRevisionId());
				logger.debug("JSON Exception: Revision " + revision.getRevisionId() + ": "
						+ e.getMessage() + ": \n"
						+ revision.toString() + "\n"
						+ revision.getText(), e);
			}			

		}		
		
		if(processor != null){
			processor.processRevision(revision);
		}
		
	}

	@Override
	public void finishRevisionProcessing() {
		logger.debug("Starting to finish...");
		
		if(processor != null){
			processor.finishRevisionProcessing();
		}
		
		logger.debug("Empty JSON values, statistics of revisionIDs: " + emptyJsonStatistics.toString());
		logger.debug("Inconsistent JSON values, statistics of revision IDs: " + inconsistentJsonXMLStatistics.toString());
		logger.debug("JSON Exceptions, statistics of revisionIDs: " + jsonExceptionStatistics.toString());
		logger.debug("Finished.");
	}	
	
	/**
	 * Parses the JSON contained in the XML 'text' element and returns an item document.
	 * If the 'text' element represents a redirect, this method returns null. 
	 * If the 'text' element cannot be parsed, a JSONException is thrown.
	 * 
	 */
	public static ParsingResult parseJson(String text) throws JSONException{
		ParsingResult result;

		// try to read the new Json format
		try{
			JacksonItemDocument jacksonItemDocument = newFormatMapper.readValue(text, JacksonItemDocument.class);
			jacksonItemDocument.setSiteIri(Datamodel.SITE_WIKIDATA);
			
			result = new ParsingResult(JsonVersion.NEW, jacksonItemDocument);
		}
		catch(Exception e){
			// This exception is raised very often
			// See the following bug reports:
			// https://github.com/Wikidata/Wikidata-Toolkit/issues/105
			// https://phabricator.wikimedia.org/T74348			
			
			// Workaround, to read the old format
			// (It is only partially supported yet and does not extract all information)
			try{
				OldJacksonItemDocument oldDoc = oldFormatMapper.readValue(text, OldJacksonItemDocument.class);
				JacksonItemDocument jacksonItemDocument = JsonNormalizer.normalizeFormat(oldDoc);
				jacksonItemDocument.setSiteIri(Datamodel.SITE_WIKIDATA);
				
				// serialize and deserialize in order to double check that the
				// JsonNormalizer worked as expected
				JsonNode node = oldFormatMapper.valueToTree(jacksonItemDocument);
				oldFormatMapper.readValue(oldFormatMapper.treeAsTokens(node), JacksonItemDocument.class);
				
				result = new ParsingResult(JsonVersion.OLD, jacksonItemDocument);
			}
			catch(Exception e2){				
				// Is it a redirect? Then do not log the exception
				try{
					redirectMapper.readValue(text, OldJacksonRedirectDocument.class);
					
					result = new ParsingResult(JsonVersion.REDIRECT, null);
				}
				catch (Exception e3){
					throw new JSONException("Format could neither be parsed as new JSON, old JSON nor as a redirect.", e2);
				}
			}			
		}		

		if (result.itemDocument != null){
			 // Convert from Jackson to Object representation which consumes less memory and is easier to work with.
			 DatamodelConverter converter = new DatamodelConverter(new DataObjectFactoryImpl());
			 // Throws a NullPointerException in some rare circumstances (if the globe coordinate is null)
			 result.itemDocument = converter.copy(result.itemDocument);
		}

		return result;
	}

	public SummaryStatistics getEmptyJsonStatistics() {
		return emptyJsonStatistics;
	}

	public SummaryStatistics getInconsistentJsonXMLStatistics() {
		return inconsistentJsonXMLStatistics;
	}
	
	public SummaryStatistics getJsonExceptionStatistics() {
		return jsonExceptionStatistics;
	}
	
	public SummaryStatistics getNullPointerExceptionStatistics() {
		return nullPointerExceptionStatistics;
	}
	
	public SummaryStatistics getNewJsonStatistics() {
		return newJsonStatistics;
	}
	
	public SummaryStatistics getOldJsonStatistics() {
		return oldJsonStatistics;
	}
	
	public SummaryStatistics getRedirectStatistics() {
		return redirectStatistics;
	}
}

enum JsonVersion {NEW, OLD, REDIRECT};

class ParsingResult{
	ItemDocument itemDocument;
	JsonVersion jsonVersion;
	
	public ParsingResult(JsonVersion jsonVersion, ItemDocument itemDocument){
		this.jsonVersion = jsonVersion;
		this.itemDocument = itemDocument;
	}
}

class JSONException extends Exception{
	private static final long serialVersionUID = 1L;
	JSONException(String str){
		super(str);
	}
	
	JSONException(String str, Exception e){
		super(str, e);
	}
}
