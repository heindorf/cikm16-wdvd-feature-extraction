package de.upb.wdqa.wdvd.features.word;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class ContainsURL extends FeatureImpl {
	private final static Pattern pattern;
	
	static {
		pattern = Pattern.compile("\\b(https?:\\/\\/|www\\.)\\S{10}.*", Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.CANON_EQ);
	}
	
	private final Matcher matcher = pattern.matcher("");
	
	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		String text = revision.getParsedComment().getSuffixComment();
		
		boolean result = false;
		if (text != null) {
			text = text.trim();

			result = matcher.reset(text).matches();
		}
		return new FeatureBooleanValue(result);
	}

}
