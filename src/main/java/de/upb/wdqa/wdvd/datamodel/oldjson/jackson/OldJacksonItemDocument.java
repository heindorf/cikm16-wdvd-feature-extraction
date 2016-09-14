package de.upb.wdqa.wdvd.datamodel.oldjson.jackson;

import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.deserializers.OldAliasesDeserializer;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.deserializers.OldLabelsDescriptionsDeserializer;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.deserializers.OldSitelinksDeserializer;

public class OldJacksonItemDocument {
	private LinkedHashMap<String,String> labels;
	private LinkedHashMap<String,String> descriptions;
	private LinkedHashMap<String, List<String>> aliases;
	private List<OldJacksonStatement> claims;
	private LinkedHashMap<String, OldJacksonSiteLink> sitelinks;
	private OldJacksonEntity entity;
	
	@JsonProperty("label")
	public LinkedHashMap<String, String> getLabels() {
		return labels;
	}
	
	@JsonProperty("label")
	@JsonDeserialize(using = OldLabelsDescriptionsDeserializer.class)
	public void setLabels(LinkedHashMap<String, String> labels) {
		this.labels = labels;
	}
	
	@JsonProperty("description")
	public LinkedHashMap<String, String> getDescriptions() {
		return descriptions;
	}
	
	@JsonProperty("description")
	@JsonDeserialize(using = OldLabelsDescriptionsDeserializer.class)
	public void setDescription(LinkedHashMap<String, String> descriptions) {
		this.descriptions = descriptions;
	}
	
	public LinkedHashMap<String, List<String>> getAliases() {
		return aliases;
	}
	
	@JsonDeserialize(using = OldAliasesDeserializer.class)
	public void setAliases(LinkedHashMap<String, List<String>> aliases) {
		this.aliases = aliases;
	}

	public List<OldJacksonStatement> getClaims() {
		return claims;
	}
	
	public void setClaims(List<OldJacksonStatement> claims) {
		this.claims = claims;
	}
	
	public OldJacksonEntity getEntity() {
		return entity;
	}
	
	public void setEntity(OldJacksonEntity entity) {
		this.entity = entity;
	}
	
	@JsonProperty("links")
	@JsonDeserialize(using = OldSitelinksDeserializer.class)
	public void setSiteLinks(LinkedHashMap<String, OldJacksonSiteLink> sitelinks) {
		this.sitelinks = sitelinks;
	}

	@JsonProperty("links")
	public LinkedHashMap<String, OldJacksonSiteLink> getSiteLinks() {
		return sitelinks;
	}

}	
