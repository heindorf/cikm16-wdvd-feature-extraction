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

package org.wikidata.wdtk.dumpfiles;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.wikidata.wdtk.dumpfiles.wmf.WmfLocalDumpFile;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.upb.wdqa.wdvd.processors.RevisionProcessor;
import de.upb.wdqa.wdvd.processors.preprocessing.AdapterProcessor;
import de.upb.wdqa.wdvd.test.TestUtils;

import org.mockito.Mockito;

public class XmlRevisionWriterTest extends XMLTestCase {
	
	@Before
	public void setUp() {
		TestUtils.initializeLogger();
	}
	
	@Test
	public void testRevisionWriter() throws SAXException, IOException {
		String controlPath = "src/test/resources/dumpTest.xml";
		String testPath = "target/writerTestResult.xml.bz2";

		
		RevisionProcessor xmlRevisionWriter = new XmlRevisionWriter(testPath, "MediaWiki 1.25wmf6");
		
		MwRevisionProcessor asyncProcessor = new AdapterProcessor(xmlRevisionWriter);
		
		MwDumpFileProcessor dumpFileProcessor = new ExtendedMwRevisionDumpFileProcessor(asyncProcessor);
		
		MwDumpFile mockDumpFile = Mockito.mock(WmfLocalDumpFile.class);
		
		dumpFileProcessor.processDumpFileContents(new FileInputStream(controlPath), mockDumpFile);
		
		XMLUnit.setIgnoreWhitespace(true);
		
		Diff diff = XMLUnit.compareXML(new InputSource(controlPath),
					new InputSource(new BZip2CompressorInputStream(new FileInputStream(testPath))));
		
		assertTrue(diff.identical());
	}

}
