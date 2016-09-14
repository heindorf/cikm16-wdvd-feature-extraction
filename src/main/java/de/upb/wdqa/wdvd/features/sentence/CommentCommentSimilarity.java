package de.upb.wdqa.wdvd.features.sentence;

import org.apache.commons.lang3.StringUtils;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureFloatValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class CommentCommentSimilarity extends FeatureImpl {
	@Override
	public FeatureFloatValue calculate(Revision revision) {
		Float result = null;
		
		String curComment = revision.getParsedComment().getSuffixComment();
		
		Revision prevRevision = revision.getPreviousRevision();
		if((curComment != null) && (prevRevision != null)){
			String prevComment = prevRevision.getParsedComment().getSuffixComment(); 

			if (prevComment != null){
				curComment = curComment.trim();
				prevComment = prevComment.trim();
				result = (float) StringUtils.getJaroWinklerDistance(curComment, prevComment);
			}
		}		

		return new FeatureFloatValue(result);
	}

}