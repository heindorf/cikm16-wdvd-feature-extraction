package de.upb.wdqa.wdvd.db;

import de.upb.wdqa.wdvd.db.interfaces.DbItem;

public interface ItemStore {	
	void connect();
	void deleteItems();

	void insertItem(DbItem item);
	void flushItems();

	DbItem getItem(int itemId);	
	
	void close();	
}