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

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.geolocation.GeoInformation;
import de.upb.wdqa.wdvd.geolocation.GeolocationDatabase;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class GeolocationDbProcessor implements RevisionProcessor {
	
	static final Logger logger =
			LoggerFactory.getLogger(GeolocationDbProcessor.class);
	
	RevisionProcessor processor;
	File geolocationFile;
	
	public GeolocationDbProcessor(
			RevisionProcessor processor, File geolocationFile) {
		this.processor = processor;
		this.geolocationFile = geolocationFile;
	}

	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		GeolocationDatabase.readFile(this.geolocationFile);
		
		
		processor.startRevisionProcessing();		
	}

	@Override
	public void processRevision(Revision revision) {
		if (!revision.hasRegisteredContributor()) {
			String userName = revision.getContributor();
			
			GeoInformation geoInformation =
					GeolocationDatabase.getGeoInformation(userName);
			
			revision.setGeoInformation(geoInformation);		
		}
		
		processor.processRevision(revision);
	}

	@Override
	public void finishRevisionProcessing() {
		processor.finishRevisionProcessing();
		
		logger.debug("Finished.");		
	}

}
