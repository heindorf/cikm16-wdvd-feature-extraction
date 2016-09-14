package de.upb.wdqa.wdvd.features.revision.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class RevisionSize extends FeatureImpl {

	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		return new FeatureIntegerValue(revision.getText().length());
	}

}
