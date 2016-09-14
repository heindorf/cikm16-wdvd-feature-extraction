package de.upb.wdqa.wdvd.datamodel.oldjson.jackson;

import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class OldJacksonEntity {
	private static final Logger logger = Logger.getLogger(OldJacksonEntity.class);
	
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
