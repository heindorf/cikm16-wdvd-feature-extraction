package de.upb.wdqa.wdvd.features.character;

import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureFloatValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.Utils;

public class AlphanumericRatio extends FeatureImpl {
	
	// All alphanumeric characters: [a-zA-z0-9]
	private final Pattern pattern = Pattern.compile("\\p{Alnum}");

	@Override
	public FeatureFloatValue calculate(Revision revision) {
		String comment = revision.getParsedComment().getSuffixComment();
		
		float result = Utils.characterRatio(comment, pattern);
		
		return new FeatureFloatValue(result);
	}
	
}
