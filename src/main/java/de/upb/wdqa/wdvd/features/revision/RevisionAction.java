package de.upb.wdqa.wdvd.features.revision;

import de.upb.wdqa.wdvd.ParsedComment;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;

public class RevisionAction extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		ParsedComment comment = revision.getParsedComment();
		
		return new FeatureStringValue(comment.getAction1());
	}

}
