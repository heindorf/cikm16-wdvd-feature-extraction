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
