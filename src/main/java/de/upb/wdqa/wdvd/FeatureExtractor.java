package de.upb.wdqa.wdvd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Manifest;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.AsyncAppender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.dumpfiles.ExtendedMwRevisionDumpFileProcessor;
import org.wikidata.wdtk.dumpfiles.MwRevisionProcessor;

import de.upb.wdqa.wdvd.db.ItemStore;
import de.upb.wdqa.wdvd.db.MemoryItemStore;
import de.upb.wdqa.wdvd.features.Feature;
import de.upb.wdqa.wdvd.labels.RevertMethod;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;
import de.upb.wdqa.wdvd.processors.controlflow.AsyncProcessor;
import de.upb.wdqa.wdvd.processors.controlflow.ParallelProcessor;
import de.upb.wdqa.wdvd.processors.controlflow.TeeProcessor;
import de.upb.wdqa.wdvd.processors.decorators.CorpusLabelProcessor;
import de.upb.wdqa.wdvd.processors.decorators.FeatureProcessor;
import de.upb.wdqa.wdvd.processors.decorators.GeolocationProcessor;
import de.upb.wdqa.wdvd.processors.decorators.GroupProcessor;
import de.upb.wdqa.wdvd.processors.decorators.JsonProcessor;
import de.upb.wdqa.wdvd.processors.decorators.JsonProcessorReducer;
import de.upb.wdqa.wdvd.processors.decorators.PageProcessor;
import de.upb.wdqa.wdvd.processors.decorators.TagDownloaderProcessor;
import de.upb.wdqa.wdvd.processors.decorators.TextRegexProcessor;
import de.upb.wdqa.wdvd.processors.filters.SamplingFilterProcessor;
import de.upb.wdqa.wdvd.processors.output.CsvFeatureWriter;
import de.upb.wdqa.wdvd.processors.preprocessing.RawConverterProcessor;
import de.upb.wdqa.wdvd.processors.statistics.ActionStatisticsProcessor;
import de.upb.wdqa.wdvd.processors.statistics.CorpusStatisticsProcessor;
import de.upb.wdqa.wdvd.processors.statistics.LabelingStatisticsProcessor;
import de.upb.wdqa.wdvd.processors.statistics.RawDumpStatisticsProcessor;
import de.upb.wdqa.wdvd.processors.statistics.UserStatisticsProcessor;

public class FeatureExtractor {
	static final Logger logger = LoggerFactory.getLogger(FeatureExtractor.class);
	
	final static boolean PROCESSOR_GEOLOCATION_ENABLED = true;
	final static boolean PROCESSOR_REVISION_TAGS_ENABLED = true;	
	final static boolean PROCESSOR_FEATURE_LANGUAGE_PROPORTION_ENABLED = true;
	
	final static boolean ALL_FEATURES_ENABLED = true;
	
	final static int PROCESSOR_FEATURE_LANGUAGE_PROPORTION_THREADS = 12;
	final static int PROCESSOR_JSON_THREADS = 4;
	
	final static double LOW_QUALITY_SAMPLING_RATE = 1.0;
	final static double HIGH_QUALITY_SAMPLING_RATE = 1.0;

	final static Level LOG_LEVEL = Level.ALL;
	
	final static int BUFFER_SIZE = 256 * 1024 * 1024;	
	
	static String runTime;
	
	public static void main(String[] args) throws IOException{
		System.exit(main2(args));
	}
	
	public static int main2(String[] args){
		int result = 0;
		
		try{
			FeatureExtractorConfiguration config = new FeatureExtractorConfiguration(args);
			
			File featureFile = config.getFeatureFile();
			initLogger(featureFile.getAbsoluteFile() + ".log");
			logConfiguration(config);
			
			executePipeline(config);
			
			logger.info("Feature Extraction finished!");
			
			if (ErrorFlagAppender.hasErrorOccured()){
				result = 1;
				
				logger.error("##################################################");
				logger.error("# AN ERROR HAS OCCURED DURING FEATURE EXTRACTION #");
				logger.error("##################################################");
			}
			
			
		} catch (Throwable t){
			logger.error("", t);
			result = 1;
		}
		finally{
			try{
				closeLogger();
			}
			catch (Throwable t){
				System.err.println(t);
			}
		}
		
		return result;
	}
		
