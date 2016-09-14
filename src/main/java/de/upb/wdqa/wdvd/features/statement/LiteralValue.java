package de.upb.wdqa.wdvd.features.statement;

import de.upb.wdqa.wdvd.ParsedComment;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;

public class LiteralValue extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		ParsedComment comment = revision.getParsedComment();	
		
		String result = comment.getDataValue();
		
		return new FeatureStringValue(result);
	}

}
