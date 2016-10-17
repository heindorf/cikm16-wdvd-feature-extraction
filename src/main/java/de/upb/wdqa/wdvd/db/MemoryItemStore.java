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

package de.upb.wdqa.wdvd.db;

import java.util.HashMap;

import de.upb.wdqa.wdvd.db.implementation.DbItemImpl;
import de.upb.wdqa.wdvd.db.interfaces.DbItem;

public class MemoryItemStore implements ItemStore {
	
	//HashMap<Integer, byte[]> items;
	HashMap<Integer, DbItem> items;

	@Override
	public void connect() {
		//Do nothing
	}

	@Override
	public void deleteItems() {
		//items = new HashMap<Integer, byte[]>();
		items = new HashMap<Integer, DbItem>();
	}

	@Override
	public void insertItem(DbItem item) {
		// DbItemImpl might need much less memory than DbItem depending on the
		// implementation
		DbItemImpl itemImpl = new DbItemImpl(item);
		
		// serialization saves memory (strings are represented as UTF-8)
		//byte[] bytes = SerializationUtils.serialize(itemImpl);		
		//items.put(item.getItemId(), bytes);		
		
		items.put(item.getItemId(), itemImpl); 
	}

	@Override
	public void flushItems() {
		// Do nothing		
	}

	@Override
	public DbItem getItem(int itemId) {		
//		byte[] bytes = items.get(itemId);		
//		DbItem item = SerializationUtils.deserialize(bytes);
		
		DbItem item = items.get(itemId);

		return item;
	}

	@Override
	public void close() {
		// Do nothing		
	}

}
