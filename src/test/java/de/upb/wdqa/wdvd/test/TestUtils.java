package de.upb.wdqa.wdvd.test;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class TestUtils {
	
	public static void initializeLogger(){
		ConsoleAppender console = new ConsoleAppender();
		console.setLayout(new PatternLayout("[%d{yyyy-MM-dd HH:mm:ss}] [%-5p] [%c{1}] %m%n"));
		console.setThreshold(Level.ALL);
		console.activateOptions();
		org.apache.log4j.Logger.getRootLogger().removeAllAppenders();
		org.apache.log4j.Logger.getRootLogger().addAppender(console);
		
		Logger logger = Logger.getLogger(TestUtils.class);
		logger.info("test logger");		
	}

}
