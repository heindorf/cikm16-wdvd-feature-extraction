package de.upb.wdqa.wdvd.features.statement;

import de.upb.wdqa.wdvd.ParsedComment;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;

public class ItemValue extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		ParsedComment comment = revision.getParsedComment();	
		
		String result = comment.getItemValue();
		
		return new FeatureStringValue(result);
	}

}
