/*
 * WDVC-2015 Statistics Example
 * 
 * Copyright (C) 2015 Stefan Heindorf
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
