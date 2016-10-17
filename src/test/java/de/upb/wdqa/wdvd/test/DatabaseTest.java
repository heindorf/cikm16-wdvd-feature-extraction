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
	public void testInsertItem() throws ClassNotFoundException, SQLException {
		DbItem item = new DbItemImpl(5, "human", 111, 222, 333);
		
		db.insertItem(item);
		db.flushItems();
		
		DbItem resultItem = db.getItem(5);
		
		Assert.assertEquals(item, resultItem);
	}	
	
	@Test
	public void test4ByteUnicodeItem() {
		DbItem item = new DbItemImpl(5, "\uD83D\uDC00", 111, 222, 333);
		
		db.insertItem(item);
		db.flushItems();
		
		DbItem resultItem = db.getItem(5);
		
		Assert.assertEquals(item, resultItem);		
	}
	
	@Test
	public void testLongLabels() {
		String longString =
				  "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ " 0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
		
		DbItem item = new DbItemImpl(5, longString, 111, 222, 333);
		
		db.insertItem(item);
		db.flushItems();
		
		DbItem resultItem = db.getItem(5);
		
		Assert.assertEquals(item, resultItem);
	}
	
	
	@Test
	public void testDeleteItems() throws ClassNotFoundException, SQLException {
		db.deleteItems();
	}
	
	@Test
	public void performanceTest() throws ClassNotFoundException, SQLException {
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < 100000; i++) {
			db.insertItem(new DbItemImpl(i, "" + i, i, i, i));
		}
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("Time (in seconds): " + (endTime - startTime) / 1000.0);
	}

}
