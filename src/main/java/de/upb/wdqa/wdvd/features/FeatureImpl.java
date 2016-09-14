package de.upb.wdqa.wdvd.features;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class FeatureImpl implements Feature {

	@Override
	public String getName() {
		// convert first character to lower case
		String result = this.getClass().getSimpleName();		
		result = Character.toLowerCase(result.charAt(0)) + result.substring(1);
		return result;
	}
	
	@Override
	public boolean equals(Object obj){
		   if (obj == null) { return false; }
		   if (obj == this) { return true; }

		   Feature rhs = (Feature) obj;
		   return new EqualsBuilder()
		                 .append(getName(), rhs.getName())
		                 .isEquals();
	}
	
	@Override
	public int hashCode() {
	       return new HashCodeBuilder().
	               append(getName()).
	               toHashCode();
	}

}
