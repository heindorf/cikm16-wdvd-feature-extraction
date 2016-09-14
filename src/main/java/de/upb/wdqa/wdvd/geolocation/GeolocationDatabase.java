package de.upb.wdqa.wdvd.geolocation;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes the data of the Geolocation Database and stores them in memory.
 *
 */
public class GeolocationDatabase {
	
	static final Logger logger = LoggerFactory.getLogger(GeolocationDatabase.class);
	
	static TreeMap<Long, GeoInformation> treeMap = new TreeMap<Long, GeoInformation>();
	
	/**
	 * Reads the csv file of the TagDownloader
	 */
	public static void readFile(File file) {
		try {
			logger.info("Starting to read file of GeolocationDatabase ...");
			BufferedReader reader = new BufferedReader(new InputStreamReader(new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(file))), "UTF-8"));
			
			CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180);
			
			
			for (CSVRecord csvRecord: parser) {
				parseRecord(csvRecord);
				if(csvRecord.getRecordNumber() % 1000000 == 0){
					logger.info("Current Record: " + csvRecord.getRecordNumber());
				}
			}
			
			parser.close();
			logger.info("Finished");
		} catch (Exception e) {
			logger.error("", e);
		}		
	}
	
	/**
	 * Reads one line of the csv file of the Geo Database
	 */
	private static void parseRecord(CSVRecord record){
		long startAdress = Long.parseLong(record.get(0));
		long endAdress = Long.parseLong(record.get(1));
		String countryCode = record.get(2);
		//String countryName = record.get(3);
		String continentCode = record.get(4);
		//String continentName = record.get(5);
		String timeZone = record.get(6);
		String regionCode = record.get(7);
		//String regionName = record.get(8);
		//String owner = record.get(9);
		String cityName = record.get(10);
		String countyName = record.get(11);
		//String latitude = record.get(12);
		//String longitude = record.get(13);
		
		
		GeoInformation geoInformation = new GeoInformation(startAdress, endAdress, countryCode, continentCode,
				timeZone, regionCode, cityName, countyName);
		
		treeMap.put(geoInformation.getStartAdress(), geoInformation);
	}
	
	public static GeoInformation getGeoInformation(String str){
		GeoInformation result = null;
		
		try {
			InetAddress adress = InetAddress.getByName(str);
			byte[] bytes = adress.getAddress();
			ArrayUtils.reverse(bytes);
			
			BigInteger bigInt = new BigInteger(1, adress.getAddress());
			
			long longAdress = bigInt.longValue();
			
			result = getGeoInformation(longAdress);			
			
		} catch (UnknownHostException e) {
			logger.error("IP Adress not known: " + str);
		}
		
		return result;		
	}
	
	private static GeoInformation getGeoInformation(long adress){
		GeoInformation result = null;
		
		Entry<Long,GeoInformation> entry = treeMap.floorEntry(adress);
		
		if (entry != null){
			GeoInformation geo = entry.getValue();		
			
			if (adress >= geo.getStartAdress() && adress <= geo.getEndAdress()){
				result = geo;
			}
		}
		
		return result;		
	}
	
}


