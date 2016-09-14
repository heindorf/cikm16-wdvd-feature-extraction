package de.upb.wdqa.wdvd.features.revision.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class MinorRevision extends FeatureImpl {

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		boolean result = revision.isMinor();
		
		return new FeatureBooleanValue(result);
	}

}
