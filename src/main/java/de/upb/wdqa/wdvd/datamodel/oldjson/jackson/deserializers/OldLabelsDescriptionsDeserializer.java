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
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class OldLabelsDescriptionsDeserializer extends
	JsonDeserializer<LinkedHashMap<String, String>> {
	
	private static final Logger logger =
			Logger.getLogger(OldLabelsDescriptionsDeserializer.class);
	
	
	@Override
	public LinkedHashMap<String, String> deserialize(JsonParser jp,
			DeserializationContext ctxt) throws IOException,
			JsonProcessingException {

		LinkedHashMap<String, String> result = null;

		// Is the alias broken, i.e., it starts with '['
		if (jp.getCurrentToken().equals(JsonToken.START_ARRAY)) {
			result = new LinkedHashMap<String, String>();
			jp.nextToken();
			if (!jp.getCurrentToken().equals(JsonToken.END_ARRAY)) {
				logger.warn("Token " + JsonToken.END_ARRAY + " expected");
			}			
		} else {
			ObjectCodec mapper = jp.getCodec();
			result = mapper.readValue(jp,
					new TypeReference<LinkedHashMap<String, String>>() { });
		}

		return result;
	}

}
