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
