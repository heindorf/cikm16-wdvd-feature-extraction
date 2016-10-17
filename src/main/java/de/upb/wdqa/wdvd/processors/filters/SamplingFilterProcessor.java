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

package de.upb.wdqa.wdvd.processors.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.labels.RevertMethod;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class SamplingFilterProcessor implements RevisionProcessor {
	final static Logger logger = LoggerFactory.getLogger(SamplingFilterProcessor.class);
	
	private final double lowQualitySamplingRate;
	private final double highQualitySamplingRate;
	private final RevertMethod revertMethod;

	private long lowQualitySamplingFiltered;
	private long highQualitySamplingFiltered;

	private final RevisionProcessor  processor;

	public SamplingFilterProcessor(RevisionProcessor processor,
			double lowQualitySamplingRate, double highQualitySamplingRate, RevertMethod revertMethod) {
		super();
		this.processor = processor;
		this.lowQualitySamplingRate = lowQualitySamplingRate;
		this.highQualitySamplingRate = highQualitySamplingRate;
		this.revertMethod = revertMethod;
	}



	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting (" + revertMethod + ")...");
		logger.info("Low quality sampling rate: " + lowQualitySamplingRate);
		logger.info("High quality sampling rate: " + highQualitySamplingRate);
		
		processor.startRevisionProcessing();		
	}

	@Override
	public void processRevision(Revision revision) {
		boolean isLowQuality = revision.wasReverted(revertMethod);
		
//		switch(revertMethod){
//		case ROLLBACK:
//			isLowQuality = revision.wasRollbackReverted();
//			break;
//		case UNDO_RESTORE:
//			isLowQuality = revision.wasUndoReverted() || revision.wasRestoreReverted();
//			break;
//		case SHA1:
//			isLowQuality = revision.wasSha1Reverted();
//			break;
//		case DOWNLOADED_SHA1:
//			isLowQuality = revision.wasDownloadedSha1Reverted();
//			break;
//		default:
//			throw new RuntimeException("Unknown Output File.");		
//		}		

		if (isLowQuality){
			// Sampling of low quality revisions
			if (Math.random() <= lowQualitySamplingRate){
				processor.processRevision(revision);
			}
			else{
				lowQualitySamplingFiltered += 1;
			}
		}
		else{
			// Sampling of high quality revisions
			if (Math.random () <= highQualitySamplingRate){
				processor.processRevision(revision);
			}
			else{
				highQualitySamplingFiltered += 1;
			}
		}		
	}



	@Override
	public void finishRevisionProcessing() {
		processor.finishRevisionProcessing();
		
		logFilteredRevisions();		
	}



	private void logFilteredRevisions() {
		logger.info(revertMethod.toString() + ": Number of revisions filtered because of low quality sampling: " + lowQualitySamplingFiltered);
		logger.info(revertMethod.toString() + ": Number of revisions filtered because of high quality sampling: " + highQualitySamplingFiltered);
	}
}
