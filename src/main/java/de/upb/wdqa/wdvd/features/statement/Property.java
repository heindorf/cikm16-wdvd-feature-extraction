package de.upb.wdqa.wdvd.features.statement;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;

public class Property extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		String result = null;
		
		if (revision.getParsedComment() != null){
			result = revision.getParsedComment().getProperty();
		}

		return new FeatureStringValue(result);
	}


}
