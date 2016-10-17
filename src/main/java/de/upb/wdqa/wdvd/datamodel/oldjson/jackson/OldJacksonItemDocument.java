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

package de.upb.wdqa.wdvd.datamodel.oldjson.jackson;

import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.deserializers.OldAliasesDeserializer;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.deserializers.OldLabelsDescriptionsDeserializer;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.deserializers.OldSitelinksDeserializer;

public class OldJacksonItemDocument {
	private LinkedHashMap<String, String> labels;
	private LinkedHashMap<String, String> descriptions;
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
