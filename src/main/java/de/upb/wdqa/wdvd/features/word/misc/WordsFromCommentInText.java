package de.upb.wdqa.wdvd.features.word.misc;

import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class WordsFromCommentInText extends FeatureImpl {
	private final static Pattern pattern;
	
	static {
		pattern = Pattern.compile("\\s+");
	}
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		Integer result = null;
		
		Revision prevRevision = revision.getPreviousRevision();
	
		if (prevRevision != null){
		
			String suffixComment = revision.getParsedComment().getSuffixComment();		
		
		
			if (suffixComment != null){
				String[] words = pattern.split(suffixComment.trim());
			
				if (words.length > 0){		
					result = 0;
				}
	
				for (String word: words){
					
					word = word.trim();
					if (!word.equals("") && prevRevision.getText().contains(word)){
						result++;
					}
				}
			}		
		}

		return new FeatureIntegerValue(result);
	
	}

}
