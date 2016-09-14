package de.upb.wdqa.wdvd.processors.decorators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummaryValues;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.processors.RevisionProcessor;
import de.upb.wdqa.wdvd.processors.controlflow.Reducer;

public class JsonProcessorReducer implements Reducer {
	
	static final Logger logger = LoggerFactory.getLogger(JsonProcessorReducer.class);

	@Override
	public void reduce(List<RevisionProcessor> workerProcessors) {
		List<SummaryStatistics> emptyStatisticsList = new ArrayList<SummaryStatistics>();
		List<SummaryStatistics> inconsistentStatisticsList = new ArrayList<SummaryStatistics>();
		List<SummaryStatistics> jsonExceptionStatisticsList = new ArrayList<SummaryStatistics>();
		List<SummaryStatistics> nullPointerExceptionStatisticsList = new ArrayList<SummaryStatistics>();
		
		List<SummaryStatistics> newJsonStatisticsList = new ArrayList<SummaryStatistics>();
		List<SummaryStatistics> oldJsonStatisticsList = new ArrayList<SummaryStatistics>();
		List<SummaryStatistics> redirectStatisticsList = new ArrayList<SummaryStatistics>();
		
		for(RevisionProcessor processor: workerProcessors){
			JsonProcessor jsonProcessor = (JsonProcessor) processor;
			
			emptyStatisticsList.add(jsonProcessor.getEmptyJsonStatistics());
			inconsistentStatisticsList.add(jsonProcessor.getInconsistentJsonXMLStatistics());
			jsonExceptionStatisticsList.add(jsonProcessor.getJsonExceptionStatistics());
			nullPointerExceptionStatisticsList.add(jsonProcessor.getNullPointerExceptionStatistics());
			
			newJsonStatisticsList.add(jsonProcessor.getNewJsonStatistics());
			oldJsonStatisticsList.add(jsonProcessor.getOldJsonStatistics());
			redirectStatisticsList.add(jsonProcessor.getRedirectStatistics());
			
		}
		
		StatisticalSummaryValues emptyStatistics = AggregateSummaryStatistics.aggregate(emptyStatisticsList);
		StatisticalSummaryValues inconsistentStatistics = AggregateSummaryStatistics.aggregate(inconsistentStatisticsList);
		StatisticalSummaryValues jsonExceptionStatistics = AggregateSummaryStatistics.aggregate(jsonExceptionStatisticsList);
		StatisticalSummaryValues nullPointerExceptionStatistics = AggregateSummaryStatistics.aggregate(nullPointerExceptionStatisticsList);
		
		StatisticalSummaryValues newJsonStatistics = AggregateSummaryStatistics.aggregate(newJsonStatisticsList);
		StatisticalSummaryValues oldJsonStatistics = AggregateSummaryStatistics.aggregate(oldJsonStatisticsList);
		StatisticalSummaryValues redirectStatistics = AggregateSummaryStatistics.aggregate(redirectStatisticsList);
		
		logger.info("Number of revisions with empty 'text' element: " + emptyStatistics.getN());
		logger.info("Number of revisions with inconsistent JSON/XML item id: " + inconsistentStatistics.getN());		
		logger.info("Number of revisions whose 'text' element could not be parsed: " + jsonExceptionStatistics.getN());
		logger.info("Number of revisions which threw a NullPointerException: " + nullPointerExceptionStatistics.getN());
		long totalNumberOfFailures = emptyStatistics.getN() + inconsistentStatistics.getN() + jsonExceptionStatistics.getN() + nullPointerExceptionStatistics.getN();
		logger.info("Total number of revisions whose item document was discarded: " + totalNumberOfFailures);
		
		logger.info("Number of revisions in new JSON format: " + newJsonStatistics.getN());
		logger.info("Number of revisions in old JSON format: " + oldJsonStatistics.getN());
		logger.info("Number of revisions which represent a redirect: " + redirectStatistics.getN());
		long totalNumberOfSuccesses = newJsonStatistics.getN() + oldJsonStatistics.getN() + redirectStatistics.getN();
		logger.info("Total number of revisions whose item document was successfully parsed: " + totalNumberOfSuccesses);

	}

}
