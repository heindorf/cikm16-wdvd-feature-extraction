package de.upb.wdqa.wdvd.datamodel.oldjson.jackson.deserializers;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.OldJacksonSiteLink;

public class OldSitelinksDeserializer extends JsonDeserializer<LinkedHashMap<String, OldJacksonSiteLink>>{
	private static final Logger logger = Logger.getLogger(OldSitelinksDeserializer.class);
	
	@Override
	public LinkedHashMap<String, OldJacksonSiteLink> deserialize(JsonParser jp,
			DeserializationContext ctxt) throws IOException,
			JsonProcessingException {

		LinkedHashMap<String, OldJacksonSiteLink> result = null;

		// Is the alias broken, i.e., it starts with '['
		if (jp.getCurrentToken().equals(JsonToken.START_ARRAY)){
			result = new LinkedHashMap<String, OldJacksonSiteLink>();
			jp.nextToken();
			if(!jp.getCurrentToken().equals(JsonToken.END_ARRAY)){
				logger.warn("Token " + JsonToken.END_ARRAY + " expected");
			}			
		}
		else{
			ObjectCodec mapper = jp.getCodec();
			result = mapper.readValue(jp, new TypeReference<LinkedHashMap<String, OldJacksonSiteLink>>() {});
		}

		return result;
	}

}
