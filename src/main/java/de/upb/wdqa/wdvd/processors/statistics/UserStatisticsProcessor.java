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

import org.apache.commons.math3.stat.Frequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.labels.RevertMethod;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class UserStatisticsProcessor implements RevisionProcessor {

	static final Logger logger =
			LoggerFactory.getLogger(UserStatisticsProcessor.class);
	
	private Frequency botDistribution = new Frequency();
	private Frequency userDistribution = new Frequency();
	private long numberOfRegistered = 0;
	private long numberOfUnregistered = 0;
	private long numberOfMinor = 0;
	private Frequency rollbackedUserDistribution = new Frequency();
	private Frequency rollbackingUserDistribution = new Frequency();
	private Frequency registeredRollbackedUserDistribution = new Frequency();
	private Frequency unregisteredRollbackedUserDistribution = new Frequency();

	
	RevisionProcessor processor;
	
	public UserStatisticsProcessor(RevisionProcessor processor) {
		super();
		this.processor = processor;
	}

	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		processor.startRevisionProcessing();		
	}

	@Override
	public void processRevision(Revision revision) {
		processor.processRevision(revision);
		
		if (revision.hasBotContributor()) {
			botDistribution.addValue(revision.getContributor());
		} else {
			userDistribution.addValue(revision.getContributor());
		}
		
		
		if (revision.hasRegisteredContributor()) {
			numberOfRegistered += 1;
		} else {
			numberOfUnregistered += 1;
		}
		
		//If the revision is minor the function isMinor returns "", otherwise null
		if (revision.isMinor()) {
			numberOfMinor += 1;
		}
		
		
		if (revision.wasReverted(RevertMethod.ROLLBACK)) {
			rollbackedUserDistribution.addValue(revision.getContributor());			
			
			if (revision.hasRegisteredContributor()) {
				registeredRollbackedUserDistribution.addValue(revision.getContributor());
			} else {
				unregisteredRollbackedUserDistribution.addValue(revision.getContributor());
			}
		}
		


		/////////////////////////////////////////////
//		logResults();
	}

	@Override
	public void finishRevisionProcessing() {
		processor.finishRevisionProcessing();		
		logResults();	
	}
	
	private void logResults() {
		logger.info("Number of registered revisions: " + numberOfRegistered);
		logger.info("Number of unregistered revision: " + numberOfUnregistered);
		logger.info("Number of minor revisions: " + numberOfMinor);
		

		logger.info("Number of bot revisions: " + botDistribution.getSumFreq());
		logger.info("Number of non-bot revisions: " + userDistribution.getSumFreq());
		logger.info("Number of unique bots: " + botDistribution.getUniqueCount());
		logger.info("Number of unique non-bot users: " + userDistribution.getUniqueCount());
		
		
		logger.info("Number of unique users who have performed a rollback: " + rollbackingUserDistribution.getUniqueCount());
		logger.info("Distribution of users who have performed a rollback:\n" + FrequencyUtils.formatFrequency(rollbackingUserDistribution, 20));
		logger.info("Number of unique users who were rollbacked: " + rollbackedUserDistribution.getUniqueCount());
		logger.info("Distribution of users who were rollbacked:\n" + FrequencyUtils.formatFrequency(rollbackedUserDistribution, 20));
		logger.info("Number of unique registered users who were rollbacked: " + registeredRollbackedUserDistribution.getUniqueCount());
		logger.info("Distribution of registered users who were rollbacked: " + FrequencyUtils.formatFrequency(registeredRollbackedUserDistribution, 20));
		logger.info("Number of unique unregistered users who were rollbacked: " + unregisteredRollbackedUserDistribution.getUniqueCount());
		logger.info("Distribution of unregistered users who were rollbacked: " + FrequencyUtils.formatFrequency(unregisteredRollbackedUserDistribution, 20));
		
		
		logger.info("Bot frequency distribution:\n" + FrequencyUtils.formatFrequency(botDistribution));
		logger.info("Most frequent users:\n" + FrequencyUtils.formatFrequency(userDistribution, 100));
	}

}
