package de.upb.wdqa.wdvd.processors.statistics;

import java.util.BitSet;
import java.util.Map;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.dumpfiles.MwRevision;
import org.wikidata.wdtk.dumpfiles.MwRevisionProcessor;

import de.upb.wdqa.wdvd.Revision;

public class RawDumpStatisticsProcessor implements MwRevisionProcessor {
	MwRevisionProcessor processor;
	
	static final Logger logger = LoggerFactory.getLogger(RawDumpStatisticsProcessor.class);
	
	long numberOfRevisions = 0;
	
	Frequency namespaceDistribution = new Frequency();
	Frequency formatDistribution = new Frequency();
	Frequency modelDistribution = new Frequency();
	SummaryStatistics revisionIdStatistics = new SummaryStatistics();
	BitSet seenRevisionIDs = new BitSet();
	BitSet seenItemIDs = new BitSet();
	BitSet seenPageIDs = new BitSet();
	
	public RawDumpStatisticsProcessor(MwRevisionProcessor processor) {
		super();
		this.processor = processor;
	}

	@Override
	public void startRevisionProcessing(String siteName, String baseUrl,
			Map<Integer, String> namespaces) {
		logger.debug("Starting...");
		processor.startRevisionProcessing(siteName, baseUrl, namespaces);
		
	}

	@Override
	public void processRevision(final MwRevision mwRevision) {
		try{
			numberOfRevisions++;
			namespaceDistribution.addValue(mwRevision.getNamespace());
			formatDistribution.addValue(mwRevision.getFormat());		
			modelDistribution.addValue(mwRevision.getModel());
			revisionIdStatistics.addValue(mwRevision.getRevisionId());
			
			if(seenRevisionIDs.get((int) mwRevision.getRevisionId())){
				logger.warn("Duplicate Revision ID: " + mwRevision.getRevisionId());
			}			
			seenRevisionIDs.set((int) mwRevision.getRevisionId());
			int itemId = Revision.getItemIdFromString(mwRevision.getPrefixedTitle());
			seenItemIDs.set(itemId<0?0:itemId);
			seenPageIDs.set((int) mwRevision.getPageId());
			
//			Date currentDate = SimpleDateFormat
		}
		catch(Exception e){
			logger.error("", e);
		}
		processor.processRevision(mwRevision);
		
///////////////////////////////////
//		logResults();
	}

	@Override
	public void finishRevisionProcessing() {
		processor.finishRevisionProcessing();		
		logResults();
	}
	
	private void logResults(){
		logger.info("Total number of revisions in the dump including all namespaces: " + numberOfRevisions);
		
		logger.info("Frequency distribution of namespaces:\n" + namespaceDistribution.toString());
		logger.info("Frequency distribution of formats:\n" + formatDistribution.toString());
		logger.info("Frequency distribution of models:\n" + modelDistribution.toString());
		logger.info("RevisionId statistics: Min: " +
				(int)revisionIdStatistics.getMin() + ", Max: " + (int)revisionIdStatistics.getMax() + ", Mean: " + revisionIdStatistics.getMean());
		
		logger.info("finished!");
	}	
}
