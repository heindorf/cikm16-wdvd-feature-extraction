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

package de.upb.wdqa.wdvd;

import java.io.File;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class FeatureExtractorConfiguration {
	private static final String CMD_NAME = "wdvd-feature-extraction";
	
	private static final String OPTION_LABEL_FILE = "labels";
	private static final String OPTION_REVISION_TAG_FILE = "revisiontags";
	private static final String OPTION_GEOLOCATION_DB_FILE = "geodb";
	private static final String OPTION_GEOLOCATION_FEATURE_FILE = "geofeatures";

	private static final String ARGUMENT_REVISION_FILE = "REVISIONS";
	private static final String ARGUMENT_FEATURE_FILE = "FEATURES";
	
	private static final String CMD_LINE_SYNTAX =
			"java -jar " + CMD_NAME + "\n"
			+ "          "
			+ "[--" + OPTION_LABEL_FILE + " <FILE>] "
			+ "[--" + OPTION_REVISION_TAG_FILE + " <FILE>] "
			+ "[--" + OPTION_GEOLOCATION_DB_FILE + " <FILE>] \n"
			+ "[--" + OPTION_GEOLOCATION_FEATURE_FILE + " <FILE>] \n"
			+ "          "
			+ ARGUMENT_REVISION_FILE + " "
			+ ARGUMENT_FEATURE_FILE + "\n\n";

	private static final String HEADER =
			"Given a REVISIONS file (in bz2 format), extracts features and stores them in the FEATURES file (in bz2 format). "
		  + "If no files for labels, revision tags or the geolocation database are specified, the corresponding features will be 'NA'."
	      + "\n"
		  + "\n"
		  + "Options:";
	
	private static final String FOOTER = "";
	
	
	private File labelFile;
	private File revisionTagFile;	
	private File geolocationDbFile;
	private File geolocationFeatureFile;
	
	private File revisionFile;
	private File featureFile;
	
	private static final Options options;
	private static final HelpFormatter helpFormatter;
	
	
	public FeatureExtractorConfiguration(String[] args) {
		readArgs(args);	
	}
	
	public File getRevisionFile() {
		return this.revisionFile;
	}
	
	public File getLabelFile() {
		return this.labelFile;
	}
	
	public File getGeolocationDbFile() {
		return this.geolocationDbFile;
	}
	
	public File getGeolocationFeatureFile() {
		return this.geolocationFeatureFile;
	}
	
	public File getRevisionTagFile() {
		return this.revisionTagFile;
	}
	
	public File getFeatureFile() {
		return this.featureFile;
	}
	
	public void printHelp() {
		helpFormatter.printHelp(CMD_LINE_SYNTAX, HEADER, options, FOOTER);
	}
	

	
	private void readArgs(String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			labelFile = getFileFromOption(cmd, OPTION_LABEL_FILE);
			geolocationDbFile = getFileFromOption(cmd, OPTION_GEOLOCATION_DB_FILE);
			geolocationFeatureFile = getFileFromOption(cmd, OPTION_GEOLOCATION_FEATURE_FILE);
			revisionTagFile = getFileFromOption(cmd, OPTION_REVISION_TAG_FILE);
			
			List<String> argList = cmd.getArgList();
			
			if (argList.size() != 2) {
				printHelp();
			} else {
				revisionFile = new File(argList.get(0));
				featureFile = new File(argList.get(1));
			}
		} catch (ParseException exp) {
			System.err.print(exp);
		}
	}
	
	private static File getFileFromOption(CommandLine cmd, String option) {
		File result = null;
		
		String optionValue = cmd.getOptionValue(option);
		if (optionValue != null) {
			result = new File(optionValue); 
		}
		
		return result;
	}
	
	static {
		options = new Options();
			
		Option labelFile = Option.builder()
				.longOpt(OPTION_LABEL_FILE)
				.argName("FILE")
				.hasArg()
				.desc("use given labels (bz2 format)")
				.build();
		
		Option geolocationDbFile = Option.builder()
				.longOpt(OPTION_GEOLOCATION_DB_FILE)
				.argName("FILE")
				.hasArg()
				.desc("use given IP geolocation database (bz2 format)")
				.build();
		
		Option geolocationFeatureFile = Option.builder()
				.longOpt(OPTION_GEOLOCATION_FEATURE_FILE)
				.argName("FILE")
				.hasArg()
				.desc("use given geolocation feature file (bz2 format)")
				.build();
		
		Option revisionTagFile = Option.builder()
				.longOpt(OPTION_REVISION_TAG_FILE)
				.argName("FILE")
				.hasArg()
				.optionalArg(false)
				.desc("use given revision tags (bz2 format)")
				.build();
		
		final List<Option> optionList = new LinkedList<Option>();
		
		optionList.add(labelFile);
		optionList.add(geolocationDbFile);
		optionList.add(geolocationFeatureFile);
		optionList.add(revisionTagFile);
		
		
		for (Option option: optionList) {
			options.addOption(option);
		}
		
		helpFormatter = new HelpFormatter();
		helpFormatter.setOptionComparator(new Comparator<Option>() {
			@Override
			public int compare(Option o1, Option o2) {
				int pos1 = optionList.indexOf(o1);
				int pos2 = optionList.indexOf(o2);
				
				return Integer.compare(pos1, pos2);

			}
		});
	}

}
