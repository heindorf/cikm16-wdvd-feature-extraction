package de.upb.wdqa.wdvd.features.meta;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;

public class Timestamp extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		return new FeatureStringValue(revision.getTimeStamp());
	}

}
