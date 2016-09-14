package de.upb.wdqa.wdvd.features.character;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class LongestCharacterSequence extends FeatureImpl{
	
//	private final static Pattern pattern;
	
//	static {
//		// Sometimes raises an exception. There seems to be a bug in the Java regex library
//		// ==> this pattern is not used, the feature is implemented manually
//		pattern = Pattern.compile("(.)\\1*", Pattern.CASE_INSENSITIVE
//				| Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.CANON_EQ);
//	}

	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		String text = revision.getParsedComment().getSuffixComment();		

		Integer maxLength = null;
		if (text != null) {
			maxLength = 0;
			char prevCharacter ='a';
			int prevPosition = 0;
			text = text.trim();
			
			int i = 0;
			for (; i < text.length(); i++){
				char curCharacter = text.charAt(i);
				
				if (i > 0 && prevCharacter != curCharacter){
					if (i-prevPosition > maxLength){
						maxLength = i-prevPosition;
					}
					
					prevPosition = i;
				}
				
				prevCharacter = curCharacter;
			}
			
			if (i > 0){
				if (i-prevPosition > maxLength){
					maxLength = i-prevPosition;
				}
			}

//			Matcher matcher = pattern.matcher(text);
//			while(matcher.find()){
////				String match = text.substring(matcher.start(), matcher.end());
//				int length = matcher.end() - matcher.start();
//				
//				if (length > max){
//					max = length;
//				}
//			}
		}
		return new FeatureIntegerValue(maxLength);
	
	}
	
}
