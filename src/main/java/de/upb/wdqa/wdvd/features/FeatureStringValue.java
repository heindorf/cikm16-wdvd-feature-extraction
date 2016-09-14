package de.upb.wdqa.wdvd.features;

public class FeatureStringValue implements FeatureValue {
	
	private String value;
	
	public FeatureStringValue(String value){
		this.value = value;
	}

	@Override
	public String toString() {
		if(value == null){
			return FeatureValue.MISSING_VALUE_STRING;
		}

		return value;
	}
}
