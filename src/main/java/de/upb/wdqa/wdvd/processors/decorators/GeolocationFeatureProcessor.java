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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureValue;
import de.upb.wdqa.wdvd.geolocation.GeoInformation;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class GeolocationFeatureProcessor implements RevisionProcessor {
	
	static final Logger logger =
			LoggerFactory.getLogger(GeolocationFeatureProcessor.class);
	
	RevisionProcessor processor;
	File geolocationFeatureFile;
	
	CSVParser csvParser;
	Iterator<CSVRecord> iterator;
	
	public GeolocationFeatureProcessor(
			RevisionProcessor processor, File geolocationFeatureFile){
		this.processor = processor;
		this.geolocationFeatureFile = geolocationFeatureFile;
	}

	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		try {
			BufferedReader csvReader;

			csvReader = new BufferedReader(
			new InputStreamReader(
			new BZip2CompressorInputStream(
			new BufferedInputStream(
			new FileInputStream(geolocationFeatureFile))), "UTF-8"));

			csvParser = new CSVParser(csvReader,
					CSVFormat.RFC4180.withHeader());
			iterator = csvParser.iterator();
		
			processor.startRevisionProcessing();
		
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@Override
	public void processRevision(Revision revision) {
		CSVRecord csvRevision = iterator.next();
		
		GeoInformation geoInformation = 
				getGeoInformationForRevision(revision.getRevisionId(), csvRevision);		
		
			
		revision.setGeoInformation(geoInformation);		

		
		processor.processRevision(revision);
	}

	@Override
	public void finishRevisionProcessing() {
		processor.finishRevisionProcessing();
		
		try {
			csvParser.close();
		} catch (IOException e) {
			logger.error("", e);
		}
		
		logger.debug("Finished.");		
	}
	
	private GeoInformation getGeoInformationForRevision(
			long revisionId, CSVRecord csvRevision){		
		
		long csvRevisionId = Long.parseLong(csvRevision.get("revisionId"));
		
		if (revisionId != csvRevisionId){
			logger.error("geolocation feature file is out of sync");
		}
		
		// not contained in feature file (only in geolocation database)
		long startAddress = -1;
		long endAddress = -1;
		
		String userCountry       = getValue(csvRevision, "userCountry");
		String userContinentCode = getValue(csvRevision, "userContinent");
		String userTimeZone      = getValue(csvRevision, "userTimeZone");
		String userRegionCode    = getValue(csvRevision, "userRegion");
		String userCityName      = getValue(csvRevision, "userCity");
		String userCountyName    = getValue(csvRevision, "userCounty");
		
		GeoInformation result;
		
		if (userCountry != null ||
			userContinentCode != null ||
			userTimeZone != null ||
			userRegionCode != null ||
			userCityName != null ||
			userCountyName != null){
				result = new GeoInformation(
							startAddress,
							endAddress,
							userCountry,
							userContinentCode,
							userTimeZone,
							userRegionCode,
							userCityName,
							userCountyName);
		}
		else{
			result = null;
		}
		
		return result;
	}
	
	private String getValue(CSVRecord record, String key){
		String value = record.get(key);
		
		if (value.equals(FeatureValue.MISSING_VALUE_STRING)){
			value = null;
		}
		
		return value;		
	}	
}
