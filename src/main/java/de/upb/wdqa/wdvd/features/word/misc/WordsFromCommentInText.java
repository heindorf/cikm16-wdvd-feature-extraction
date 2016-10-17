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

package de.upb.wdqa.wdvd.features.word.misc;

import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class WordsFromCommentInText extends FeatureImpl {
	private static final Pattern pattern;
	
	static {
		pattern = Pattern.compile("\\s+");
	}
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		Integer result = null;
		
		Revision prevRevision = revision.getPreviousRevision();
	
		if (prevRevision != null) {
		
			String suffixComment = revision.getParsedComment().getSuffixComment();
		
		
			if (suffixComment != null) {
				String[] words = pattern.split(suffixComment.trim());
			
				if (words.length > 0) {		
					result = 0;
				}
	
				for (String word: words) {
					
					word = word.trim();
					if (!word.equals("") && prevRevision.getText().contains(word)) {
						result++;
					}
				}
			}
		}

		return new FeatureIntegerValue(result);
	
	}

}
