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

package de.upb.wdqa.wdvd.processors.statistics;

import java.util.EnumMap;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.labels.RevertMethod;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class LabelingStatisticsProcessor implements RevisionProcessor {
	static final Logger logger =
			LoggerFactory.getLogger(LabelingStatisticsProcessor.class);
	
	private long totalNumberOfRevisions;
	
	private EnumMap<RevertMethod, Integer> reverted = new EnumMap<RevertMethod, Integer>(RevertMethod.class);
	private EnumMap<RevertMethod, Integer> revertedWithin7Days = new EnumMap<RevertMethod, Integer>(RevertMethod.class);
	private EnumMap<RevertMethod, Integer> revisionsRevertedByBots = new EnumMap<RevertMethod, Integer>(RevertMethod.class);
	private EnumMap<RevertMethod, SummaryStatistics> timeUntilRevert = new EnumMap<RevertMethod, SummaryStatistics>(RevertMethod.class);
	
	long numberOfRollbackRevertsNotDetectedBySha1;
	long numberOfRollbackRevertsNotDetectedByDownloadedSha1;
	long numberOfUndoRestoreRevertsNotDetectedBySha1;
	long numberOfUndoRestoreRevertsNotDetectedByDownloadedSha1;
	
	RevisionProcessor processor;
	
	public LabelingStatisticsProcessor(RevisionProcessor processor) {
		this.processor = processor;
	}
	
	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		processor.startRevisionProcessing();
		
		for (RevertMethod method: RevertMethod.values()) {
			reverted.put(method, 0);
			revertedWithin7Days.put(method, 0);
			revisionsRevertedByBots.put(method, 0);
			timeUntilRevert.put(method, new SummaryStatistics());
		}
	}
	
	@Override
	public void processRevision(Revision revision) {
		processor.processRevision(revision);
		
		for (RevertMethod method: RevertMethod.values()) {
			
			if (revision.wasReverted(method)) {
				reverted.put(method, reverted.get(method) + 1);
			}	
		}
		
			
		totalNumberOfRevisions += 1;
		
	}
	
	@Override
	public void finishRevisionProcessing() {
		processor.finishRevisionProcessing();
		
		logResults();
	}

	private void logResults() {
		logger.info("Total number of revisions: " + totalNumberOfRevisions);
		
		for (RevertMethod method: RevertMethod.values()) {
			//Number of reverted revisions
			logger.info(method + ": reverted revisions: " + reverted.get(method));
			// Number of revisions that have been reverted within 7 days
			logger.info(method + ": reverted revisions within 7 days: " + revertedWithin7Days.get(method));
			// Number of reverted revisions by bots
			logger.info(method + ": number of revisions that were reverted by bots: " + revisionsRevertedByBots.get(method));
			// Time until revert (some statistics, e.g., average time)
			logger.info(method + ": reverted revisions: time until revert (in seconds): " + "Min: "
					+ (int) timeUntilRevert.get(method).getMin() + ", Max: " + (int) timeUntilRevert.get(method).getMax() + ", Mean: " + timeUntilRevert.get(method).getMean());
		}
		
		logger.info("Number of rollback-reverted revisions not detected by sha1 revert: " +  numberOfRollbackRevertsNotDetectedBySha1);
		logger.info("Number of rollback-reverted revisions not detected by downloaded sha1 revert: " + numberOfRollbackRevertsNotDetectedByDownloadedSha1);
		logger.info("Number of undo-restore-reverted revisions not detected by sha1 revert: " + numberOfUndoRestoreRevertsNotDetectedBySha1);
		logger.info("Number of undo-restore-reverted revision not detected by downloaded sha1 revert: " + numberOfUndoRestoreRevertsNotDetectedByDownloadedSha1);
	}

}
