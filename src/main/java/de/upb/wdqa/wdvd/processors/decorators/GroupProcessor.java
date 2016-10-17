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
		boolean sameContributor =
				(revision.getContributor().equals(prevContributor));
		
		// Is this the start of a new group?
		if (!samePage || !sameContributor) {
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
		if (revisions.size() > 0) {
			long groupId = revisions.get(0).getRevisionId();
			
			for (int i = 0; i < revisions.size(); i++) {
				Revision revision = revisions.get(i);
				
				revision.setPositionWithinGroup(i + 1);
				revision.setRevisionGroupId(groupId);
			}
		}
			
		for (Revision revision: revisions) {
			processor.processRevision(revision);
		}
	}

}
