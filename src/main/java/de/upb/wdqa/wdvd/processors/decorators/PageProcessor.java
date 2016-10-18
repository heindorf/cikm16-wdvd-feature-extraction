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
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.ItemStore;
import de.upb.wdqa.wdvd.db.implementation.ItemDocumentDbItem;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class PageProcessor implements RevisionProcessor {
	static final Logger logger = LoggerFactory.getLogger(PageProcessor.class);
	
	RevisionProcessor processor;
	
	long lastTimeProgressLogged = System.currentTimeMillis();
	final long PROGRESS_LOGGING_INTERVAL = 10000; // 10 seconds
	
	List<Revision> revisionsOfCurrentPage = new ArrayList<Revision>();
	int previousPageId = -1;
	
	ItemStore itemStore;
	
	public PageProcessor(RevisionProcessor processor, ItemStore itemStore) {
		this.processor = processor;
		this.itemStore = itemStore;
	}


	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		
		itemStore.connect();
		itemStore.deleteItems();


		processor.startRevisionProcessing();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.upb.wdqa.wdvd.processors.RevisionProcessor#processRevision(de.upb.wdqa
	 * .wdvd.Revision)
	 */
	@Override
	public void processRevision(Revision revision) {
		try {
//			if(revision.getNamespace() == 0){	
				
			// Check whether a new page has been started (and thus the previous
			// page is complete).  In case an exception has been thrown before,
			// check that there is at least one revision to process
			// (otherwise further exceptions will be thrown)
				if (previousPageId != -1
					&& revision.getPageId() != previousPageId
					&& revisionsOfCurrentPage.size() > 0) {
						processPage();
						revisionsOfCurrentPage.clear();
				}
			
				previousPageId = revision.getPageId();
				revisionsOfCurrentPage.add(revision);
//			}
		} catch (OutOfMemoryError e) {
			logger.error("", e);
			logger.error(revision.toString());
			logger.error("Size of list: " + revisionsOfCurrentPage.size());
			revisionsOfCurrentPage.clear();
		} catch (Throwable e) {
			logger.error("", e);
			logger.error(revision.toString());
			logger.error("Size of list: " + revisionsOfCurrentPage.size());
			revisionsOfCurrentPage.clear();
		}
	}

	@Override
	public void finishRevisionProcessing() {
		processPage();
		processor.finishRevisionProcessing();
		
		itemStore.close();
		
		logger.debug("Finished.");
	}
	
	private void processPage() {
		if (System.currentTimeMillis() - lastTimeProgressLogged > PROGRESS_LOGGING_INTERVAL) {
			logger.info("Processing item " + revisionsOfCurrentPage.get(0).getPrefixedTitle());
			lastTimeProgressLogged = System.currentTimeMillis();
		}
		
		Revision firstRevision = revisionsOfCurrentPage.get(0);
		Revision latestRevision = revisionsOfCurrentPage.get(revisionsOfCurrentPage.size() - 1);
		
		// Find the latest parsable JSON
		ItemDocument latestItemDocument = null;
		for (int index = revisionsOfCurrentPage.size() - 1; index >= 0; index--) {
			ItemDocument itemDocument = revisionsOfCurrentPage.get(index).getItemDocument();
			
			if (itemDocument != null) {
				latestItemDocument = itemDocument;
				itemStore.insertItem(new ItemDocumentDbItem(itemDocument));
				
				break;
			}
		}
		
		if (latestItemDocument == null) {
			logger.debug("Could not parse JSON of any revision: "
					+ firstRevision.getPrefixedTitle());
		}
		
		
		Revision prevRevision = null;		
		for (Revision revision: revisionsOfCurrentPage) {
			revision.setLatestRevision(latestRevision);
			revision.setPreviousRevision(prevRevision);
			
			revision.setLatestItemDocument(latestItemDocument);
			
			// Check whether revisions are sorted increasingly
			checkIncreasingRevisionId(prevRevision, revision);	
			
			processor.processRevision(revision);
			prevRevision = revision;
		}
		
	}
	
	private static void checkIncreasingRevisionId(Revision prev, Revision cur) {
		if (prev != null && prev.getRevisionId() >= cur.getRevisionId()) {
			logger.warn("Revision IDs should be increasing, prev revision: "
					+ prev.toString() + ", current Revision: " + cur.toString());
		}
	}

}
