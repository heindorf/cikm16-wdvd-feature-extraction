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

package de.upb.wdqa.wdvd.features.revision;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsLatinLanguage extends FeatureImpl {

	private final static Pattern latinPattern;
	private final static Pattern nonLatinPattern;
	
	static {
		
		/**
		 * Adding latin script language description in non-latin script
		 * https://www.wikidata.org/wiki/Special:AbuseFilter/33
		 * Last updated: November 7, 2015
		 * 
		 * Adding non-latin script language description in latin script
		 * https://www.wikidata.org/wiki/Special:AbuseFilter/48
		 * Last updated: November 7, 2015
		 */
		String latinRegex = "(af|ak|an|ang|ast|ay|az|bar|bcl|bi|bm|br|bs|ca|cbk-zam|ceb|ch|chm|cho|chy|co|crh-latn|cs|csb|cv|cy|da|de|diq|dsb|ee|eml|en|eo|es|et|eu|ff|fi|fj|fo|fr|frp|frr|fur|fy|ga|gd|gl|gn|gsw|gv|ha|haw|ho|hr|hsb|ht|hu|hz|id|ie|ig|ik|ilo|io|is|it|jbo|jv|kab|kg|ki|kj|kl|kr|ksh|ku(?!-arab\b)|kw|la|lad|lb|lg|li|lij|lmo|ln|lt|lv|map-bms|mg|mh|min?|ms|mt|mus|mwl|na|nah|nan|nap|nb|nds|nds-nl|ng|nl|nn|nov|nrm|nv|ny|oc|om|pag|pam|pap|pcd|pdc|pih|pl|pms|pt|qu|rm|rn|ro|roa-tara|rup|rw|sc|scn|sco|se|sg|sgs|sk|sl|sm|sn|so|sq|sr-el|ss|st|stq|su|sv|sw|szl|tet|tk|tl|tn|to|tpi|tr|ts|tum|tw|ty|uz|ve|vec|vi|vls|vo|vro|wa|war|wo|xh|yo|za|zea|zu)";
		String nonLatinRegex = "(ab|am|arc|ar|arz|as|ba|be|be-tarask|bg|bh|bn|bo|bpy|bxr|chr|ckb|cr|cv|dv|dz|el|fa|gan|glk|got|gu|hak|he|hi|hy|ii|iu|ja|ka|kbd|kk|km|kn|ko|koi|krc|ks|ku-arab|kv|ky|lbe|lez|lo|mai|mdf|mhr|mk|ml|mn|mo|mr|mrj|my|myv|mzn|ne|new|or|os|pa|pnb|pnt|ps|ru|rue|sa|sah|sd|si|sr|ta|te|tg|th|ti|tt|tyv|udm|ug|uk|ur|wuu|xmf|yi|zh|zh-classical|zh-hans|zh-hant|zh-tw|zh-cn|zh-hk|zh-sg)";
		
		latinPattern = Pattern.compile(latinRegex, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.CANON_EQ);
		
		nonLatinPattern = Pattern.compile(nonLatinRegex, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.CANON_EQ);
	}
	
	private final Matcher latinMatcher = latinPattern.matcher("");
	private final Matcher nonLatinMatcher = nonLatinPattern.matcher("");
	
	
	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		Boolean result = null;
		
		String[] params = revision.getParsedComment().getParameters();
		
		if (params.length > 1){
			String param2 = params[1].trim();
			
			if (latinMatcher.reset(param2).matches()){
				result = true;
			}
			
			if (nonLatinMatcher.reset(param2).matches()){
				result = false;
			}
		}
		return new FeatureBooleanValue(result);
	
	}

}
