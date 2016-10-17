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

package de.upb.wdqa.wdvd.features;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	
	final static Logger logger = LoggerFactory.getLogger(Utils.class);
	
	public static String simplifyParam(String param){
		String result = param;
		
		if (param != null){
			int colon = param.indexOf(":");
			
			if (colon > -1){
				result = param.substring(0, colon);
			}
			else if (param.startsWith("Q")){
				result = "Q####";
			}
			else if (param.startsWith("[[Q")){
				result = "[[Q####]]";
			}
		}
		
		return result;
	}
	
	public static boolean isNumeric(String str)
	{
	  NumberFormat formatter = NumberFormat.getInstance();
	  ParsePosition pos = new ParsePosition(0);
	  formatter.parse(str, pos);
	  
	  return str.length() == pos.getIndex();
	}
	
	// This method might be called by different threads.
	// Hence all parameters must be thread safe (and Pattern is used instead of Matcher)
	public static float characterRatio(String str, Pattern pattern){
		double charRatio = -1.0;
		if (str != null){
			String tmp = pattern.matcher(str).replaceAll("");
			
			double digits = str.length() - tmp.length();
			charRatio = digits / (double) str.length();
		}
		
		if (charRatio > 1.0){
			logger.warn("Character Ratio (str=" + str + ", " + "pattern= " + pattern + "): " + charRatio);
		}
		
		return (float)charRatio;
	}
	
	
	
	private final static Pattern splitPattern = Pattern.compile("\\s+");
	
	public static float wordRatio(String str, Pattern pattern){
		double result = -1.0;
		
		String[] words = splitPattern.split(str.trim());
		
		if (words.length > 0){		
			result = 0;
		}
		
		Matcher matcher = pattern.matcher("");

		for (String word: words){
			
			word = word.trim();
			if (!word.equals("") && matcher.reset(word).matches()){
				result++;
			}
		}
		
		result = result / words.length;
		
		return (float)result;
	}
	
	public static float proportion(double oldCount, double newCount){
		double result = (newCount-oldCount) / (newCount + 1.0);
		return (float)result;		
	}

}
