package de.upb.wdqa.wdvd.processors.decorators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.interfaces.DbRevision;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;
import de.upb.wdqa.wdvd.revisiontags.TagDownloader;

public class TagDownloaderProcessor implements RevisionProcessor {
	
	static final Logger logger = LoggerFactory.getLogger(TagDownloaderProcessor.class);
	
	RevisionProcessor processor;
	File revisionTagFile;
	
	static final int BATCH_SIZE = 1000;
	List<Revision> revisions = new ArrayList<Revision>(BATCH_SIZE);
	
	public TagDownloaderProcessor(RevisionProcessor processor, File revisionTagFile){
		this.processor = processor;
		this.revisionTagFile = revisionTagFile;
	}


	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		TagDownloader.readFile(this.revisionTagFile);
		processor.startRevisionProcessing();		
	}

	@Override
	public void processRevision(Revision revision) {
		revisions.add(revision);
		
		if(revisions.size() >= BATCH_SIZE){
			processAllRevisions();
		}
	}

	@Override
	public void finishRevisionProcessing() {
		// must be called before finishing because otherwise threads might have been already killed
		processAllRevisions(); 
		
		processor.finishRevisionProcessing();
		
		logger.debug("Finished.");		
	}
	
	private void processAllRevisions(){
		List<Long> revisionIds = new ArrayList<Long>();
		
		for(Revision revision: revisions){
			revisionIds.add(revision.getRevisionId());
		}
		
		List<DbRevision> result = TagDownloader.getRevisions(revisionIds);
		
		for(int i = 0; i < revisions.size(); i++){
			attachInformation(revisions.get(i), result.get(i));
		}
		
		for (Revision revision: revisions){
			processor.processRevision(revision);
		}
		
		revisions.clear();
	}
	
	private void attachInformation(Revision revision, DbRevision dbRevision){		
		if(dbRevision != null){		
			revision.setDownloadedSha1(dbRevision.getSha1());
			revision.setDownloadedTags(dbRevision.getTagNames());
		}
	}

}
