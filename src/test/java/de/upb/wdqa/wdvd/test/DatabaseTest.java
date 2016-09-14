package de.upb.wdqa.wdvd.test;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.upb.wdqa.wdvd.db.ItemStore;
import de.upb.wdqa.wdvd.db.SQLItemStore;
import de.upb.wdqa.wdvd.db.implementation.DbItemImpl;
import de.upb.wdqa.wdvd.db.interfaces.DbItem;
import de.upb.wdqa.wdvd.test.TestUtils;

// Test is ignored because it requires MySQL dependency in POM file.
@Ignore
public class DatabaseTest {	
	ItemStore db;
	
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
		TestUtils.initializeLogger();
		db = new SQLItemStore();
		db.connect();
		db.deleteItems();		
    }
 
    @After
    public void tearDown() throws SQLException {
    	db.close();
    }

	
	@Test
	public void testInsertItem() throws ClassNotFoundException, SQLException{
		DbItem item = new DbItemImpl(5, "human", 111, 222, 333);
		
		db.insertItem(item);
		db.flushItems();
		
		DbItem resultItem = db.getItem(5);
		
		Assert.assertEquals(item, resultItem);
	}	
	
	@Test
	public void test4ByteUnicodeItem(){
		DbItem item = new DbItemImpl(5, "\uD83D\uDC00", 111, 222, 333);
		
		db.insertItem(item);
		db.flushItems();
		
		DbItem resultItem = db.getItem(5);
		
		Assert.assertEquals(item, resultItem);		
	}
	
	@Test
	public void testLongLabels(){
		String longString =
				"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";				
		
		DbItem item = new DbItemImpl(5, longString, 111, 222, 333);
		
		db.insertItem(item);
		db.flushItems();
		
		DbItem resultItem = db.getItem(5);
		
		Assert.assertEquals(item, resultItem);		
	}
	
	
	@Test
	public void testDeleteItems() throws ClassNotFoundException, SQLException{
		db.deleteItems();
	}
	
	@Test
	public void performanceTest() throws ClassNotFoundException, SQLException {
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < 100000; i++){		
			db.insertItem(new DbItemImpl(i, "" + i, i,i,i));
		}
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("Time (in seconds): " + (endTime - startTime)/1000.0);
	}
}
