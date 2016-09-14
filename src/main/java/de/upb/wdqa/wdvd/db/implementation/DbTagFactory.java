package de.upb.wdqa.wdvd.db.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.db.interfaces.DbTag;

public class DbTagFactory {	
	static final Logger logger = LoggerFactory.getLogger(DbTagFactory.class);
	
	// the two hash maps are used to convert the tags to bitSets and vice versa
	private HashMap<String, DbTag> tagNameMap = new HashMap<>();
	private HashMap<Integer, DbTag> tagIdMap = new HashMap<>();
	
	
	// This method is synchronized because it is accessed by multiple threads
	public synchronized DbTag getTag(String tagName){
		DbTag result;
		
		if (!tagNameMap.containsKey(tagName)){
			logger.debug("Adding Tag: " + tagName);
			
			int tagId = tagNameMap.size();
			result = new DbTagImpl(tagId, tagName);
			tagNameMap.put(tagName, result);
			tagIdMap.put(tagId, result);
		}
		else{
			result = tagNameMap.get(tagName);
		}
		
		return result;		
	}
	
	public DbTag getTagById(int tagId){
		return tagIdMap.get(tagId);
	}
	
	public List<DbTag> getAllDbTags(){
		List<DbTag> list = new ArrayList<DbTag>();
		
		list.addAll(tagIdMap.values());		
		
		return list;
	}
	
	public String toString(){
		return "" + getAllDbTags();
	}
	


}
