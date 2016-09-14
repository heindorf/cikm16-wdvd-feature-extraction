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
