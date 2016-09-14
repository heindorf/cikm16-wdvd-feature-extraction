package de.upb.wdqa.wdvd.processors.decorators;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.geolocation.GeoInformation;
import de.upb.wdqa.wdvd.geolocation.GeolocationDatabase;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class GeolocationProcessor implements RevisionProcessor {
	
	static final Logger logger = LoggerFactory.getLogger(GeolocationProcessor.class);
	
	RevisionProcessor processor;
	File geolocationFile;
	
	public GeolocationProcessor(RevisionProcessor processor, File geolocationFile){
		this.processor = processor;
		this.geolocationFile = geolocationFile;
	}

	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		GeolocationDatabase.readFile(this.geolocationFile);
		
		
		processor.startRevisionProcessing();		
	}

	@Override
	public void processRevision(Revision revision) {
		if (!revision.hasRegisteredContributor()){
			String userName = revision.getContributor();
			
			GeoInformation geoInformation = GeolocationDatabase.getGeoInformation(userName);
			
			revision.setGeoInformation(geoInformation);		
		}
		
		processor.processRevision(revision);
	}

	@Override
	public void finishRevisionProcessing() {
		processor.finishRevisionProcessing();
		
		logger.debug("Finished.");		
	}
	
}
