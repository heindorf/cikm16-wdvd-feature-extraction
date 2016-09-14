package de.upb.wdqa.wdvd.features.user;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsRegisteredUser extends FeatureImpl {

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		return new FeatureBooleanValue(revision.hasRegisteredContributor());
	}

}
