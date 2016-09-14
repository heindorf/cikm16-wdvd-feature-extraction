package de.upb.wdqa.wdvd.datamodel.oldjson.jackson.deserializers;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.OldAliasList;


/**
 * * It implements a workaround to cope with empty aliases being represented as
 * <code>"aliases":[]</code> despite its declaration as map and not as list or
 * array.
 *
 */
public class OldAliasesDeserializer extends
		JsonDeserializer<LinkedHashMap<String, List<String>>> {
	
	private static final Logger logger = Logger.getLogger(OldAliasesDeserializer.class);

	@Override
	public LinkedHashMap<String, List<String>> deserialize(
			JsonParser jp, DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		
		LinkedHashMap<String, List<String>> result = new LinkedHashMap<String, List<String>>();

		// Is the alias broken, i.e., it starts with '['
		if (jp.getCurrentToken().equals(JsonToken.START_ARRAY)){
			jp.nextToken();
			if(!jp.getCurrentToken().equals(JsonToken.END_ARRAY)){
				logger.warn("Token " + JsonToken.END_ARRAY + " expected");
			}			
		}
		else{
				ObjectCodec mapper = jp.getCodec();
				result = mapper.readValue(jp, new TypeReference<LinkedHashMap<String, OldAliasList>>() {});
		}

		return result;
	}
}
