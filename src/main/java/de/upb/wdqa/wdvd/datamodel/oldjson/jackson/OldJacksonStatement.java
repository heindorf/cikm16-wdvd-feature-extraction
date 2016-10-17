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

import java.util.ArrayList;
import java.util.List;

import org.wikidata.wdtk.datamodel.interfaces.StatementRank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class OldJacksonStatement {
	private OldJacksonSnak mainsnak;
	private List<OldJacksonSnak> qualifiers = new ArrayList<OldJacksonSnak>();
	private String id;
	private StatementRank rank;
	private List<List<OldJacksonSnak>> references;
	
	@JsonProperty("m")
	public void setMainsnak(OldJacksonSnak mainsnak) {
		this.mainsnak = mainsnak;
	}
	
	@JsonProperty("m")
	public OldJacksonSnak getMainsnak() {
		return this.mainsnak;
	}
	
	@JsonProperty("q")
	public void setQualifiers(List<OldJacksonSnak> qualifiers) {
		this.qualifiers = qualifiers;
	}
	
	@JsonProperty("q")
	public List<OldJacksonSnak> getQualifiers() {
		return qualifiers;
	}
	
	@JsonProperty("g")
	public void setStatementId(String id) {
		this.id = id;
	}
	
	@JsonProperty("g")
	public String getStatementId() {
		return id;
	}
	
	@JsonDeserialize(using = OldStatementRankDeserializer.class)
	public void setRank(StatementRank rank) {
		this.rank = rank;
	}
	
	public StatementRank getRank() {
		return rank;
	}
	
	@JsonProperty("refs")
	public void setReferences(List<List<OldJacksonSnak>> references) {
		this.references = references;
	}
	
	@JsonProperty("refs")
	public List<List<OldJacksonSnak>> getReferences() {
		return references;
	}

}
