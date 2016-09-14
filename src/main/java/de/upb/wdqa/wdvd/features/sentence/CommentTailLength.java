package de.upb.wdqa.wdvd.features.sentence;

import de.upb.wdqa.wdvd.ParsedComment;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class CommentTailLength extends FeatureImpl {

	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		Integer result = null;
		
		ParsedComment comment = revision.getParsedComment();
		if (comment.getSuffixComment() != null){
			result = comment.getSuffixComment().length();
		}

		return new FeatureIntegerValue(result);
	}

}
