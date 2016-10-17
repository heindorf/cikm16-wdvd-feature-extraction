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
