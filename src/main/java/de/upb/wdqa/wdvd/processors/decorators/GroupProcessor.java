package de.upb.wdqa.wdvd.processors.decorators;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;


public class GroupProcessor implements RevisionProcessor {
	
	static final Logger logger = LoggerFactory.getLogger(GroupProcessor.class);
	
	RevisionProcessor processor;
	
	List<Revision> revisionsOfCurrentGroup = new ArrayList<Revision>();
	int prevPageId = -1;
	String prevContributor;
	
	public GroupProcessor(RevisionProcessor processor) {
		this.processor = processor;	
	}

	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		processor.startRevisionProcessing();		
	}

	@Override
	public void processRevision(Revision revision) {
		boolean samePage = (revision.getPageId() == prevPageId);
		boolean sameContributor = (revision.getContributor().equals(prevContributor));
		
		// Is this the start of a new group?
		if (!samePage || !sameContributor  ){
			processGroup(revisionsOfCurrentGroup);
			revisionsOfCurrentGroup.clear();
		}
			
		revisionsOfCurrentGroup.add(revision);
		prevPageId = revision.getPageId();
		prevContributor = revision.getContributor();		
	}

	@Override
	public void finishRevisionProcessing() {
		processGroup(revisionsOfCurrentGroup);

		processor.finishRevisionProcessing();
		logger.debug("Finished.");
	}
	
	private void processGroup(List<Revision> revisions) {
		if (revisions.size() > 0){
			long groupId = revisions.get(0).getRevisionId();
			
			for (int i = 0; i < revisions.size(); i++){
				Revision revision = revisions.get(i);
				
				revision.setPositionWithinGroup(i + 1);
				revision.setRevisionGroupId(groupId);
			}
		}
			
		for (Revision revision: revisions){				
			processor.processRevision(revision);
		}				
	}	
}


