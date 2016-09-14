package de.upb.wdqa.wdvd.features;

public class FeatureBooleanValue implements FeatureValue {
	
	private Boolean value;
	
	public FeatureBooleanValue(Boolean value){
		this.value = value;
	}

	@Override
	public String toString() {
		if(value == null){
			return FeatureValue.MISSING_VALUE_STRING;
		}
		else if(value == false){
			return "F";
		}
		else if(value== true){
			return "T";
		}
		else{
			throw new RuntimeException();
		}
	}

	public Boolean getBoolean() {
		return value;
	}	


}
