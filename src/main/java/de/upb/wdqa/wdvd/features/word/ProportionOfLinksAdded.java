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
		if (prevRevision != null) {
			oldCount = prevRevision.getTextRegex().getNumberOfLinks();
		}
		
		double newCount = revision.getTextRegex().getNumberOfLinks();
		
		float result = Utils.proportion(oldCount, newCount);
		
		return new FeatureFloatValue(result);
	}
	
}