	public static void executePipeline(FeatureExtractorConfiguration config){
		try {
	//		dumpFile.prepareDumpFile();
			
	//		RevisionProcessor csvSHA1Processor = new CsvFeatureWriter(outputFilename + "_sha1.csv");
	//		RevisionProcessor samplingSHA1Processor = new SamplingFilterProcessor(csvSHA1Processor, LOW_QUALITY_SAMPLING_RATE, HIGH_QUALITY_SAMPLING_RATE, RevertMethod.SHA1);
			
	//		RevisionProcessor csvDownloadedSHA1Processor = new CsvFeatureWriter(outputFilename + "_downloaded_sha1.csv");
	//		RevisionProcessor samplingDownloadedSHA1Processor = new SamplingFilterProcessor(csvDownloadedSHA1Processor, LOW_QUALITY_SAMPLING_RATE, HIGH_QUALITY_SAMPLING_RATE, RevertMethod.DOWNLOADED_SHA1);
			
			//ItemStore itemStore = new SQLItemStore();
			ItemStore itemStore = new MemoryItemStore();
			
			List<Feature> features = FeatureList.getFeatures(ALL_FEATURES_ENABLED);
	
			RevisionProcessor nextProcessor = new CsvFeatureWriter(config.getFeatureFile(), features);
			
			
			for (Feature feature: features){
				List<Feature> featureList = new ArrayList<Feature>();
	
				featureList.add(feature);
				
				FeatureProcessor fp = new FeatureProcessor(nextProcessor, featureList);
				nextProcessor = new AsyncProcessor(fp, getFeatureString(featureList), 32);
			}
			
			nextProcessor = new AsyncProcessor(nextProcessor, "features", 1024);
			
			RevisionProcessor samplingRollbackProcessor = new SamplingFilterProcessor(nextProcessor, LOW_QUALITY_SAMPLING_RATE, HIGH_QUALITY_SAMPLING_RATE, RevertMethod.ROLLBACK);
			
			String name = "all";
			RevisionProcessor wholeCorpusStatisticsProcessor = new CorpusStatisticsProcessor(null, name, itemStore);
			
			TeeProcessor teeProcessor = new TeeProcessor();
			teeProcessor.add(wholeCorpusStatisticsProcessor);
			teeProcessor.add(samplingRollbackProcessor);
	//		teeProcessor.add(samplingDownloadedSHA1Processor);
	//		teeProcessor.add(samplingSHA1Processor);
			
			nextProcessor = new ActionStatisticsProcessor(teeProcessor, config.getFeatureFile().getAbsoluteFile() + "_monthlyActionDistribution.csv");
			
			nextProcessor = new LabelingStatisticsProcessor(nextProcessor);		
			
			nextProcessor = new UserStatisticsProcessor(nextProcessor);		
			
			nextProcessor = new GroupProcessor(nextProcessor);
			
			if (config.getLabelFile() != null){
				nextProcessor = new CorpusLabelProcessor(nextProcessor, config.getLabelFile());
			}
			else{
				logger.info("Labels are disabled.");
			}
			
			nextProcessor = new PageProcessor(nextProcessor, itemStore);
			
			
			List<RevisionProcessor> parallelProcessorList = new ArrayList<RevisionProcessor>();
			for (int i=0; i < PROCESSOR_FEATURE_LANGUAGE_PROPORTION_THREADS; i++){
				RevisionProcessor textRegexProcessor = new TextRegexProcessor(PROCESSOR_FEATURE_LANGUAGE_PROPORTION_ENABLED);
				parallelProcessorList.add(textRegexProcessor);
			}
			nextProcessor = new ParallelProcessor(parallelProcessorList, null, nextProcessor, "textRegex");
			
			
			parallelProcessorList = new ArrayList<RevisionProcessor>();
			for (int i = 0; i < PROCESSOR_JSON_THREADS; i++){
				RevisionProcessor jsonProcessor = new JsonProcessor(null, i);
				parallelProcessorList.add(jsonProcessor);
			}			
			nextProcessor = new ParallelProcessor(parallelProcessorList, new JsonProcessorReducer(), nextProcessor, "json");
			
			if(PROCESSOR_REVISION_TAGS_ENABLED && (config.getRevisionTagFile() != null)){
				nextProcessor = new TagDownloaderProcessor(nextProcessor, config.getRevisionTagFile());
			}
			else{
				logger.info("Revision tags are disabled.");
			}
			
			if(PROCESSOR_GEOLOCATION_ENABLED && (config.getGeolocationFile() != null)){
				nextProcessor = new GeolocationProcessor(nextProcessor, config.getGeolocationFile());
			}
			else{
				logger.info("Geolocation database is disabled.");
			}
			
			nextProcessor = new AsyncProcessor(nextProcessor, "initial", 1024);
			
			MwRevisionProcessor nextMwProcessor = new RawConverterProcessor(nextProcessor);
			
			nextMwProcessor = new RawDumpStatisticsProcessor(nextMwProcessor);
			
			ExtendedMwRevisionDumpFileProcessor dumpFileProcessor = new ExtendedMwRevisionDumpFileProcessor(nextMwProcessor);
			
	//		dumpFileProcessor.processDumpFileContents(dumpFile.getDumpFileStream(), dumpFile);
			
			// one thread retrieves the data and the other thread decompresses it.
			InputStream uncompressedStream = getUncompressedStream((getPipedDumpFileStream(getCompressedDumpFileStream(config.getRevisionFile()))));
			
			dumpFileProcessor.processDumpFileContents(uncompressedStream);
			
			uncompressedStream.close();			
			

		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	private static InputStream getCompressedDumpFileStream(File file) throws IOException{		
		InputStream fileInputStream = new FileInputStream(file);
		
		InputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		
		return bufferedInputStream;
	}
	
	private static InputStream getPipedDumpFileStream(final InputStream inputStream) throws IOException{
		final PipedOutputStream pipedOutputStream = new PipedOutputStream();
		final PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream, BUFFER_SIZE);
		
		new Thread("Dump File Reader"){
			@Override
			public void run(){
				try {
					IOUtils.copy(inputStream, pipedOutputStream);
					
					inputStream.close();
					pipedOutputStream.close();
				} catch (Throwable t) {
					logger.error("", t);
				}
			}
		}.start();
		
		return pipedInputStream;
	}
	
	// Decompresses the input stream in a new thread
	private static InputStream getUncompressedStream(final InputStream inputStream) throws IOException{
		// the decompression is a major bottleneck, make sure that it does not have to wait for the buffer to empty
		final PipedOutputStream pipedOutputStream = new PipedOutputStream();
		final PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream, BUFFER_SIZE);
		
		new Thread("Dump File Decompressor"){
			@Override
			public void run(){
				try {
					InputStream compressorInputStream = new BZip2CompressorInputStream(inputStream);
					
					IOUtils.copy(compressorInputStream, pipedOutputStream);
					
					compressorInputStream.close();
					pipedOutputStream.close();					
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}.start();
		
		return pipedInputStream;
	}
	
	private static void initLogger(String filename){
		final String PATTERN = "[%d{yyyy-MM-dd HH:mm:ss}] [%-5p] [%c{1}] %m%n";
		
		// Stores whether an error has occured
		AppenderSkeleton errorFlagAppender = new ErrorFlagAppender();
		errorFlagAppender.setThreshold(Level.ERROR);
		errorFlagAppender.activateOptions();
		org.apache.log4j.Logger.getRootLogger().addAppender(errorFlagAppender);

		ConsoleAppender consoleAppender = new ConsoleAppender(); 
		consoleAppender.setEncoding("UTF-8");
		consoleAppender.setLayout(new PatternLayout(PATTERN)); 
		consoleAppender.setThreshold(LOG_LEVEL);
		consoleAppender.activateOptions();		
		AsyncAppender asyncConsoleAppender = new AsyncAppender();
		asyncConsoleAppender.addAppender(consoleAppender);
		asyncConsoleAppender.setBufferSize(1024);
		asyncConsoleAppender.activateOptions();
		org.apache.log4j.Logger.getRootLogger().addAppender(asyncConsoleAppender);
		
		FileAppender fileAppender = new FileAppender();
		fileAppender.setEncoding("UTF-8");
		fileAppender.setFile(filename);
		fileAppender.setLayout(new PatternLayout(PATTERN));
		fileAppender.setThreshold(LOG_LEVEL);
		fileAppender.setAppend(false);
		fileAppender.activateOptions();		
		AsyncAppender asyncFileAppender = new AsyncAppender();
		asyncFileAppender.addAppender(fileAppender);
		asyncFileAppender.setBufferSize(1024);
		asyncFileAppender.activateOptions();
		org.apache.log4j.Logger.getRootLogger().addAppender(asyncFileAppender);
		
	}
	
   static class ErrorFlagAppender extends AppenderSkeleton
    {
	   static boolean hasErrorOccured = false;
	   
	   public static boolean hasErrorOccured(){
		   return hasErrorOccured;
	   }
  
		@Override
		public void close() {
		}

		@Override
		public boolean requiresLayout() {
			return false;
		}

		@Override
		protected void append(LoggingEvent arg0) {
			hasErrorOccured = true;
		}
    }
	
	private static void logConfiguration(FeatureExtractorConfiguration config){
//		System.getProperties().list(System.out);
		
//		for(Package p: Package.getPackages()){
//			System.out.println(p);
//		}
		
	    if (logger.isInfoEnabled()){
	    	// Host and operating system
	    	logger.info("Host name: " + getHostName());
	    	logger.info("Operating system: " + System.getProperty("os.name"));
	    	
	    	// Java
	    	logger.info("java.home: " + System.getProperty("java.home") );
	    	logger.info("java.version: " + System.getProperty("java.version"));
	    	logger.info("java.runtime.name: " + System.getProperty("java.runtime.name"));
	    	logger.info("java.runtime.version: " + System.getProperty("java.runtime.version"));
	    	logger.info("java.vm.name: " + System.getProperty("java.vm.name"));
	    	logger.info("java.vm.version: " + System.getProperty("java.vm.version"));
	    	logger.info("java.vm.vendor: " + System.getProperty("java.vm.vendor"));
	    	
	    	// Feature Extraction
		    logger.info("Filename of JAR: " + getJarFile());
		    logger.info("Implementation version: " + FeatureExtractor.class.getPackage().getImplementationVersion());
		    logger.info("Build time: " + getBuildTime());
		    logger.info("Run time: " + getRunTime());
		    
		    // Configuration
		    logger.info("Revision file: " + config.getRevisionFile());
		    logger.info("Label file: " + config.getLabelFile());
		    logger.info("Feature file: " + config.getFeatureFile());
		    logger.info("Revision tag file: " + config.getRevisionTagFile());
		    logger.info("Geolocation file: " + config.getGeolocationFile());
		    logger.info("Geolocation enabled: " + PROCESSOR_GEOLOCATION_ENABLED);
		    logger.info("TagDownloader enabled: " + PROCESSOR_REVISION_TAGS_ENABLED);
		    logger.info("Feature languageProportion enabled: " + PROCESSOR_FEATURE_LANGUAGE_PROPORTION_ENABLED);
	    }
	}
	
	private static void closeLogger(){
		org.apache.log4j.LogManager.shutdown();	
		Enumeration<?> e = org.apache.log4j.Logger.getRootLogger().getAllAppenders();
		while ( e.hasMoreElements() ){
			Appender appender = (Appender)e.nextElement();
			appender.close();
		}
	}
	
	private static String getHostName(){
		String result = null;
		try{
			result = InetAddress.getLocalHost().getHostName();
		}
		catch(UnknownHostException e){
		}
		
		return result;
	}
	
	static private String getBuildTime(){
		String result = null;
		try {
			Enumeration<URL> resources;
			resources = FeatureExtractor.class.getClassLoader()
					  .getResources("META-INF/MANIFEST.MF");

	//		while (resources.hasMoreElements()) {
		    Manifest manifest = new Manifest(resources.nextElement().openStream());
		      // check that this is your manifest and do what you need or get the next one
		    
		    result = manifest.getMainAttributes().getValue("Build-Time");
		    
			} catch (IOException e) {
			}

	    return result;
	}
	
	static private String getRunTime(){
		
		if (runTime == null){
		    Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		    runTime = sdf.format(cal.getTime());
		}
	    
	    return runTime;
	}
	
	static private File getJarFile(){
		File result = new java.io.File(FeatureExtractor.class.getProtectionDomain()
					.getCodeSource()
					.getLocation()
					.getPath());
		
		return result;
	}
	
	private static String getFeatureString(List<Feature> features){
		StringBuilder featureString = new StringBuilder();
		for(Feature feature: features){
			featureString.append(feature.getName() + ",");
		}
		
		return featureString.toString();
	}
	
}
