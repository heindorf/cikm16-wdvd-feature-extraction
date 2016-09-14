package de.upb.wdqa.wdvd.datamodel.oldjson.jackson.deserializers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.json.jackson.datavalues.JacksonValue;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.OldJacksonNoValueSnak;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.OldJacksonSnak;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.OldJacksonSomeValueSnak;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.OldJacksonValueSnak;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.datavalues.OldJacksonValue;
import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.datavalues.OldJacksonValueItemId;


public class OldJacksonSnakDeserializer extends StdDeserializer<OldJacksonSnak> {
	
	static final Logger logger = LoggerFactory.getLogger(OldJacksonSnakDeserializer.class);
	
	private static final long serialVersionUID = 1L;


	public OldJacksonSnakDeserializer() {
		super(OldJacksonSnakDeserializer.class);
	}
	
	
	@Override
	public OldJacksonSnak deserialize(JsonParser p,
			DeserializationContext ctxt) throws IOException,
			JsonProcessingException {	
		OldJacksonSnak result = null;
		
		if(!p.getCurrentToken().equals(JsonToken.START_ARRAY)){
			logger.warn("Token " + JsonToken.START_ARRAY + " expected");
		}
		
		p.nextToken();
		String type = p.getText();
		p.nextToken();
		
		// determine type
		if (type.equals("value")){
			OldJacksonValueSnak valuesnak = new OldJacksonValueSnak();
			valuesnak.setProperty("P" + p.getIntValue());
			p.nextToken();
			valuesnak.setDatatype(p.getText());
			p.nextToken();
			
			ObjectMapper mapper = (ObjectMapper) p.getCodec();
			JsonNode root = mapper.readTree(p);
			Class<? extends OldJacksonValue> valueClass = getValueClass(valuesnak.getDatatype());
			if (valueClass != null){
				valuesnak.setDatavalue(mapper.treeToValue(root, valueClass));
			}
			
			p.nextToken();
			
			result = valuesnak;				
		}
		else if (type.equals("novalue")){
			result = new OldJacksonNoValueSnak();
			result.setProperty("P" + p.getIntValue());
			p.nextToken();
		}
		else if(type.equals("somevalue")){
			result = new OldJacksonSomeValueSnak();
			result.setProperty("P" + p.getIntValue());
			p.nextToken();
		}
		else{
			logger.warn("Unknown value type: " + type);
		}			

		if(!p.getCurrentToken().equals(JsonToken.END_ARRAY)){
			logger.warn("Token " + JsonToken.END_ARRAY + " expected");
		}

		return result;
	}
	
	
	private Class<? extends OldJacksonValue> getValueClass(String datatype)
			throws JsonMappingException {

		switch (datatype) {
		case JacksonValue.JSON_VALUE_TYPE_ENTITY_ID:
			return OldJacksonValueItemId.class;
		case JacksonValue.JSON_VALUE_TYPE_STRING:
//			return JacksonValueString.class;
			return null;	
		case JacksonValue.JSON_VALUE_TYPE_TIME:
//			return JacksonValueTime.class;
			return null;
		case JacksonValue.JSON_VALUE_TYPE_GLOBE_COORDINATES:
//			return JacksonValueGlobeCoordinates.class;
			return null;
		case JacksonValue.JSON_VALUE_TYPE_QUANTITY:
//			return JacksonValueQuantity.class;
			return null;
		case JacksonValue.JSON_VALUE_TYPE_MONOLINGUAL_TEXT:
//			return JacksonValueMonolingualText.class;
			return null;
		case "bad":
			// Some snaks have the datatype "bad".
			logger.debug("Bad data type");
			return null;
		default:
			logger.warn("Unknown data type: \"" + datatype + "\"");
			
			return null;
		}
	}
	
}
