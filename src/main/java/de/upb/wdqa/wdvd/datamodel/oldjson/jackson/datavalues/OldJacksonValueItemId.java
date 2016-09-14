package de.upb.wdqa.wdvd.datamodel.oldjson.jackson.datavalues;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OldJacksonValueItemId extends OldJacksonValue {
	

	private String entityType;
	

	private int numericId;

	@JsonProperty("entity-type")
	public String getEntityType() {
		return entityType;
	}

	@JsonProperty("entity-type")
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	@JsonProperty("numeric-id")
	public int getNumericId() {
		return numericId;
	}

	@JsonProperty("numeric-id")
	public void setNumericId(int numericId) {
		this.numericId = numericId;
	}
}
