package de.upb.wdqa.wdvd.features.label;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class RollbackReverted extends FeatureImpl {

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		return new FeatureBooleanValue(revision.wasRollbackReverted());
	}

}
