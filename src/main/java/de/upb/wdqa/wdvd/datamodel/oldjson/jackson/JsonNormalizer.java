package de.upb.wdqa.wdvd.datamodel.oldjson.jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonItemDocument;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonMonolingualTextValue;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonNoValueSnak;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonObjectFactory;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonReference;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonSiteLink;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonSnak;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonSomeValueSnak;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonStatement;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonValueSnak;
import org.wikidata.wdtk.datamodel.json.jackson.datavalues.JacksonInnerEntityId;
import org.wikidata.wdtk.datamodel.json.jackson.datavalues.JacksonValue;
import org.wikidata.wdtk.datamodel.json.jackson.datavalues.JacksonValueItemId;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.datavalues.NotImplementedValue;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.datavalues.OldJacksonValue;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.datavalues.OldJacksonValueItemId;


/**
 *  Converts an item document in the old format (OldJacksonItemDocument) into the new format (JacksonItemDocument).
 *  
 *  The old format has been reverse engineered based on the database dump and
 *  the new format that is described here: http://www.mediawiki.org/wiki/Wikibase/DataModel/JSON   
 *  
 */
public class JsonNormalizer {
	private static final Logger logger = Logger.getLogger(JsonNormalizer.class);
	
	public static JacksonItemDocument normalizeFormat(OldJacksonItemDocument oldDocument) throws JsonParseException, JsonMappingException, IOException{
		
		JacksonItemDocument newDocument = getNewJacksonItemDocument(oldDocument);
		
		return newDocument;
	}
	
	private static JacksonItemDocument getNewJacksonItemDocument(OldJacksonItemDocument oldItemDocument){
		JacksonItemDocument result = new JacksonItemDocument();
		
		result.setJsonId(oldItemDocument.getEntity().getId());
		
		Map<String, JacksonMonolingualTextValue> labels = getNewLabels(oldItemDocument.getLabels());
		Map<String, JacksonMonolingualTextValue> descriptions = getNewDescriptions(oldItemDocument.getDescriptions());
		Map<String, List<JacksonMonolingualTextValue>> aliases = getNewAliases(oldItemDocument.getAliases());
		Map<String, List<JacksonStatement>> claims = getNewStatements(oldItemDocument.getClaims());
		Map<String, JacksonSiteLink> sitelinks = getNewSiteLinks(oldItemDocument.getSiteLinks());
		
		result.setLabels(labels);
		result.setDescriptions(descriptions);
		result.setAliases(aliases);
		result.setJsonClaims(claims);

		result.setSiteLinks(sitelinks);
		
		return result;
	}
	
	private static Map<String, JacksonSiteLink> getNewSiteLinks(
			LinkedHashMap<String, OldJacksonSiteLink> siteLinks) {
		Map<String, JacksonSiteLink> result = new LinkedHashMap<String, JacksonSiteLink>();
		
		if(siteLinks != null){
			for(Entry<String,OldJacksonSiteLink> entry: siteLinks.entrySet()){
				String siteKey = entry.getKey();
				OldJacksonSiteLink oldSitelink = entry.getValue();
				
				JacksonObjectFactory factory = new JacksonObjectFactory();
				JacksonSiteLink sitelink = (JacksonSiteLink) factory.getSiteLink(
						oldSitelink.getName(),
						siteKey,
						oldSitelink.getBadges()
						);
				
				result.put(siteKey, sitelink);
			}
		}
		
		return result;
	}

	private static Map<String, List<JacksonStatement>> getNewStatements(
			List<OldJacksonStatement> claims) {
		Map<String, List<JacksonStatement>> result = new LinkedHashMap<String, List<JacksonStatement>>();
		
		if(claims != null){
			for(OldJacksonStatement oldStatement: claims){
				JacksonStatement statement = getNewStatement(oldStatement);
				JacksonSnak mainsnak = statement.getMainsnak();
				if(mainsnak != null){
					String property = mainsnak.getProperty();
					if(!result.containsKey(property)){
						result.put(property, new ArrayList<JacksonStatement>());
					}
					
					List<JacksonStatement> list = result.get(property);
					list.add(statement);			
					result.put(property, list);
				}
			}
		}
		
		return result;
	}
	
	private static JacksonStatement getNewStatement(
			OldJacksonStatement oldStatement) {
		JacksonStatement result = new JacksonStatement();
		
		if(oldStatement != null){
			result.setStatementId(oldStatement.getStatementId());
			
			result.setRank(oldStatement.getRank());			
						
			List<JacksonReference> references = getNewReferences(oldStatement.getReferences());
			result.setReferences(references);
			
			JacksonSnak mainsnak = getNewSnak(oldStatement.getMainsnak());		
			result.setMainsnak(mainsnak);
			
			Map<String, List<JacksonSnak>> qualifiers = getNewQualifiers(oldStatement.getQualifiers());
			result.setQualifiers(qualifiers);			
			List<String> qualifierPropertyOrder = getNewPropertyOrder(oldStatement.getQualifiers());
			result.setPropertyOrder(qualifierPropertyOrder );
			
			// Remark: the subject must not be set here. It is later automatically set
			// by a call to JacksonTermedStatementDocument.java#setSiteIri			
		}	
		
		return result;
	}	

