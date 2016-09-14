package de.upb.wdqa.wdvd.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;












import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.upb.wdqa.wdvd.db.implementation.DbRevisionImpl;
import de.upb.wdqa.wdvd.db.implementation.DbTagFactory;
import de.upb.wdqa.wdvd.db.interfaces.DbRevision;
import de.upb.wdqa.wdvd.db.interfaces.DbTag;
import de.upb.wdqa.wdvd.revisiontags.SHA1Converter;
import de.upb.wdqa.wdvd.revisiontags.TagDownloaderRevisionData;

public class TagDownloaderRevisionDataTest {
	
	DbTagFactory tagFactory = new DbTagFactory();
	
	@Test
	public void testTags(){		
		List<String> emptyTags = new ArrayList<String>();
		List<String> emptyStringTags = new ArrayList<String>();
		emptyStringTags.add("");
		List<String> oneStringTags = new ArrayList<String>();
		oneStringTags.add("a");
		List<String> twoStringTags = new ArrayList<String>();
		twoStringTags.add("b");
		twoStringTags.add("c");
		
		DbRevision revision = getDbRevision("", emptyTags);
		TagDownloaderRevisionData data = new TagDownloaderRevisionData(revision, tagFactory);
		Assert.assertEquals(0, data.getTags().size());
		
		revision = getDbRevision("", emptyStringTags);
		data = new TagDownloaderRevisionData(revision, tagFactory);
		Assert.assertEquals(1, data.getTags().size());
		Assert.assertTrue(containsTagName("", data.getTags()));
		
		revision = getDbRevision("", oneStringTags);
		data = new TagDownloaderRevisionData(revision, tagFactory);
		Assert.assertEquals(1, data.getTags().size());
		Assert.assertTrue(containsTagName("a", data.getTags()));
		
		revision = getDbRevision("", twoStringTags);
		data = new TagDownloaderRevisionData(revision, tagFactory);
		Assert.assertEquals(2, data.getTags().size());
		Assert.assertTrue(containsTagName("b", data.getTags()));
		Assert.assertTrue(containsTagName("c", data.getTags()));
		

		byte[] bytes = data.getByteArray();
		System.out.println(bytes.length);
		
		TagDownloaderRevisionData loadedData = new TagDownloaderRevisionData(bytes, tagFactory);		
		Assert.assertEquals(data, loadedData);
		
		revision = getDbRevision("f5c51abf1b5f1812f2af898d9f2cc3dc875ca5c3", oneStringTags);
		data = new TagDownloaderRevisionData(revision, tagFactory);
		bytes = data.getByteArray();
		System.out.println(bytes.length);
		
		loadedData = new TagDownloaderRevisionData(bytes, tagFactory);		
		Assert.assertEquals(data, loadedData);
	}
	
	private DbRevision getDbRevision(String sha1Base16, List<String> tagNames){
		
		Set<DbTag> tags = new HashSet<DbTag>(); 
		
		for(String tagName: tagNames){
			tags.add(tagFactory.getTag(tagName));
		}		
		
		return new DbRevisionImpl(-1, SHA1Converter.base16to36(sha1Base16), tags);
	}
	
	private boolean containsTagName(String tagName, Set<DbTag> tags){
		Set<String> tagNames = new HashSet<String>();
		for(DbTag tag: tags){
			tagNames.add(tag.getTagName());
		}
		
		return tagNames.contains(tagName);
	}
}
