package de.upb.wdqa.wdvd.processors.preprocessing;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.dumpfiles.MwRevision;
import org.wikidata.wdtk.dumpfiles.MwRevisionProcessor;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class AdapterProcessor implements MwRevisionProcessor {
	static final Logger logger = LoggerFactory.getLogger(AdapterProcessor.class);
	
	RevisionProcessor processor;
	
	public AdapterProcessor(RevisionProcessor processor){
		logger.debug("Starting...");
		this.processor = processor;
	}

	@Override
	public void startRevisionProcessing(String siteName, String baseUrl,
			Map<Integer, String> namespaces) {
		processor.startRevisionProcessing();		
	}

	@Override
	public void processRevision(MwRevision mwRevision) {
		processor.processRevision(new Revision(mwRevision));		
	}

	@Override
	public void finishRevisionProcessing() {
		processor.finishRevisionProcessing();		
	}
}
