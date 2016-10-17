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

package de.upb.wdqa.wdvd.datamodel.oldjson.jackson;

import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class OldJacksonEntity {
	private static final Logger logger =
			Logger.getLogger(OldJacksonEntity.class);
	
	private String type;
	private String id;	
	
	/**
	 * 
	 * Used for entities of the form {"entity": "q183"}
	 */
	@JsonCreator
	public OldJacksonEntity(String id){
		setId(id);
	}
	
	/**
	 * 
	 * Used for entities of the form {"entity":["item",183]}
	 */
	@JsonCreator
	public OldJacksonEntity(List<String> list){
		if(list.size() != 2){
			logger.warn("List has size " + list.size());
		}
		else{
			setType(list.get(0));
			setId(list.get(1));
		}		
	}
	
	
	public String getType() {
		return type;
	}
	
	@JsonIgnore
	public void setType(String type) {
		this.type = type;
	}

	
	public String getId() {
		return id;
	}
	
	@JsonIgnore
	public void setId(String id) {
		// Normalize id
		String number = id;
		
		if(id != null && id.length() > 0){
			char prefix = id.charAt(0);

			if(prefix == 'q' || prefix == 'Q'){
				number = id.substring(1);
			}		
		}
		
		this.id = "Q" + Integer.valueOf(number);
	}
}
