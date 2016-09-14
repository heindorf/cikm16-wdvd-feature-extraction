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
    public void setUp(){
		TestUtils.initializeLogger();		
    }
	
	@Test
	public void testRevisionWriter() throws SAXException, IOException{
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
