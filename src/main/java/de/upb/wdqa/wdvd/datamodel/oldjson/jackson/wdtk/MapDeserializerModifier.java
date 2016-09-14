package de.upb.wdqa.wdvd.datamodel.oldjson.jackson.wdtk;

import java.util.List;

import org.wikidata.wdtk.datamodel.json.jackson.JacksonMonolingualTextValue;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonSiteLink;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonStatement;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.type.MapType;

//Workaround to cope with empty claims list, i.e. "claims": []
public class MapDeserializerModifier extends BeanDeserializerModifier {
		@Override
		public JsonDeserializer<?> modifyMapDeserializer(
				DeserializationConfig config, MapType type,
				BeanDescription beanDesc,
				JsonDeserializer<?> deserializer) {
			
			// statements
			if(isMapOfStringAndListOfStatements(type)){
				return new ModifiedMapDeserializer<String, List<JacksonStatement>>(deserializer);
			}
			// labels and descriptions
			else if(isMapOfStringAndMonolingualTextValue(type)){
				return new ModifiedMapDeserializer<String, JacksonMonolingualTextValue>(deserializer);
			}
			// sitelinks
			else if(isMapOfStringAndSitelink(type)){
				return new ModifiedMapDeserializer<String, JacksonSiteLink>(deserializer);
			}
			// aliases and miscallaneous that does not need this workaround
			else{
				return deserializer;
			}
		}
		
		private boolean isMapOfStringAndSitelink(JavaType type){
			if(!type.containedType(0).hasRawClass(String.class)){
				return false;
			}
			
			if(!type.containedType(1).hasRawClass(JacksonSiteLink.class)){
				return false;
			}
			
			return true;
			
		}
		
		private boolean isMapOfStringAndMonolingualTextValue(JavaType type){
			if(!type.containedType(0).hasRawClass(String.class)){
				return false;
			}
			
			if(!type.containedType(1).hasRawClass(JacksonMonolingualTextValue.class)){
				return false;
			}
			
			return true;
			
		}
		
		
		

	    private boolean isMapOfStringAndListOfStatements(JavaType type){
	    	if(!type.containedType(0).hasRawClass(String.class)){
	    		return false;
	    	}
	    	
	    	JavaType valueType = type.containedType(1);
	    	
	    	if(!valueType.hasRawClass(List.class)){
	    		return false;
	    	}
	    	
	    	JavaType listType = valueType.containedType(0);
	    	if(!listType.hasRawClass(JacksonStatement.class)){
	    		return false;
	    	}
	    	
	    	return true;
	    }
}
