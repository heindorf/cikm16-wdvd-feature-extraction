package de.upb.wdqa.wdvd.features.word;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class LongestWord extends FeatureImpl {

	private final static Pattern pattern;
	
	static {
		pattern = Pattern.compile("\\p{IsAlphabetic}+", Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.CANON_EQ);
	}
	
	private final Matcher matcher = pattern.matcher("");
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		String text = revision.getParsedComment().getSuffixComment();

		Integer max = null;
		if (text != null) {
			max = 0;
			text = text.trim();

			matcher.reset(text);

			while(matcher.find()){
//				String match = text.substring(matcher.start(), matcher.end());
				int length = matcher.end() - matcher.start();
				
				if (length > max){
					max = length;
				}
			}
		}
		return new FeatureIntegerValue(max);
	
	}

}
