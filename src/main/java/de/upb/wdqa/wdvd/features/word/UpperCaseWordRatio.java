package de.upb.wdqa.wdvd.features.word;

import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureFloatValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.Utils;

public class UpperCaseWordRatio extends FeatureImpl {
	private final static Pattern pattern;
	
	static {
		pattern = Pattern.compile("\\p{Lu}.*");
	}
	
	@Override
	public FeatureFloatValue calculate(Revision revision) {
		Float result = null;
		
		String suffixComment = revision.getParsedComment().getSuffixComment();	

		if(suffixComment != null){
			result = Utils.wordRatio(suffixComment, pattern);	
		}		

		return new FeatureFloatValue(result);
	}

}