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
