package de.upb.wdqa.wdvd.features.revision;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class CommentLength extends FeatureImpl {

	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		Integer result = null;
		
		if (revision.getComment() != null){
			result = revision.getComment().length();
		}

		return new FeatureIntegerValue(result);
	}

}
