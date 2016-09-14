package de.upb.wdqa.wdvd.datamodel.oldjson.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.deserializers.OldJacksonSnakDeserializer;

@JsonDeserialize(using = OldJacksonSnakDeserializer.class)
public class OldJacksonSnak {
	private String property;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}
