/*
 * Wikidata Vandalism Detector 2016 (WDVD-2016)
 * 
 * Copyright (c) 2016 Stefan Heindorf, Martin Potthast, Benno Stein, Gregor Engels
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.upb.wdqa.wdvd.features.character;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class LongestCharacterSequence extends FeatureImpl {
	
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
			char prevCharacter = 'a';
			int prevPosition = 0;
			text = text.trim();
			
			int i = 0;
			for (; i < text.length(); i++) {
				char curCharacter = text.charAt(i);
				
				if (i > 0 && prevCharacter != curCharacter) {
					if (i - prevPosition > maxLength) {
						maxLength = i - prevPosition;
					}
					
					prevPosition = i;
				}
				
				prevCharacter = curCharacter;
			}
			
			if (i > 0) {
				if (i - prevPosition > maxLength) {
					maxLength = i - prevPosition;
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
