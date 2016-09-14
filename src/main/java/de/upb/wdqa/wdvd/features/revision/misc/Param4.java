package de.upb.wdqa.wdvd.features.revision.misc;

import de.upb.wdqa.wdvd.ParsedComment;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;
import de.upb.wdqa.wdvd.features.Utils;

public class Param4 extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		ParsedComment comment = revision.getParsedComment();		
		String[] params = comment.getParameters();
		
		String param4 = null;
		if (params.length >= 4){
			param4 = params[3];
		}
		
		String result = param4;
		result = Utils.simplifyParam(result);

		return new FeatureStringValue(param4);
	}

}
