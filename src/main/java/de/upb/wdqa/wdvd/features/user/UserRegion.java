package de.upb.wdqa.wdvd.features.user;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;
import de.upb.wdqa.wdvd.geolocation.GeoInformation;

public class UserRegion extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		String result = null;
		GeoInformation geoInformation = revision.getGeoInformation();
		if (geoInformation != null){
			result = geoInformation.getRegionCode();
		}
		
		return new FeatureStringValue(result);		
	}

}