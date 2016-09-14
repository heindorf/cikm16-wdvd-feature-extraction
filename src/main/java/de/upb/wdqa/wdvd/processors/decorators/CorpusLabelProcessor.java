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
