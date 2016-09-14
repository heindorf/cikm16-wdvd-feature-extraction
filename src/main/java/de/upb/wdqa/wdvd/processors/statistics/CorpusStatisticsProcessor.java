package de.upb.wdqa.wdvd.processors.statistics;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.ItemStore;
import de.upb.wdqa.wdvd.db.interfaces.DbItem;
import de.upb.wdqa.wdvd.features.revision.misc.CONTENT_TYPE;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class CorpusStatisticsProcessor implements RevisionProcessor {
	final Logger logger;
	
	private long numberOfRevisions = 0;
	private long numberOfRollbackRevertedRevisions = 0;
	
	private long revertedRegisteredRevisionGroups;
	private long revertedUnregisteredRevisionGroups;
	private long nonRevertedRegisteredRevisionGroups;
	private long nonRevertedUnregisteredRevisionGroups;
	
	private long revertedRegisteredRevisions;
	private long revertedUnregisteredRevisions;
	private long nonRevertedRegisteredRevisions;
	private long nonRevertedUnregisteredRevisions;
	
	private Frequency topVandalizedItems = new Frequency();
	private Frequency topNonVandalizedItems = new Frequency();
	
	private Frequency topRevertedRegisteredUsers = new Frequency();
	private Frequency topRevertedUnregisteredUsers = new Frequency();
	
	private DescriptiveStatistics nonRevertedRevisionGroupLengthDistribution = new DescriptiveStatistics();
	private DescriptiveStatistics revertedRevisionGroupLengthDistribution = new DescriptiveStatistics();
	
	private ContentTypeFrequency contentTypeOfRollbackRevertedRevisionGroups = new ContentTypeFrequency();
	private ContentTypeFrequency contentTypeOfUnregisteredRollbackRevertedRevisionGroups = new ContentTypeFrequency();
	private ContentTypeFrequency contentTypeOfRegisteredRollbackRevertedRevisionGroups = new ContentTypeFrequency();
	private ContentTypeFrequency contentTypeOfNonRollbackRevertedRevisionGroups = new ContentTypeFrequency();
	
	private ContentTypeFrequency contentTypeOfRollbackRevertedRevisions = new ContentTypeFrequency();
	private ContentTypeFrequency contentTypeOfUnregisteredRollbackRevertedRevisions = new ContentTypeFrequency();
	private ContentTypeFrequency contentTypeOfRegisteredRollbackRevertedRevisions = new ContentTypeFrequency();
	private ContentTypeFrequency contentTypeOfNonRollbackRevertedRevisions = new ContentTypeFrequency();
	
	private long lastRevisionGroupId = -1;
	
	List<Revision> currentRevisionGroup = new ArrayList<>();
	
	ItemStore itemStore;
	
	RevisionProcessor processor;
	
	public CorpusStatisticsProcessor(RevisionProcessor processor, String processorName, ItemStore itemStore) {
		this.processor = processor;
		logger = LoggerFactory.getLogger(CorpusStatisticsProcessor.class + " (" + processorName + ")");
		this.itemStore = itemStore;
	}
	
	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		if (processor != null){
			processor.startRevisionProcessing();
		}
		
		

	}

	@Override
	public void processRevision(Revision revision) {
		if(processor != null){
			processor.processRevision(revision);
		}
		
		CONTENT_TYPE contentType = revision.getContentType();
		
		numberOfRevisions++;
		
		if(revision.wasRollbackReverted()){
			numberOfRollbackRevertedRevisions++;
			contentTypeOfRollbackRevertedRevisions.inc(contentType);
			if(revision.hasRegisteredContributor()){
				revertedRegisteredRevisions++;
				contentTypeOfRegisteredRollbackRevertedRevisions.inc(contentType);
			}
			else{
				revertedUnregisteredRevisions++;
				contentTypeOfUnregisteredRollbackRevertedRevisions.inc(contentType);
			}
		}
		else{
			contentTypeOfNonRollbackRevertedRevisions.inc(contentType);
			
			if (revision.hasRegisteredContributor()){
				nonRevertedRegisteredRevisions++;
			}
			else{
				nonRevertedUnregisteredRevisions++;				
			}
		}		
		
		
		processRevisionGroup(revision);
		
	}
	


	@Override
	public void finishRevisionProcessing() {
		if(processor != null){
			processor.finishRevisionProcessing();
		}
		
		processRevisionGroup2();
		
		logResults();
	}
	
	private void logResults() {
		logger.info("Number of revisions: " + numberOfRevisions);
		logger.info("Number of non-vandalized revisions: " + (numberOfRevisions - numberOfRollbackRevertedRevisions));
		logger.info("Number of vandalized revisions: " + numberOfRollbackRevertedRevisions);
		logger.info("");
		logger.info("Number of revision groups: " + (nonRevertedRegisteredRevisionGroups + nonRevertedUnregisteredRevisionGroups
				+ revertedRegisteredRevisionGroups + revertedUnregisteredRevisionGroups));
		logger.info("Number of non-vandalized revision groups: " + (nonRevertedRegisteredRevisionGroups + nonRevertedUnregisteredRevisionGroups));
		logger.info("Number of vandalized revision groups: " + (revertedRegisteredRevisionGroups + revertedUnregisteredRevisionGroups));
		logger.info("");
		logger.info("Top non-vandalized items: \n" + formatTopItems(topNonVandalizedItems, 10));
		logger.info("Top vandalized items: \n" + formatTopItems(topVandalizedItems, 10));
		logger.info("Number of items vandalized exactly once: " + numberOfItemsVandalizedExactlyOnce());
		logger.info("");
		logger.info("Number of vandalism revision groups by unregistered users: " + revertedUnregisteredRevisionGroups);
		logger.info("Number of vandalism revision groups by registered users: " + revertedRegisteredRevisionGroups);
		logger.info("Number of non-vandalism revision groups by unregistered users: " + nonRevertedUnregisteredRevisionGroups);
		logger.info("Number of non-vandalism revision groups by registered users: " + nonRevertedRegisteredRevisionGroups);
		logger.info("Number of unique unregistered users that have done vandalism: " + topRevertedUnregisteredUsers.getUniqueCount());
		logger.info("Number of unique registered users that have done vandalism: " + topRevertedRegisteredUsers.getUniqueCount());
		logger.info("Length of reverted revision groups: \n" + revertedRevisionGroupLengthDistribution);
		logger.info("Length of non-reverted revision groups: \n" + nonRevertedRevisionGroupLengthDistribution);
		
		logger.info("Number of vandalism revisions by unregistered users: " + revertedUnregisteredRevisions);
		logger.info("Number of vandalism revisions by registered users: " + revertedRegisteredRevisions);
		logger.info("Number of non-vandalism revisions by unregistered users: " + nonRevertedUnregisteredRevisions);
		logger.info("Number of non-vandalism revisions by registered users: " + nonRevertedRegisteredRevisions);
		
		logger.info("rollback reverted revision groups: " + contentTypeOfRollbackRevertedRevisionGroups);
		logger.info("rollback reverted, unregistered revision groups: " + contentTypeOfUnregisteredRollbackRevertedRevisionGroups);
		logger.info("rollback reverted, registered revision groups: " + contentTypeOfRegisteredRollbackRevertedRevisionGroups);		
		logger.info("non-rollback reverted revision groups: " + contentTypeOfNonRollbackRevertedRevisionGroups);	
		
		logger.info("rollback reverted revisions: " + contentTypeOfRollbackRevertedRevisions);
		logger.info("rollback reverted, unregistered revisions: " + contentTypeOfUnregisteredRollbackRevertedRevisions);
		logger.info("rollback reverted, registered revisions: " + contentTypeOfRegisteredRollbackRevertedRevisions);
		logger.info("non-rollback reverted revisions:" + contentTypeOfNonRollbackRevertedRevisions);
	}
	
	private void processRevisionGroup(Revision revision){
		if (lastRevisionGroupId != revision.getRevisionGroupId()){
			processRevisionGroup2();
			currentRevisionGroup.clear();
		}
		currentRevisionGroup.add(revision);
		lastRevisionGroupId = revision.getRevisionGroupId();
	}

	private void processRevisionGroup2() {
		if(currentRevisionGroup.size() > 0){
			Revision firstRevision = currentRevisionGroup.get(0);
			CONTENT_TYPE contentType = getContentTypeOfRevisionGroup();
			
			// If any revision of a revision group was reverted,
			// then the first revision of this group was reverted as well.
			if(firstRevision.wasRollbackReverted()){
				revertedRevisionGroupLengthDistribution.addValue(currentRevisionGroup.size());			

				topVandalizedItems.addValue(firstRevision.getItemId());
				contentTypeOfRollbackRevertedRevisionGroups.inc(contentType);
				
				if(firstRevision.hasRegisteredContributor()){
					revertedRegisteredRevisionGroups++;
					topRevertedRegisteredUsers.addValue(firstRevision.getContributor());
					contentTypeOfRegisteredRollbackRevertedRevisionGroups.inc(contentType);
				}
				else{
					revertedUnregisteredRevisionGroups++;
					topRevertedUnregisteredUsers.addValue(firstRevision.getContributor());
				}
				revertedRevisionGroupLengthDistribution.addValue(currentRevisionGroup.size());
				contentTypeOfUnregisteredRollbackRevertedRevisionGroups.inc(contentType);
			}
			else{
				topNonVandalizedItems.addValue(firstRevision.getItemId());
				contentTypeOfNonRollbackRevertedRevisionGroups.inc(contentType);
				
				if(firstRevision.hasRegisteredContributor()){
					nonRevertedRegisteredRevisionGroups++;
				}
				else{
					nonRevertedUnregisteredRevisionGroups++;
				}
				nonRevertedRevisionGroupLengthDistribution.addValue(currentRevisionGroup.size());
			}
		}		
	}
	
	
	public CONTENT_TYPE getContentTypeOfRevisionGroup(){
		Revision revision0 = currentRevisionGroup.get(0);
		CONTENT_TYPE result = revision0.getContentType();
		
		for(int i=1; i < currentRevisionGroup.size(); i++){
			Revision revision= currentRevisionGroup.get(i);
			result = result.combine(revision.getContentType());
		}			
		
		return result;
	}
	
	private int numberOfItemsVandalizedExactlyOnce(){
		int result = 0;
		
		Iterator<Entry<Comparable<?>, Long>> iterator = topVandalizedItems.entrySetIterator();
			
		while(iterator.hasNext()){
			Entry<Comparable<?>, Long> entry = iterator.next();
			
			if (entry.getValue() == 1){
				result++;
			}
		}
		return result;
	}
	
	public String formatTopItems(Frequency frequency, int maxCount){
		itemStore.flushItems();
		
		List<Map.Entry<Comparable<?>, Long>> list = FrequencyUtils.sortByFrequency(frequency);		
		String result = "";
		
		for(int i = 0; i < maxCount && i < list.size(); i++){
//			String percent = String.format(Locale.US, "%.0f", frequency.getPct(list.get(i).getKey()) * 100) + "%";
			
			int itemId = (int)(long)((Long) list.get(i).getKey());
			int count = (int)(long)((Long) list.get(i).getValue());
			String label = null;
			Integer instanceOfId = null;
			String instanceOfLabel = null;

			DbItem item = itemStore.getItem(itemId);
			
			if(item != null){
				label = item.getLabel();
				instanceOfId = item.getInstanceOfId();
				if (instanceOfId != null){
					DbItem instanceOfItem = itemStore.getItem(instanceOfId);
					if (instanceOfItem != null){
						instanceOfLabel = instanceOfItem.getLabel();
					}
				}
			}
			
			result += itemId + "," + count + "," + label + "," + instanceOfId + "," + instanceOfLabel + "\n";
		}		
		
		return result;		
	}
}


class ContentTypeFrequency extends EnumMap<CONTENT_TYPE, Integer> {
	private static final long serialVersionUID = 1L;

	public ContentTypeFrequency() {
		super(CONTENT_TYPE.class);
		
		for (CONTENT_TYPE ct: CONTENT_TYPE.values()){
			put(ct, 0);
		}		
	}
	
	public void inc(CONTENT_TYPE contentType){
		put(contentType, get(contentType) + 1);		
	}
}
