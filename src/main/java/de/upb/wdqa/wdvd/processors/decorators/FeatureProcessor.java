package de.upb.wdqa.wdvd.processors.decorators;

import java.util.List;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.Feature;
import de.upb.wdqa.wdvd.features.FeatureValue;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;


public class FeatureProcessor implements RevisionProcessor {	
	RevisionProcessor processor;
	List<Feature> features;
	
	public FeatureProcessor(RevisionProcessor processor, List<Feature> features){
		this.processor = processor;
		this.features = features;
	}
	
	@Override
	public void startRevisionProcessing() {
		if (processor != null){
			processor.startRevisionProcessing();
		}
		
	}
	
	@Override
	public void processRevision(Revision revision) {
		for(Feature feature: features){
			FeatureValue value = feature.calculate(revision);
			revision.setFeatureValue(feature, value);
		}
		
		if(processor != null){
			processor.processRevision(revision);
		}
		
	}
	
	@Override
	public void finishRevisionProcessing() {
		if(processor != null){
			processor.finishRevisionProcessing();
		}
	}
}


