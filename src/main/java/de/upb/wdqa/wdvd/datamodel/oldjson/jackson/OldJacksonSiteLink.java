package de.upb.wdqa.wdvd.datamodel.oldjson.jackson;

import java.util.LinkedList;
import java.util.List;

public class OldJacksonSiteLink  {
	private String name;
	private List<String> badges = new LinkedList<>();
	
	
	/**
	 * This constructor is used for sitelinks *with* badges
	 */
	public OldJacksonSiteLink(){
	}
	
	/**
	 * This constructor is used for sitelinks *without* badges
	 */
	public OldJacksonSiteLink(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getBadges() {
		return badges;
	}
	
	public void setBadges(List<String> badges) {
		this.badges = badges;
	}
}
