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
