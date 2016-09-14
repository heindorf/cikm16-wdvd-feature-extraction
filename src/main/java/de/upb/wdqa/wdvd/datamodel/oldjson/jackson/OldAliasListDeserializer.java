package de.upb.wdqa.wdvd.datamodel.oldjson.jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class OldAliasListDeserializer extends JsonDeserializer<List<String>>{

	@Override
	public List<String> deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {		
		List<String> result;
	
		 ObjectCodec codec = p.getCodec();
		 

		if (p.getCurrentToken().equals(JsonToken.START_ARRAY)){
			result = codec.readValue(p, new TypeReference<List<String>>() {}) ;
		}
		else{
			LinkedHashMap<Integer, String> map = codec.readValue(p, new TypeReference<LinkedHashMap<Integer, String>>() {});
			result = new ArrayList<String>(map.values());
		}
		return result;
	}

}
