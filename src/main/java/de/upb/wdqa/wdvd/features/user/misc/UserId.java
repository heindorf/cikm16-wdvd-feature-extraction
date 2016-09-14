package de.upb.wdqa.wdvd.features.user.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class UserId extends FeatureImpl {

	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		return new FeatureIntegerValue(revision.getContributorId());
	}

}