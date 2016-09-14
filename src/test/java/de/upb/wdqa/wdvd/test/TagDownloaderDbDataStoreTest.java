package de.upb.wdqa.wdvd.test;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.upb.wdqa.wdvd.db.implementation.DbRevisionImpl;
import de.upb.wdqa.wdvd.db.implementation.DbTagFactory;
import de.upb.wdqa.wdvd.db.interfaces.DbRevision;
import de.upb.wdqa.wdvd.db.interfaces.DbTag;
import de.upb.wdqa.wdvd.revisiontags.TagDownloaderDataStore;
import de.upb.wdqa.wdvd.revisiontags.TagDownloaderDbDataStore;

// Test is ignored because it requires MySQL dependency in POM file.
@Ignore
public class TagDownloaderDbDataStoreTest {
	DbTagFactory tagFactory = new DbTagFactory();
	
	TagDownloaderDataStore dataStore = new TagDownloaderDbDataStore(tagFactory);
	
	@BeforeClass
	public static void SetUp(){
		TestUtils.initializeLogger();
	}
	
    @Before
    public void before() throws Exception {    	
		dataStore.connect();
		dataStore.clear();
    }
 
    @After
    public void after() throws Exception {
    	dataStore.disconnect();
    }

	
	@Test
	public void testInsertRevision() throws Exception{
		DbRevision revision = new DbRevisionImpl(123456, "1234567890123456789012345678901", null);
		
		dataStore.putRevision(revision);
		
		dataStore.flush();		
		
		DbRevision resultRevision = dataStore.getRevision(123456);
		
		Assert.assertEquals(revision, resultRevision);
	}	
	
	@Test
	public void testClear() throws Exception{
		dataStore.clear();
	}
	
	@Test
	public void testMultipleRead() throws Exception{
		dataStore.putRevision(new DbRevisionImpl(1, "1", null));
		dataStore.putRevision(new DbRevisionImpl(2, "2", null));
		dataStore.putRevision(new DbRevisionImpl(3, "3", null));
		dataStore.putRevision(new DbRevisionImpl(4, "4", null));
		dataStore.flush();
		
		List<Long> revisionIds = new ArrayList<Long>();
		revisionIds.add(2L);
		revisionIds.add(4L);
		revisionIds.add(3L);
		revisionIds.add(1L);
		
		List<DbRevision> result = dataStore.getRevisions(revisionIds);
		
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(2L, result.get(0).getRevisionId());
		Assert.assertEquals(4L, result.get(1).getRevisionId());
		Assert.assertEquals(3L, result.get(2).getRevisionId());
		Assert.assertEquals(1L, result.get(3).getRevisionId());
	}
	
	@Test
	public void writePerformanceTest() throws Exception {
		long startTime = System.currentTimeMillis();
		
		for (long i = 0; i < 100000; i++){
			Set<DbTag> tags = new HashSet<DbTag>();
			tags.add(tagFactory.getTag("TestName" + (i % 50)));
			
			dataStore.putRevision(new DbRevisionImpl(i,"" + i, tags));
		}
		
		dataStore.flush();
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("Write time (in seconds): " + (endTime - startTime)/1000.0);
	}
	
	@Test
	public void multipleReadPerformanceTest() throws Exception{
		writePerformanceTest();		
		
		long startTime = System.currentTimeMillis();
		
		List<Long> list = new ArrayList<Long>();
		
		for (long i = 0; i < 100000; i++){
			list.add((i));
			
			if( i % 1000 == 999){
				dataStore.getRevisions(list);
				list = new ArrayList<Long>();
			}
		}
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("multiple read time (in seconds): " + (endTime - startTime)/1000.0);
	}
	
	@Test
	public void testTags() throws Exception{
		DbRevisionImpl revision = new DbRevisionImpl(42, "42", null);
		revision.addTag(tagFactory.getTag("Tag1"));
		revision.addTag(tagFactory.getTag("Tag2"));
		
		dataStore.putRevision(revision);
		dataStore.flush();
		
		DbRevision result = dataStore.getRevision(42);
		
		Assert.assertEquals(42, result.getRevisionId());
		Assert.assertEquals(2, result.getTags().size());
		Assert.assertTrue(result.getTagNames().contains("Tag1"));
		Assert.assertTrue(result.getTagNames().contains("Tag2"));
	}
	
	@Test
	public void testGetEmptyRevisionList() throws Exception{
		List<Long> revisionIds = new ArrayList<Long>();

		dataStore.getRevisions(revisionIds);
	}
}
