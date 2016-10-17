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

package de.upb.wdqa.wdvd.features.sentence;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.SiteLink;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureFloatValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class CommentSitelinkSimilarity extends FeatureImpl {
	
	private static String getEnglishSitelink(ItemDocument itemDocument) {
		String result = null;
		
		if (itemDocument != null) {
			Map<String, SiteLink> map = itemDocument.getSiteLinks();
			
			SiteLink sitelink = map.get("enwiki");
			
			if (sitelink != null) {
				result = sitelink.getPageTitle();
			}
		}
			
		return result;		
	}
	
	@Override
	public FeatureFloatValue calculate(Revision revision) {
		Float result = null;
		
		String suffixComment = revision.getParsedComment().getSuffixComment();
		
		if (suffixComment != null) {

		
			ItemDocument itemDocument = revision.getItemDocument();
			
			String englishSitelink = getEnglishSitelink(itemDocument);

	
			if (englishSitelink != null) {
				englishSitelink = englishSitelink.trim();
				suffixComment = suffixComment.trim();
				
				result = (float) StringUtils.getJaroWinklerDistance(
						englishSitelink, suffixComment);
			}	
		}		

		return new FeatureFloatValue(result);
	}

}
