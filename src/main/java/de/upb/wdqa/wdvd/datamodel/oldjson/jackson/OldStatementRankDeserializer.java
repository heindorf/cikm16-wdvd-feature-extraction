package de.upb.wdqa.wdvd.datamodel.oldjson.jackson;

import java.io.IOException;

import org.wikidata.wdtk.datamodel.interfaces.StatementRank;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class OldStatementRankDeserializer extends JsonDeserializer<StatementRank> {

	@Override
	public StatementRank deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		StatementRank result;
		String text = jp.getText();
		
		if(text!=null){
			text = text.trim();
		}
		
		switch(text){
			case "0":
				result = StatementRank.DEPRECATED;
				break;
			case "1":
				result = StatementRank.NORMAL;
				break;
			case "2":
				result = StatementRank.PREFERRED;
				break;
			default:
				result = null;
		}
		
		return result;
	}


}