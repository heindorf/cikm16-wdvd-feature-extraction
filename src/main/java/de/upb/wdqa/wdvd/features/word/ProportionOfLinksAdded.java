package de.upb.wdqa.wdvd.features.word;

import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureFloatValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.Utils;

public class ProportionOfLinksAdded extends FeatureImpl {	
	
	// Taken from ORES' implementation. However, regular expression was fixed:
	// Original expression in Python: r'https?\://|wwww\.' (escaped colon, four w)
	// New expression: see below (colon does not have to be escaped, three 3 w)
	public static final Pattern pattern = Pattern.compile("https?:\\/\\/|www\\.");
	
//	private static String optimize(String text){
//		if (text != null){
//			//optimization
//			if ((text.indexOf("http")) < 0 && (text.indexOf("www") < 0))
//				text = "";
//		}
//		
//		return text;
//	}

	@Override
	public FeatureFloatValue calculate(Revision revision) {
		double oldCount = 0.0;		
		Revision prevRevision = revision.getPreviousRevision();
		if (prevRevision != null){
			oldCount = prevRevision.getTextRegex().getNumberOfLinks();
		}
		
		double newCount = revision.getTextRegex().getNumberOfLinks();
		
		float result = Utils.proportion(oldCount, newCount);
		
		return new FeatureFloatValue(result);
	}
	
}