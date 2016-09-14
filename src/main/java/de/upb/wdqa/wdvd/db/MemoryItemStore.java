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
		// DbItemImpl might need much less memory than DbItem depending on the implementation
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
