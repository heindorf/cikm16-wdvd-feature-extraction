package de.upb.wdqa.wdvd.features.word;

import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureFloatValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.Utils;

public class ProportionOfQidAdded extends FeatureImpl {	
	
	public static final Pattern pattern = Pattern.compile("Q\\d{1,8}");

	@Override
	public FeatureFloatValue calculate(Revision revision) {
		double oldCount = 0.0;		
		Revision prevRevision = revision.getPreviousRevision();
		if (prevRevision != null){
			oldCount = prevRevision.getTextRegex().getNumberOfQids();
		}
		
		double newCount = revision.getTextRegex().getNumberOfQids();
		
		float result = Utils.proportion(oldCount, newCount);
		
		return new FeatureFloatValue(result);
	}

}