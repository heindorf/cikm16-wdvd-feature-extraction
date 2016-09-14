package de.upb.wdqa.wdvd.test;
import java.util.ArrayList;
import java.util.List;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class TestProcessor implements RevisionProcessor{

	private int startRevisionProcessingCount = 0;
	private int processRevisionProcessingCount = 0;
	private int finishRevisionProcessingCount = 0;
	private List<Revision> revisions = new ArrayList<Revision>();	

	@Override
	public void startRevisionProcessing() {
		startRevisionProcessingCount++;
	}

	@Override
	public void processRevision(Revision revision) {
		processRevisionProcessingCount++;
		revisions.add(revision);
	}

	@Override
	public void finishRevisionProcessing() {
		finishRevisionProcessingCount++;		
	}
	 
	
	public int getStartRevisionProcessingCount() {
		return startRevisionProcessingCount;
	}
	
	public int getProcessRevisionProcessingCount() {
		return processRevisionProcessingCount;
	}
	
	public int getFinishRevisionProcessingCount() {
		return finishRevisionProcessingCount;
	}
	
	public List<Revision> getRevisions() {
		return revisions;
	}
	
	public static void executeProcessor(RevisionProcessor processor, List<Revision> revisions){
		processor.startRevisionProcessing();
		
		for(Revision revision: revisions){
			processor.processRevision(revision);
		}
		
		processor.finishRevisionProcessing();		
	}
};