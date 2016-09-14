package de.upb.wdqa.wdvd.features.revision.misc;

import de.upb.wdqa.wdvd.ParsedComment;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;
import de.upb.wdqa.wdvd.features.Utils;

public class Param3 extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		ParsedComment comment = revision.getParsedComment();		
		String[] params = comment.getParameters();
		
		String result = null;
		if (params.length >= 3){
			result = params[2];
		}
		
		result = Utils.simplifyParam(result);

		return new FeatureStringValue(result);
	}

}
