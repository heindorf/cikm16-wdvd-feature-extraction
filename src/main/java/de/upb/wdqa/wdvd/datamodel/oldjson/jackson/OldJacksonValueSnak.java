package de.upb.wdqa.wdvd.datamodel.oldjson.jackson;

import de.upb.wdqa.wdvd.datamodel.oldjson.jackson.datavalues.OldJacksonValue;

public class OldJacksonValueSnak extends OldJacksonSnak {
	private String datatype;
	private OldJacksonValue datavalue;
	
	public String getDatatype() {
		return datatype;
	}
	
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	
	public OldJacksonValue getDatavalue() {
		return datavalue;
	}
	
	public void setDatavalue(OldJacksonValue datavalue) {
		this.datavalue = datavalue;
	}
	
	
}
