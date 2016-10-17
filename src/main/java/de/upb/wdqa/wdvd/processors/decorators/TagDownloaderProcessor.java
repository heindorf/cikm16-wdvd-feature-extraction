/*
 * Wikidata Vandalism Detector 2016 (WDVD-2016)
 * 
 * Copyright (c) 2016 Stefan Heindorf, Martin Potthast, Benno Stein, Gregor Engels
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
	
	static final Logger logger =
			LoggerFactory.getLogger(TagDownloaderProcessor.class);
	
	RevisionProcessor processor;
	File revisionTagFile;
	
	static final int BATCH_SIZE = 1000;
	List<Revision> revisions = new ArrayList<Revision>(BATCH_SIZE);
	
	public TagDownloaderProcessor(
			RevisionProcessor processor, File revisionTagFile){
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
		// must be called before finishing because otherwise threads might have
		// been already killed
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
