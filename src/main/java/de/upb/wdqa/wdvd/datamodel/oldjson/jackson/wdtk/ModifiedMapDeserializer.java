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

package de.upb.wdqa.wdvd.datamodel.oldjson.jackson.wdtk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ModifiedMapDeserializer<K,V> extends StdDeserializer<Map<K,V>> implements ResolvableDeserializer, ContextualDeserializer {
	private static Logger logger = Logger.getLogger(ModifiedMapDeserializer.class);
	
	private static final long serialVersionUID = 1L;
	
	private MapDeserializer defaultDeserializer;
	
	public ModifiedMapDeserializer(JsonDeserializer<?> defaultDeserializer) {
		super((MapDeserializer)defaultDeserializer);
		this.defaultDeserializer = (MapDeserializer) defaultDeserializer;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Map<K,V> deserialize(JsonParser p,
			DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		
		Map<K,V> result = null;
			if(p.getCurrentToken().equals(JsonToken.START_ARRAY)){
				result = new HashMap<>();
				p.nextToken();
				if(!p.getCurrentToken().equals(JsonToken.END_ARRAY)){
					logger.warn("Array was not empty. Current token: " + p.getCurrentToken());
				}
			}
			else{
				Object tmp = defaultDeserializer.deserialize(p, ctxt);
				result = (Map<K,V>) tmp;
			}
		    return result;
	}	


	@Override
	public void resolve(DeserializationContext ctxt)
			throws JsonMappingException {
		defaultDeserializer.resolve(ctxt);		
	}

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
			BeanProperty property) throws JsonMappingException {
		defaultDeserializer = (MapDeserializer) defaultDeserializer.createContextual(ctxt, property);
		
		return this;
	}
}
