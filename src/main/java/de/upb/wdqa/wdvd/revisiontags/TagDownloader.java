package de.upb.wdqa.wdvd.revisiontags;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.stat.Frequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.db.implementation.DbRevisionImpl;
import de.upb.wdqa.wdvd.db.implementation.DbTagFactory;
import de.upb.wdqa.wdvd.db.interfaces.DbRevision;
import de.upb.wdqa.wdvd.db.interfaces.DbTag;
import de.upb.wdqa.wdvd.processors.statistics.FrequencyUtils;

/**
 * Processes the data of the TagDownloader and stores them in memory.
 *
 */
public class TagDownloader {
	
	static final Logger logger = LoggerFactory.getLogger(TagDownloader.class);
	
	static DbTagFactory tagFactory = new DbTagFactory();
	
	static long MAX_REVISION_ID = 180000000L;
	
	static TagDownloaderDataStore dataStore = new TagDownloaderMemoryDataStore(tagFactory, MAX_REVISION_ID);

	
	static Frequency tagDistribution = new Frequency();
	
	
	/**
	 * Reads the csv file of the TagDownloader
	 */
	public static void readFile(File file) {
		try {
			logger.info("Starting to read file of TagDownloader ...");
			BufferedReader reader = new BufferedReader(new InputStreamReader(new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(file))), "UTF-8"));
			
			CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180);
			
			dataStore.connect();		
			
			
			for (CSVRecord csvRecord: parser) {
				parseRecord(csvRecord);
				if(csvRecord.getRecordNumber() % 1000000 == 0){
					logger.info("Current Record: " + csvRecord.getRecordNumber());
				}
			}
			
			dataStore.disconnect();
			parser.close();
			logger.info("Tag Distribution:\n" + FrequencyUtils.formatFrequency(tagDistribution));
			logger.info("Finished");
		} catch (Exception e) {
			logger.error("", e);
		}		
	}
	
	/**
	 * Reads one line of the csv file of the TagDownloader
	 */
	private static void parseRecord(CSVRecord record){
		long revisionId = Long.parseLong(record.get(0)); // revision id
		String sha1Base16 = record.get(1); // sha1
		
		//record.get(2); // size
		
		Set<DbTag> tags = new HashSet<DbTag>();
		for (int i = 3; i < record.size(); i++){
			String tagName = record.get(i);
			tags.add(tagFactory.getTag(tagName));
			tagDistribution.addValue(tagName);
		}
		
		try{
			dataStore.putRevision(new DbRevisionImpl(revisionId, SHA1Converter.base16to36(sha1Base16), tags));
		}
		catch(Exception e){
			logger.error("", e);
		}
	}
	
	public static DbRevision getRevision(long revisionId){		
		DbRevision result = null;
		
		try{
			result = dataStore.getRevision(revisionId);
		}
		catch(Exception e){
			logger.error("", e);
		}
		
		return result;
	}
	
	public static List<DbRevision> getRevisions(List<Long> revisionIds){
		List<DbRevision> result = null;
		
		try{
			result = dataStore.getRevisions(revisionIds);
		}
		catch(Exception e){
			logger.error("", e);
		}
		
		return result;
	}
}


