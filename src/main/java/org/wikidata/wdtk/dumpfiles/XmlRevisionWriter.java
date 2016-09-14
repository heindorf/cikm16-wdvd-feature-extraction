package org.wikidata.wdtk.dumpfiles;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.dumpfiles.MwRevisionDumpFileProcessor;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class XmlRevisionWriter implements RevisionProcessor {
	static final String A_VERSION = "version";
	static final String V_VERSION = "0.9";
	
	static final String E_DBNAME = "dbname";
	static final String E_GENERATOR = "generator";
	static final String E_CASE = "case";
	static final String E_NAMESPACES = "namespaces";
	static final String A_CASE = "case";
	
	static final String V_CASE_FIRST_LETTER = "first-letter";
	static final String V_SITENAME = "Wikidata";
	static final String V_DBNAME = "wikidatawiki";
	static final String V_BASEURL= "http://www.wikidata.org/wiki/Wikidata:Main_Page";
	
	static final String URI_NS_MEDIAWIKI = "http://www.mediawiki.org/xml/export-0.9/";
	static final String URI_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	static final String URI_SCHEMA_LOCATION = "http://www.mediawiki.org/xml/export-0.9/ http://www.mediawiki.org/xml/export-0.9.xsd";
	
	static final Logger logger = LoggerFactory.getLogger(XmlRevisionWriter.class);
	static final String SCHEMA_FILE_NAME = "export-0.9.xsd";
	
	String path;
	String generator;
	XMLOutputFactory xmlFactory;
	XMLStreamWriter xmlWriter;
	
	// Saving the threads such that we can wait for their termination
	Thread transformerThread;
	Thread compressorThread;
	
	// Closing the xml output stream (closing XMLStreamWriter does NOT close the underlying stream).
	PipedOutputStream pipedXMLOutputStream;
	
	int prevPageId = -1;
	
	public XmlRevisionWriter(String path, String generator) {
		super();
		this.path = path;
		this.generator = generator;
	}

	@Override
	public void startRevisionProcessing() {

		try {			
			this.xmlFactory = XMLOutputFactory.newInstance();
			
			// first, generate XML
			pipedXMLOutputStream =new PipedOutputStream();
			this.xmlWriter = this.xmlFactory.createXMLStreamWriter(pipedXMLOutputStream, "utf-8");
			PipedInputStream pipedXMLInputStream = new PipedInputStream(pipedXMLOutputStream);
			
			// second, make this XML look nice (Transformer Thread)
			
			// third, compress this nice XML (Compressor Thread)
			//     the compression seems to be the bottleneck of the whole program
			//     --> make sure that the incoming pipe is never empty
			final int BUFFER_BEFORE_COMPRESSION = 256 * 1024 * 1024;
			PipedOutputStream pipedCompressOutputStream = new PipedOutputStream();
			PipedInputStream pipedCompressInputStream = new PipedInputStream(pipedCompressOutputStream, BUFFER_BEFORE_COMPRESSION);
			
			// fourth, write this compresses XML to a file
			OutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(path));
			
			// Used for indentation
			transformerThread = new TransformerThread(pipedXMLInputStream, pipedCompressOutputStream);
			transformerThread.start();
			
			compressorThread = new CompressorThread(pipedCompressInputStream, fileOutputStream);
			compressorThread.setPriority(Thread.MAX_PRIORITY);
			compressorThread.start();
			
			xmlWriter.setDefaultNamespace(URI_NS_MEDIAWIKI);
			xmlWriter.writeStartDocument();
			xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_MEDIAWIKI);
			xmlWriter.writeDefaultNamespace(URI_NS_MEDIAWIKI);
			xmlWriter.setPrefix("xsi", URI_XSI);
			xmlWriter.writeNamespace("xsi", URI_XSI);
			xmlWriter.writeAttribute(URI_XSI, "schemaLocation", URI_SCHEMA_LOCATION);
			xmlWriter.writeAttribute(A_VERSION, V_VERSION);
			xmlWriter.writeAttribute("xml:lang", "en");

			writeXMLSiteInfo();
		} catch (XMLStreamException | IOException e) {
			logger.error("", e);
		}
	}

	@Override
	public void processRevision(Revision revision) {
		try {
			if(prevPageId != revision.getPageId()){
				// Is this not the very first revision processed?
				if (prevPageId != -1){	
					endPage();	
				}
				startPage(revision);				
			}
			
			xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_PAGE_REVISION);			
			writeRevision(revision);			
			xmlWriter.writeEndElement();
		
		} catch (XMLStreamException e) {
			logger.error("", e);
		}
		
		prevPageId = revision.getPageId();
		
	}

	@Override
	public void finishRevisionProcessing() {
		try {
			endPage();
			xmlWriter.writeEndElement(); // </mediawiki>		
			xmlWriter.writeEndDocument();	
		} catch (XMLStreamException e) {
			logger.error("", e);
		}
		
		try {
			xmlWriter.close();
		} catch (XMLStreamException e) {
			logger.error("", e);
		}

		try {
			pipedXMLOutputStream.close();
		} catch (IOException e) {
			logger.error("", e);
		}
		
		try {
			// wait until all threads are finished and all data is written
			transformerThread.join();
			compressorThread.join();
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		
//		validateXML();
	}
	
	void writeXMLSiteInfo() throws XMLStreamException {		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_SITEINFO);
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_SITENAME);
		xmlWriter.writeCharacters(V_SITENAME);
		xmlWriter.writeEndElement();
		
		xmlWriter.writeStartElement(E_DBNAME);
		xmlWriter.writeCharacters(V_DBNAME);
		xmlWriter.writeEndElement();
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_BASEURL);
		xmlWriter.writeCharacters(V_BASEURL);
		xmlWriter.writeEndElement();
		
		xmlWriter.writeStartElement(E_GENERATOR);
		xmlWriter.writeCharacters(generator);
		xmlWriter.writeEndElement();
		
		xmlWriter.writeStartElement(E_CASE);
		xmlWriter.writeCharacters(V_CASE_FIRST_LETTER);
		xmlWriter.writeEndElement();
		
		xmlWriter.writeStartElement(E_NAMESPACES);
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_NAMESPACE);
		xmlWriter.writeAttribute(MwRevisionDumpFileProcessor.A_NSKEY, "0");
		xmlWriter.writeAttribute(A_CASE, V_CASE_FIRST_LETTER);
		xmlWriter.writeEndElement();
		
		xmlWriter.writeEndElement();		
		xmlWriter.writeEndElement();		
	}
	
	void startPage(Revision revision) throws XMLStreamException{
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_PAGE);
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_PAGE_TITLE);
		xmlWriter.writeCharacters(revision.getTitle());
		xmlWriter.writeEndElement();
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_PAGE_NAMESPACE);
		xmlWriter.writeCharacters("" + revision.getNamespace());
		xmlWriter.writeEndElement();
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_PAGE_ID);
		xmlWriter.writeCharacters("" + revision.getPageId());
		xmlWriter.writeEndElement();		
	}
	
	void endPage() throws XMLStreamException{
		xmlWriter.writeEndElement();
	}
	
	private void writeRevision(Revision revision) throws XMLStreamException {
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_REV_ID);
		xmlWriter.writeCharacters("" + revision.getRevisionId());
		xmlWriter.writeEndElement();
		
		if (revision.getParentId() != null){
			xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_REV_PARENT_ID);
			xmlWriter.writeCharacters("" + revision.getParentId());
			xmlWriter.writeEndElement();
		}
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_REV_TIMESTAMP);
		xmlWriter.writeCharacters(revision.getTimeStamp());
		xmlWriter.writeEndElement();
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_REV_CONTRIBUTOR);		
		writeContributor(revision);		
		xmlWriter.writeEndElement();
		
		if(revision.isMinor()){
			xmlWriter.writeEmptyElement(MwRevisionDumpFileProcessor.E_REV_MINOR);			
		}
		
		if(revision.getComment() != null){
			xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_REV_COMMENT);
			if(revision.isCommentDeleted()){
				xmlWriter.writeAttribute(ExtendedMwRevisionDumpFileProcessor.A_DELETED, ExtendedMwRevisionDumpFileProcessor.A_DELETED);
			}		
			xmlWriter.writeCharacters(revision.getComment());
			xmlWriter.writeEndElement();
		}
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_REV_TEXT);
		if(revision.isTextDeleted()){
			xmlWriter.writeAttribute(ExtendedMwRevisionDumpFileProcessor.A_DELETED, ExtendedMwRevisionDumpFileProcessor.A_DELETED);
		}
		else{
			xmlWriter.writeAttribute("xml:space", "preserve");
		}
		xmlWriter.writeCharacters(revision.getText());
		xmlWriter.writeEndElement();
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_REV_SHA1);
		xmlWriter.writeCharacters(revision.getSHA1());
		xmlWriter.writeEndElement();
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_REV_MODEL);
		xmlWriter.writeCharacters(revision.getModel());
		xmlWriter.writeEndElement();
		
		xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_REV_FORMAT);
		xmlWriter.writeCharacters(revision.getFormat());
		xmlWriter.writeEndElement();		
	}
	
	private void writeContributor(Revision revision) throws XMLStreamException {
		
		// registered contributor?
		if (revision.hasRegisteredContributor()){
			xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_CONTRIBUTOR_NAME);
			xmlWriter.writeCharacters(revision.getContributor());
			xmlWriter.writeEndElement();
			
			xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_CONTRIBUTOR_ID);
			xmlWriter.writeCharacters("" + revision.getContributorId());
			xmlWriter.writeEndElement();
		}
		else{
			xmlWriter.writeStartElement(MwRevisionDumpFileProcessor.E_CONTRIBUTOR_IP);
			xmlWriter.writeCharacters("" + revision.getContributor());
			xmlWriter.writeEndElement();
		}
		
	}
	
	// Does not work because it is too slow.
