package de.upb.wdqa.wdvd.features.revision.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;

public class ContentType extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		String result = "" + CONTENT_TYPE.getContentType(revision.getParsedComment().getAction1());
		
		return new FeatureStringValue(result);
	}

}