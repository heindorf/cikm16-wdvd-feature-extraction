package de.upb.wdqa.wdvd.features.revision;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class PositionWithinSession extends FeatureImpl {

	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		return new FeatureIntegerValue(revision.getPositionWithinGroup());
	}

}
