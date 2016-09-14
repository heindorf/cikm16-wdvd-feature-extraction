package de.upb.wdqa.wdvd.processors.statistics;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.math3.stat.Frequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class ActionStatisticsProcessor implements RevisionProcessor {
	final static Logger logger = LoggerFactory.getLogger(ActionStatisticsProcessor.class);	
	
	private Frequency actionDistribution = new Frequency(); // Corpus Creation Statistics
	private Frequency rollbackRevertedActionDistribution = new Frequency();
	private Frequency nonRollbackRevertedActionDistribution = new Frequency();
	
	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
	{
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	// Month-Year, action, count
	HashMap<String, HashMap<String, Integer>> monthlyActionDistribution= new HashMap<String, HashMap<String, Integer>>();

	RevisionProcessor processor;
	String path;
	
	public ActionStatisticsProcessor(RevisionProcessor processor, String path) {
		this.processor = processor;
		this.path = path;
	}

	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		processor.startRevisionProcessing();		
	}

	@Override
	public void processRevision(Revision revision) {		
		String action1 = "" + revision.getParsedComment().getAction1();
		// getParsedComment() is called before processRevision such that it can
		// be cached (revision is cloned in the next RevisionProcessor)
		processor.processRevision(revision);
		
		actionDistribution.addValue(action1);
		if(revision.wasRollbackReverted()){
			rollbackRevertedActionDistribution.addValue(action1);
		}
		else{
			nonRollbackRevertedActionDistribution.addValue(action1);
		}
		
		
		processMonthlyActionDistribution(revision);
	}



	private void processMonthlyActionDistribution(Revision revision) {
		String action1 = "" + revision.getParsedComment().getAction1();
		
		String timeString = format.format(revision.getDate()); 
		
		if(!monthlyActionDistribution.containsKey(timeString)){
			monthlyActionDistribution.put(timeString, new HashMap<String, Integer>());
		}
		
		if(!monthlyActionDistribution.get(timeString).containsKey(action1)){
			monthlyActionDistribution.get(timeString).put(action1, 0);
		}
		
		monthlyActionDistribution.get(timeString).put(action1, monthlyActionDistribution.get(timeString).get(action1) + 1);
	}

	@Override
	public void finishRevisionProcessing() {
		processor.finishRevisionProcessing();
		
		logResults();		
	}
	
	private void logResults() {
		logger.info("Action frequency distribution:\n" + FrequencyUtils.formatFrequency(actionDistribution));
		logger.info("Action frequency distribution of rollback-reverted revisions:\n" + FrequencyUtils.formatFrequency(rollbackRevertedActionDistribution));
		logger.info("Action frequency distribution of non-rollback-reverted revisions:\n" + FrequencyUtils.formatFrequency(nonRollbackRevertedActionDistribution));
		
		try {
			Writer writer = new PrintWriter(path, "UTF-8");
			CSVPrinter csvWriter = CSVFormat.RFC4180.withQuoteMode(QuoteMode.ALL).withHeader("month", "action", "count").print(writer);
			
			for(Entry<String, HashMap<String, Integer>> entry: getSortedList(monthlyActionDistribution)){
				String month = entry.getKey();
				
				for(Entry<String, Integer> entry2: getSortedList2(entry.getValue())){
					String action = entry2.getKey();				
					Integer value = entry2.getValue();					
	
					csvWriter.printRecord(month, action, value);					
				}
			}
			csvWriter.close();
		} catch (IOException e) {
			logger.error("", e);
		}		
	}
	
	private static List<Map.Entry<String, HashMap<String, Integer>>> getSortedList(
			HashMap<String, HashMap<String, Integer>> map) {

		List<Map.Entry<String, HashMap<String, Integer>>> entries = new ArrayList<Map.Entry<String, HashMap<String, Integer>>>(
				map.entrySet());

		Collections.sort(entries,
				new Comparator<Map.Entry<String, HashMap<String, Integer>>>() {
					public int compare(
							Map.Entry<String, HashMap<String, Integer>> a,
							Map.Entry<String, HashMap<String, Integer>> b) {
						return a.getKey().compareTo(b.getKey());
					}
				});
		return entries;
	}
	
	private static List<Map.Entry<String, Integer>> getSortedList2(
			HashMap<String, Integer> map) {
		List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(
				map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> a,
					Map.Entry<String, Integer> b) {
				return a.getKey().compareTo(b.getKey());
			}
		});
		return entries;
	}
}
