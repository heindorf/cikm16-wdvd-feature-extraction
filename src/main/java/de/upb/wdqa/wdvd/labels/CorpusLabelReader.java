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

package de.upb.wdqa.wdvd.labels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class reads the labels of the corpus from a bzip2-compressed CSV file.
 *
 */
public class CorpusLabelReader {
	static final Logger logger = LoggerFactory
			.getLogger(CorpusLabelReader.class);

	InputStream labelsStream;

	CSVParser csvParser;
	Iterator<CSVRecord> iterator;
	
	final int BUFFER_SIZE = 256 * 1024;

	private static final String[] FILE_HEADER = { "revision_id",
		"revision_group_id", "rollback_reverted", "undo_restore_reverted" };

	public CorpusLabelReader(InputStream labelsStream) {
		this.labelsStream = labelsStream;
	}

	/**
	 *	Initializes the label reader.
	 */
	public void startReading() {
		try {
			BufferedReader csvReader = new BufferedReader(
					new InputStreamReader(labelsStream, "UTF-8"), BUFFER_SIZE);

			csvParser = new CSVParser(csvReader,
					CSVFormat.RFC4180.withHeader(FILE_HEADER));
			iterator = csvParser.iterator();

			CSVRecord headerRecord = iterator.next();

			for (int i = 0; i < FILE_HEADER.length; i++) {
				if (!FILE_HEADER[i].equals(headerRecord.get(i))) {
					throw new IOException(
							"The header of the CSV file is wrong.");
				}
			}
		} catch (IOException e) {
			logger.error("", e);
			finishReading();
		}
	}

	/**
	 * Returns the next label in the label file.
	 * 
	 * @return the next label in the label file.
	 */
	public CorpusLabel getNextLabel() {
		CorpusLabel label = null;

		if (iterator.hasNext()) {
			CSVRecord csvRecord = iterator.next();

			label = new CorpusLabel();

			label.setRevisionId(Long.parseLong(csvRecord.get(0)));
			label.setGroupId(Long.parseLong(csvRecord.get(1)));
			label.setRollbackReverted(Boolean.parseBoolean(csvRecord.get(2)));
			label.setUndoRestoreReverted(Boolean.parseBoolean(csvRecord.get(3)));
		}

		return label;
	}

	/**
	 * Performs final actions after all labels have been read. For example, it closes open resources.
	 */
	public void finishReading() {
		try {
			csvParser.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}
}
