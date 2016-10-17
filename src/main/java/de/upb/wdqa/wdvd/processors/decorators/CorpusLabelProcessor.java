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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.labels.CorpusLabel;
import de.upb.wdqa.wdvd.labels.CorpusLabelReader;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

/**
 * This class is a MwRevisionProcessor and processes the revisions (which are
 * stored in a bzip2-compressed XML-file) to computes statistics.
 *
 */
public class CorpusLabelProcessor implements RevisionProcessor {

	static final Logger logger = LoggerFactory
			.getLogger(CorpusLabelProcessor.class);

	// for calculating the statistics
	int numberOfRevisions;
	int numberOfRollbackedRevisions;
	int numberOfRegisteredRevisions;
	int numberOfRegisteredRollbackedRevisions;

	RevisionProcessor processor;
	File labelFile;
	CorpusLabelReader labelReader;

	public CorpusLabelProcessor( RevisionProcessor processor, File labelFile) {
		this.processor = processor;
		this.labelFile = labelFile;
	}

	@Override
	public void startRevisionProcessing() {
		logger.info("Starting...");
		
		InputStream compressedLabelsInputStream;
		
		try {
			compressedLabelsInputStream = new FileInputStream(labelFile);

			InputStream uncompressedLabelsInputStream = new BZip2CompressorInputStream(
					new BufferedInputStream(compressedLabelsInputStream));
			
			labelReader = new CorpusLabelReader(uncompressedLabelsInputStream);
		
		} catch (IOException e) {
			logger.error("", e);
		}		
		
		processor.startRevisionProcessing();
		
		labelReader.startReading();
	}

	@Override
	public void processRevision(Revision revision) {
		CorpusLabel label = labelReader.getNextLabel();
	


		if (revision.getRevisionId() != label.getRevisionId()) {
			logger.error("Corpus and labeling file are out of sync.");
		}
		
		revision.setRollbackReverted(label.wasRollbackReverted());
		revision.setUndoRestoreReverted(label.wasUndoRestoreReverted());
		revision.setRevisionGroupId(label.getGroupId());

		numberOfRevisions++;

		if (label.wasRollbackReverted()) {
			numberOfRollbackedRevisions++;

			if (revision.hasRegisteredContributor()) {
				numberOfRegisteredRollbackedRevisions++;
			}
		}

		if (revision.hasRegisteredContributor()) {
			numberOfRegisteredRevisions++;
		}

		processor.processRevision(revision);
	}

	@Override
	public void finishRevisionProcessing() {
		processor.finishRevisionProcessing();
		
		logger.info("Final result:");
		printStatistics();
		labelReader.finishReading();
	}

	private void printStatistics() {
		logger.info("   Number of revisions: " + numberOfRevisions);
		logger.info("   Number of rollbacked revisions: "
				+ numberOfRollbackedRevisions);
		logger.info("   Number of unregistered, rollbacked revisions: "
				+ (numberOfRollbackedRevisions - numberOfRegisteredRollbackedRevisions));
		logger.info("   Number of registered, rollbacked revisions: "
				+ numberOfRegisteredRollbackedRevisions);
		logger.info("   Number of unregistered revisions: "
				+ (numberOfRevisions - numberOfRegisteredRevisions));
		logger.info("   Number of registered revisions: "
				+ numberOfRegisteredRevisions);
	}
}
