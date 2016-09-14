package de.upb.wdqa.wdvd.processors.controlflow;

import java.util.ArrayList;
import java.util.List;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class TeeProcessor implements RevisionProcessor {
	
	List<RevisionProcessor> processors = new ArrayList<>();

	@Override
	public void startRevisionProcessing() {
		for(RevisionProcessor processor: processors){
			processor.startRevisionProcessing();
		}		
	}

	@Override
	public void processRevision(Revision revision) {
		for(RevisionProcessor processor: processors){
			processor.processRevision(new Revision(revision));
		}		
	}

	@Override
	public void finishRevisionProcessing() {
		for(RevisionProcessor processor: processors){
			processor.finishRevisionProcessing();
		}
	}
	
	public void add(RevisionProcessor processor) {
		processors.add(processor);
	}
	

}
