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

package de.upb.wdqa.wdvd.processors.output;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.*;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class CsvFeatureWriter implements RevisionProcessor {
	CSVPrinter csvPrinter;
	File featureFile;
	
	static final Logger logger = LoggerFactory.getLogger(CsvFeatureWriter.class);
	
	final int BUFFER_SIZE = 8 * 1024 * 1024;
	final int BZIP2_BLOCKSIZE = BZip2CompressorOutputStream.MIN_BLOCKSIZE;
	
	private List<Feature> features;
	
	public CsvFeatureWriter(File featureFile, List<Feature> features) {
		super();
		this.featureFile = featureFile;
		this.features = features;
	}

	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting (" + featureFile + ")...");
		
		try {
			OutputStreamWriter writer =
					new OutputStreamWriter(
				    getPipedOutputStreamStream(
					new BZip2CompressorOutputStream(
					new BufferedOutputStream(
					new FileOutputStream(featureFile)),
					BZIP2_BLOCKSIZE)),
				    "utf-8");
	
			String[] header = new String[features.size()];
			
			for (int i = 0; i < features.size(); i++) {
				header[i] = features.get(i).getName();
			}			

			
			csvPrinter = CSVFormat.RFC4180.withHeader(header).print(writer);
			
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public void processRevision(Revision revision) {
		try {
			List<String> record = new ArrayList<String>(features.size());
			
			for (int i = 0; i < features.size(); i++) {
				Feature feature = features.get(i);
				FeatureValue featureValue = revision.getFeatureValues().get(feature);
				String writeString;
				
				if (featureValue == null) {
					writeString = FeatureValue.MISSING_VALUE_STRING;
					logger.error("Feature returned null (Revision "
							+ revision.getRevisionId()
							+ ", Feature "
							+ feature.getName() + ")");
				} else {
					writeString = featureValue.toString();
				}
				
				record.add(writeString);
			}			

			csvPrinter.printRecord(record);
		} catch (IOException e) {
			logger.error("", e);
		}	
		
	}
	

	@Override
	public void finishRevisionProcessing() {
		try {
			csvPrinter.close();
		} catch (IOException e) {
			logger.error("", e);
		}	
	}
	
	private static OutputStream getPipedOutputStreamStream(
			final OutputStream outputStream) throws IOException {
		final int BUFFER_SIZE = 1 * 1024 * 1024;
		
		final PipedOutputStream pipedOutputStream = new PipedOutputStream();
		final PipedInputStream pipedInputStream =
				new PipedInputStream(pipedOutputStream, BUFFER_SIZE);
		
		new Thread("Label Writer Output Stream") {
			@Override
			public void run() {
				try {
					IOUtils.copy(pipedInputStream, outputStream);
					
					pipedInputStream.close();
					outputStream.close();
				} catch (Throwable t) {
					logger.error("", t);
				}
			}
		}.start();
		
		return pipedOutputStream;
	}

}
