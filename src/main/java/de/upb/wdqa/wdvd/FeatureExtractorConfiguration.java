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
	private static final String OPTION_GEOLOCATION_FILE = "geodb";

	private static final String ARGUMENT_REVISION_FILE = "REVISIONS";
	private static final String ARGUMENT_FEATURE_FILE = "FEATURES";
	
	private static final String CMD_LINE_SYNTAX =
			"java -jar " + CMD_NAME + "\n"
			+ "          "
			+ "[--" + OPTION_LABEL_FILE + " <FILE>] "
			+ "[--" + OPTION_REVISION_TAG_FILE + " <FILE>] "
			+ "[--" + OPTION_GEOLOCATION_FILE + " <FILE>] \n"
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
	private File geolocationFile;
	
	private File revisionFile;
	private File featureFile;
	
	private static final Options options;
	private static final HelpFormatter helpFormatter;
	
	
	public FeatureExtractorConfiguration(String[] args){		
		readArgs(args);	
	}
	
	public File getRevisionFile(){
		return this.revisionFile;
	}
	
	public File getLabelFile(){
		return this.labelFile;
	}
	
	public File getGeolocationFile(){
		return this.geolocationFile;
	}
	
	public File getRevisionTagFile(){
		return this.revisionTagFile;
	}
	
	public File getFeatureFile(){
		return this.featureFile;
	}
	
	public void printHelp(){
		helpFormatter.printHelp(CMD_LINE_SYNTAX, HEADER, options, FOOTER);
	}
	

	
	private void readArgs(String[] args){
		CommandLineParser parser = new DefaultParser();
		
		try{
			CommandLine cmd = parser.parse( options, args);
			
			labelFile = getFileFromOption(cmd, OPTION_LABEL_FILE);
			geolocationFile = getFileFromOption(cmd, OPTION_GEOLOCATION_FILE);
			revisionTagFile = getFileFromOption(cmd, OPTION_REVISION_TAG_FILE);
			
			List<String> argList = cmd.getArgList();
			
			if(argList.size() != 2){
				printHelp();
			}
			else{
				revisionFile = new File(argList.get(0));
				featureFile = new File(argList.get(1));
			}
	    }
	    catch(ParseException exp ) {
	    	System.err.print(exp);
	    }
	}
	
	private static File getFileFromOption(CommandLine cmd, String option){
		File result = null;
		
		String optionValue = cmd.getOptionValue(option);
		if (optionValue != null){
			result = new File(optionValue); 
		}
		
		return result;
	}
	
	static {
		options = new Options();
			
		Option labelfile = Option.builder()
				.longOpt(OPTION_LABEL_FILE)
				.argName("FILE")
                .hasArg()
                .desc("use given labels (bz2 format)")
                .build();
		
		Option geolocationfile = Option.builder()
				.longOpt(OPTION_GEOLOCATION_FILE)
				.argName("FILE")
                .hasArg()
                .desc("use given IP geolocation database (bz2 format)")
                .build();
		
		Option revisiontagfile = Option.builder()
				.longOpt(OPTION_REVISION_TAG_FILE)
				.argName("FILE")
                .hasArg()
                .optionalArg(false)
                .desc("use given revision tags (bz2 format)")
                .build();
		
		final List<Option> optionList = new LinkedList<Option>();
		
		optionList.add(labelfile);
		optionList.add(geolocationfile);
		optionList.add(revisiontagfile);
		
		for (Option option: optionList){
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
