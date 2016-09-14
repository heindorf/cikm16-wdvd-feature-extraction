package de.upb.wdqa.wdvd;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;

import de.upb.wdqa.wdvd.FeatureExtractor;

public class FeatureExtractorTest {

	
	@Test
	public void SimpleSmokeTest() throws IOException {
		File tmpDirectory = null;
		try{
			String[] args = new String[2];
			
			tmpDirectory = Files.createTempDirectory("WDVD").toFile();
			
			args[0] = "src/test/resources/SmokeTest.xml.bz2";
			args[1] = new File(tmpDirectory, "features.csv.bz2").toString();
			
			int result = FeatureExtractor.main2(args);
			
			assertEquals(0, result);
		}
		finally{
			delete(tmpDirectory);
		}
		
	}
	
	static void delete(File f) throws IOException {
		if (f!= null){
			  if (f.isDirectory()) {
			    for (File c : f.listFiles())
			      delete(c);
			  }
			  if (!f.delete())
			    throw new FileNotFoundException("Failed to delete file: " + f);
			}
		}
}
