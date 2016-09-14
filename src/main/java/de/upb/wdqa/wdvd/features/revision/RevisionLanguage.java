package de.upb.wdqa.wdvd.features.revision;

import de.upb.wdqa.wdvd.ParsedComment;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;

public class RevisionLanguage extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		ParsedComment comment = revision.getParsedComment();		
		String[] params = comment.getParameters();
		
		String result = null;
		if (params.length >= 2){
			result = params[1];
			
		}

		return new FeatureStringValue(result);
	}

}
