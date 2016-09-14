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
	public void setQualifiers(List<OldJacksonSnak> qualifiers){
		this.qualifiers = qualifiers;
	}
	
	@JsonProperty("q")
	public List<OldJacksonSnak> getQualifiers(){
		return qualifiers;
	}
	
	@JsonProperty("g")
	public void setStatementId(String id){
		this.id = id;
	}
	
	@JsonProperty("g")
	public String getStatementId(){
		return id;
	}
	
	@JsonDeserialize(using = OldStatementRankDeserializer.class)
	public void setRank(StatementRank rank){
		this.rank = rank;
	}
	
	public StatementRank getRank(){
		return rank;
	}
	
	@JsonProperty("refs")
	public void setReferences(List<List<OldJacksonSnak>> references){
		this.references = references;
	}
	
	@JsonProperty("refs")
	public List<List<OldJacksonSnak>> getReferences(){
		return references;
	}
}