	private static Map<String, List<JacksonSnak>> getNewQualifiers(
			List<OldJacksonSnak> qualifiers) {
		Map<String, List<JacksonSnak>> result = new LinkedHashMap<String, List<JacksonSnak>>();
		
		if(qualifiers != null){
			for(OldJacksonSnak oldSnak: qualifiers){
				JacksonSnak snak = getNewSnak(oldSnak);
				String property = snak.getProperty();
				if(property != null){
					if(!result.containsKey(property)){
						result.put(property, new ArrayList<JacksonSnak>());
					}
					
					List<JacksonSnak> list = result.get(property);
					list.add(snak);			
					result.put(property, list);
				}
			}
		}
		
		return result;
	}
	
	private static Map<String, List<JacksonSnak>> getNewReferenceSnaks(
			List<OldJacksonSnak> reference) {
		return getNewQualifiers(reference);
	}

	private static JacksonSnak getNewSnak(OldJacksonSnak snak) {
		JacksonSnak result = null;
		
		if(snak instanceof OldJacksonValueSnak){
			OldJacksonValueSnak snak2 = (OldJacksonValueSnak) snak;
			JacksonValueSnak result2 = new JacksonValueSnak();
			result2.setProperty(snak2.getProperty());
			result2.setDatatype(snak2.getDatatype());
			result2.setProperty(snak2.getProperty());
			result2.setDatavalue(getNewDataValue(snak2.getDatavalue()));
			result = result2;
		}
		else if(snak instanceof OldJacksonNoValueSnak){
			OldJacksonNoValueSnak snak2 = (OldJacksonNoValueSnak) snak;
			JacksonNoValueSnak result2 = new JacksonNoValueSnak();
			result2.setProperty(snak2.getProperty());
			result = result2;
		}
		else if(snak instanceof OldJacksonSomeValueSnak){
			OldJacksonSomeValueSnak snak2 = (OldJacksonSomeValueSnak) snak;
			JacksonSomeValueSnak result2 = new JacksonSomeValueSnak();
			result2.setProperty(snak2.getProperty());
			result = result2;
		}
		else{
			logger.warn("Unknown OldJacksonValueSnak Type: " + snak.getClass());
		}
		
		return result;
	}

	private static JacksonValue getNewDataValue(OldJacksonValue datavalue) {
		if (datavalue instanceof OldJacksonValueItemId){
			OldJacksonValueItemId datavalue2 = (OldJacksonValueItemId) datavalue;
			JacksonValueItemId result = new JacksonValueItemId();
			result.setType(JacksonValue.JSON_VALUE_TYPE_ENTITY_ID);
			result.setValue(new JacksonInnerEntityId(datavalue2.getEntityType(), datavalue2.getNumericId()));
			return result;
		}
		else{
			// Implement those other values when they are necessary (up until now they are not necessary)		
			return new NotImplementedValue();
		}
	}

	private static Map<String, JacksonMonolingualTextValue> getNewLabels(
			LinkedHashMap<String, String> labels) {
		LinkedHashMap<String, JacksonMonolingualTextValue> result = new LinkedHashMap<String, JacksonMonolingualTextValue>();
		
		if(labels != null){
			for (Entry<String, String> entry : labels.entrySet()) {
				JacksonMonolingualTextValue mltv = new JacksonMonolingualTextValue(entry.getKey(), entry.getValue());
				result.put(entry.getKey(), mltv);
			}
		}
		
		return result;
	}
	
	private static Map<String, JacksonMonolingualTextValue> getNewDescriptions(
			LinkedHashMap<String, String> descriptions) {
		
		return getNewLabels(descriptions);
	}
	
	// similar to method "getNewStatements"
	private static Map<String, List<JacksonMonolingualTextValue>> getNewAliases(
			LinkedHashMap<String, List<String>> aliases) {
		Map<String, List<JacksonMonolingualTextValue>> result = new LinkedHashMap<String, List<JacksonMonolingualTextValue>>();
		
		if(aliases != null){
			for(Entry<String, List<String>> entry: aliases.entrySet()){
				String languageCode = entry.getKey();
				List<String> langAliases = entry.getValue();			
	
				List<JacksonMonolingualTextValue >list = new ArrayList<JacksonMonolingualTextValue>();
				result.put(languageCode, list);
				
				for(String langAlias: langAliases){
					JacksonMonolingualTextValue mltv = new JacksonMonolingualTextValue(languageCode, langAlias);
					list.add(mltv);
				}
			}
		}
		
		return result;
	}
	
	private static List<JacksonReference> getNewReferences(List<List<OldJacksonSnak>> oldReferences){
		List<JacksonReference> result = new ArrayList<JacksonReference>();
		
		// A single reference consists of a list of snaks
		for (List<OldJacksonSnak> oldReference: oldReferences){
			JacksonReference newReference = new JacksonReference();
			
			Map<String, List<JacksonSnak>> newSnaks = getNewReferenceSnaks(oldReference);
			newReference.setSnaks(newSnaks);
			
			List<String> newPropertyOrder = getNewPropertyOrder(oldReference);
			newReference.setPropertyOrder(newPropertyOrder);
			
			result.add(newReference);
		}

		
		return result;
	}

	private static List<String> getNewPropertyOrder(List<OldJacksonSnak> oldReference) {
		List<String> result = new ArrayList<String>();
		
		for (OldJacksonSnak oldSnak: oldReference){
			String property = oldSnak.getProperty();
			
			result.add(property);
		}
		
		return result;
	}



}
