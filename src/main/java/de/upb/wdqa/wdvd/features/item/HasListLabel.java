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

package de.upb.wdqa.wdvd.features.item;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.implementation.ItemDocumentDbItem;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class HasListLabel extends FeatureImpl {
	
	static final Logger logger = LoggerFactory.getLogger(HasListLabel.class);
	
	private static final Pattern pattern;

	static {
		// Label starts with "List"
		String patternString = "List\\b.*";
		pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.CANON_EQ);
	}

	private final Matcher matcher = pattern.matcher("");
	

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		Boolean result = null;
		
		ItemDocument itemDocument = revision.getItemDocument();		
			
		if (itemDocument != null) {
			ItemDocumentDbItem item = new ItemDocumentDbItem(itemDocument);
			
			String label = item.getLabel();
			
			if (label != null) {
				result = matcher.reset(label).matches();
			}
		}

		return new FeatureBooleanValue(result);
	}

}
