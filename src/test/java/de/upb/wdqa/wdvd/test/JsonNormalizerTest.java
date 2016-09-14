package de.upb.wdqa.wdvd.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonItemDocument;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.JsonNormalizer;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.OldJacksonItemDocument;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.OldJacksonRedirectDocument;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.wdtk.MapDeserializerModifier;

public class JsonNormalizerTest {
	
	@BeforeClass
	public static void SetUp(){
		TestUtils.initializeLogger();
	}
	
	
	@Test(expected=com.fasterxml.jackson.databind.JsonMappingException.class)
	public void testWDTKParsing() throws IOException{
		testWDTKParsing("src/test/resources/oldJson01.json");
	}
	
	// no exception expected
	@Test
	public void testWDTKParsing01() throws IOException{
		testWDTKParsing("src/test/resources/newJson01.json");
	}
	
	
	@Test
	public void testWDTKParsing02() throws IOException{
		// sitelinks are represented as empty array
		testWDTKParsing("src/test/resources/newJson02.json");
	}
	
	@Test
	public void testWDTKParsing03() throws IOException{
		// aliases are represented as empty array
		testWDTKParsing("src/test/resources/newJson03.json");
	}
	
	@Test
	public void testWDTKParsing04() throws IOException{
		// descriptions are represented as empty array
		testWDTKParsing("src/test/resources/newJson04.json");
	}
	
	@Test(expected=NullPointerException.class)
	public void testWDTKParsingNewJsonGlobeIriNull() throws IOException{
		// Throws a NullPointerException because the JSON contains an entry "globe":null in a globecoordinate
		testWDTKParsing("src/test/resources/newJsonGlobeIriNull.json");
	}
	
	@Test
	public void testJsonParsing01() throws IOException{
		testOldFormatParsing("src/test/resources/oldJson01.json");
	}
	
	@Test
	public void testJsonParsing02() throws IOException{
		testOldFormatParsing("src/test/resources/oldJson02.json");
	}
	
	@Test
	public void testJsonParsing03() throws IOException{
		testOldFormatParsing("src/test/resources/oldJson03.json");
	}
	
	@Test
	public void testJsonParsing04() throws IOException{
		testOldFormatParsing("src/test/resources/oldJson04.json");
	}
	
	/**
	 * Links without badges
	 */
	@Test
	public void testJsonParsing05() throws IOException{
		testOldFormatParsing("src/test/resources/oldJson05.json");
	}

	@Test
	public void testJsonParsing06() throws IOException{
		testOldFormatParsing("src/test/resources/oldJson06.json");
	}

	@Test
	public void testJsonParsing07() throws IOException{
		testOldFormatParsing("src/test/resources/oldJson07.json");
	}
	
	@Test
	public void testEmptyAliases() throws IOException{
		testOldFormatParsing("src/test/resources/oldJsonEmptyAliases.json");
	}	
	
	@Test(expected=com.fasterxml.jackson.databind.JsonMappingException.class)
	public void testRedirect1() throws IOException{
		String jsonString = readFile("src/test/resources/oldJson01.json", StandardCharsets.UTF_8);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.readValue(jsonString, OldJacksonRedirectDocument.class);
	}
	
	@Test
	public void testRedirect2() throws IOException{
		String jsonString = readFile("src/test/resources/redirect.json", StandardCharsets.UTF_8);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.readValue(jsonString, OldJacksonRedirectDocument.class);
	}
	
	@Test(expected=com.fasterxml.jackson.databind.JsonMappingException.class)
	public void testWDTKRedirect() throws IOException{
		testWDTKParsing("src/test/resources/redirect.json");
	}
	
	@Test(expected=com.fasterxml.jackson.databind.JsonMappingException.class)
	public void testOldJsonRedirect() throws IOException{
		testOldFormatParsing("src/test/resources/redirect.json");
	}
	
	@Test
	public void testNormalizedJsonParsing2() throws IOException{
		String jsonString = readFile("src/test/resources/oldJson02.json", StandardCharsets.UTF_8);
		
		ObjectMapper mapper = new ObjectMapper();
		OldJacksonItemDocument oldDoc = mapper.readValue(jsonString, OldJacksonItemDocument.class);
		
		Assert.assertNotNull(oldDoc.getLabels());
		Assert.assertNotNull(oldDoc.getDescriptions());
		Assert.assertNotNull(oldDoc.getAliases());
		Assert.assertNotNull(oldDoc.getClaims());
		Assert.assertNotNull(oldDoc.getSiteLinks());
		
		JacksonItemDocument doc = JsonNormalizer.normalizeFormat(oldDoc);
		
		Assert.assertNotNull(doc.getLabels());
		Assert.assertNotNull(doc.getDescriptions());
		Assert.assertNotNull(doc.getAliases());
		Assert.assertNotNull(doc.getJsonClaims());
		Assert.assertNotNull(doc.getSiteLinks());
		
				
		JsonNode node = mapper.valueToTree(doc);
		mapper.readValue(mapper.treeAsTokens(node), JacksonItemDocument.class);
	}
	
	@Test
	public void testOldEmptyWorkaround() throws IOException{
		testOldFormatParsing("src/test/resources/oldEmptyWorkaround.json");
	}
	
	@Test
	public void testNewEmptyWorkaround() throws IOException{
		testWDTKParsing("src/test/resources/newEmptyWorkaround.json");
	}
	

	
	private void testOldFormatParsing(String filename) throws IOException{
		String jsonString = readFile(filename, StandardCharsets.UTF_8);
		
		ObjectMapper mapper = new ObjectMapper();
		OldJacksonItemDocument oldDoc = mapper.readValue(jsonString, OldJacksonItemDocument.class);
		
		JacksonItemDocument itemDocument = JsonNormalizer.normalizeFormat(oldDoc);		
				
		JsonNode node = mapper.valueToTree(itemDocument);
		mapper.readValue(mapper.treeAsTokens(node), JacksonItemDocument.class);
		
		itemDocument.setSiteIri(Datamodel.SITE_WIKIDATA);		
		itemDocument.toString(); // raises an exception if information is missing
	}
	
	private void testWDTKParsing(String filename) throws IOException{
		String jsonString = readFile(filename, StandardCharsets.UTF_8);
		
		SimpleModule module = new SimpleModule();
		module.setDeserializerModifier(new MapDeserializerModifier());
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);

//		JsonNode root = mapper.readTree(jsonString);		
//		mapper.readValue(mapper.treeAsTokens(root), JacksonItemDocument.class);
		
		JacksonItemDocument itemDocument = mapper.readValue(jsonString, JacksonItemDocument.class);
		
		itemDocument.setSiteIri(Datamodel.SITE_WIKIDATA);		
		itemDocument.toString(); // raises an exception if information is missing
	}
	
	static String readFile(String path, Charset encoding) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