//	private void validateXML(){
//		try{
//			logger.info("Starting to validate XML...");
//		
//			final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//			
//			final Schema schema = schemaFactory.newSchema(new StreamSource(this.getClass().getResourceAsStream("/" + SCHEMA_FILE_NAME)));  
//			final Validator validator = schema.newValidator();  
//			validator.validate(new StreamSource(new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(path)))));
//		}
//		catch(Exception e){
//			logger.error("Validation exception: ", e);
//		}
//		finally{
//			logger.info("XML validation finished.");
//		}
//	}
}


class TransformerThread extends Thread {
	static final Logger logger = LoggerFactory.getLogger(TransformerThread.class);
	
	InputStream in;
	OutputStream out;
	
	public TransformerThread(InputStream in, OutputStream out){
		super("XML Transformer");
		this.in = in;
		this.out = out;
	}
	
    public void run() {
    	try {
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        final Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");
     		
			transformer.transform(new StreamSource(in), new StreamResult(out));
			
			in.close();
			out.close();
		} catch (Throwable t) {
			logger.error("", t);
		}
    }
};

class CompressorThread extends Thread{
	static final Logger logger = LoggerFactory.getLogger(CompressorThread.class);
	
	InputStream in;
	OutputStream out;
	
	public CompressorThread(InputStream in, OutputStream out){
		super("XML Compressor");		
		this.in = in;
		this.out = out;
	}
	
    public void run() {
   		try {
			// This compression seems to be really slow and is a major bottleneck of the whole program
			OutputStream compressedOutStream = new BZip2CompressorOutputStream(out, 1);
   			
			IOUtils.copy(in, compressedOutStream);
			
			in.close();
			compressedOutStream.close();
			out.close();
		} catch (IOException e) {
			logger.error("", e);
		}
    }
}

