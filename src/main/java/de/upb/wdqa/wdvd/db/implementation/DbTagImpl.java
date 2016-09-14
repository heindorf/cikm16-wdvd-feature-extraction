package de.upb.wdqa.wdvd.db.implementation;

import de.upb.wdqa.wdvd.db.interfaces.DbTag;

public class DbTagImpl implements DbTag {
	int tagId;
	String tagName;
	
	DbTagImpl(int tagId, String tagName) {
		super();
		this.tagId = tagId;
		this.tagName = tagName;
	}
	
	public int getTagId() {
		return tagId;
	}
	
	public String getTagName() {
		return tagName;
	}
	

	public int hashCode() {
		return tagId;
	}
	
	public String toString(){
		return tagName + " (id " + tagId + ")";
	}
}
