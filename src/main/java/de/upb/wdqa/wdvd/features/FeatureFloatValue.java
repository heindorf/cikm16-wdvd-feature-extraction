package de.upb.wdqa.wdvd.features;

import java.text.NumberFormat;
import java.util.Locale;

public class FeatureFloatValue implements FeatureValue {
	
	Float value;

	public FeatureFloatValue(Float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		if (value == null){
			return FeatureValue.MISSING_VALUE_STRING;
		}
		
		//NumberFormat is not thread safe. Hence it is created for every call.
		// An alternative would be to use a static variable of type ThreadLocal<NumberFormat> 
		NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);
		formatter.setMaximumFractionDigits(2);
		formatter.setGroupingUsed(false);
		
		return formatter.format(value);
	}

	public Float getFloat() {
		throw new UnsupportedOperationException();
	}

}
