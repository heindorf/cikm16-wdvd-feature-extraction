package de.upb.wdqa.wdvd.features;

import de.upb.wdqa.wdvd.Revision;

public interface Feature {
	
	public FeatureValue calculate(Revision revision);
	
	public String getName();
	

}
