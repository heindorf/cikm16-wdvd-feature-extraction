package de.upb.wdqa.wdvd.features;

public class FeatureIntegerValue implements FeatureValue {
	Integer value;
	
	public FeatureIntegerValue(Integer value) {
		this.value = value;
	}

	@Override
	public String toString() {
		if(value == null){
			return FeatureValue.MISSING_VALUE_STRING;
		}
		
		return "" + value;
	}

	public Integer getInteger() {
		return value;
	}

}
