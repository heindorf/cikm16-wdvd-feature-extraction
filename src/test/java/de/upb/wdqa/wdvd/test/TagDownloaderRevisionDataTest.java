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
