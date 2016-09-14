package de.upb.wdqa.wdvd.features.character;

import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureFloatValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.Utils;

public class AsciiRatio extends FeatureImpl  {
	
	// All ASCII characters: [\x00-\x7F]
	private final Pattern pattern = Pattern.compile("\\p{ASCII}");
	
	@Override
	public FeatureFloatValue calculate(Revision revision) {
		String comment = revision.getParsedComment().getSuffixComment();
		
		Float result = Utils.characterRatio(comment, pattern);
		
		return new FeatureFloatValue(result);
	}
	
}
