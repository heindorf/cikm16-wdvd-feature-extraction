package de.upb.wdqa.wdvd.features.meta;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class GroupId extends FeatureImpl {

	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		// It might be necessary to change it to long in the future when revision ids exceed Integer.MAX_VALUE
		return new FeatureIntegerValue((int)revision.getRevisionGroupId());
	}

}
