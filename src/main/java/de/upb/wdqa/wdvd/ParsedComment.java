package de.upb.wdqa.wdvd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses the comment of a revision.
 * 
 * For Wikibase comments, compare:
 *   https://github.com/wikimedia/mediawiki-extensions-Wikibase/blob/master/docs/summaries.txt
 *   https://github.com/wikimedia/mediawiki-extensions-Wikibase/blob/master/lib/includes/Summary.php
 *   https://github.com/wikimedia/mediawiki-extensions-Wikibase/blob/master/repo/includes/SummaryFormatter.php
 * 
 * 
 * For Wikimedia comments, compare:
 *    https://github.com/wikimedia/mediawiki/blob/master/languages/i18n/en.json
 * 
 * 
 *
 */
public class ParsedComment {
	final static Logger logger = LoggerFactory.getLogger(ParsedComment.class);
	
	final static Pattern ROBUST_ROLLBACK_PATTERN= Pattern.compile(
			".*\\bReverted\\s*edits\\s*by\\s*\\[\\[Special:Contributions\\/([^\\|\\]]*)\\|.*",
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	final static Pattern PRECISE_ROLLBACK_PATTERN = Pattern.compile(
			"^Reverted edits by \\[\\[Special:Contributions\\/([^\\|\\]]*)\\|\\1\\]\\] \\(\\[\\[User talk:\\1\\|talk\\]\\]\\) to last revision by \\[\\[User:([^\\|\\]]*)\\|\\2\\]\\]$");
	final static Pattern ROBUST_UNDO_PATTERN =  Pattern.compile(
			".*\\b(Undo|Undid)\\b.*revision\\s*(\\d+).*",
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	final static Pattern PRECISE_UNDO_PATTERN =  Pattern.compile(
			".*\\b(Undo|Undid) revision (\\d+) by \\[\\[Special:Contributions\\/([^|]*)\\|\\3\\]\\] \\(\\[\\[User talk:\\3\\|talk\\]\\]\\).*");
	final static Pattern ROBUST_RESTORE_PATTERN=  Pattern.compile(
			".*\\bRestored?\\b.*revision\\s*(\\d+).*",
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	final static Pattern PRECISE_RESTORE_PATTERN =  Pattern.compile(
			".*\\bRestored? revision (\\d+) by \\[\\[Special:Contributions\\/([^|]*)\\|\\2\\]\\].*");
			
	String text;	

	String action1;
	String action2;
	String[] parameters = new String[0];
	String suffixComment;
	String property;
	String dataValue;
	String itemValue;
	

	public ParsedComment(String comment){
		this.text = comment;
		
		if (comment!= null){
			if(isRollback(comment)){
				action1 = "rollback";
			}
			else if (isUndo(comment)){
				action1 = "undo";
			}
			else if (isRestore(comment)){
				action1 = "restore";
			}
			else if (isPageCreation(comment)){
				action1 = "pageCreation";
			}
			else if("".equals(comment)){
				action1 = "emptyComment";
			}
			else if(isSetPageProtection(comment)){
				action1 ="setPageProtection";
			}
			else if(isChangePageProtection(comment)){
				action1 ="changePageProtection";
			}
			else if(isRemovePageProtection(comment)){
				action1 ="removePageProtection";
			}
			else{
				boolean result = parseNormalComment(comment);
				
				if(result == false){
					action1 = "unknownCommentType";
					logger.debug("unknown comment type: " + comment);
				}
			}
		}		
	}
	
	// Parse a comment of the form /* action1-action2: param1, param2, ... */ value
	// or of the form              /* action1 */ value
	// @param comment
	// returns whether it is a normal comment, i.e., it contains /* ...*/
	private boolean parseNormalComment(String comment){
		boolean result = false;
		
		int asteriskStart = comment.indexOf("/*");
		
		// Is there something of the form /* ... */?
		if (asteriskStart != -1){
			result = true;
			

			int asteriskEnd = comment.indexOf("*/", asteriskStart);
			
			// Is the closing ... */ missing? (The comment was shortened because it was too long)
			if (asteriskEnd == -1){
				asteriskEnd = comment.length();
				suffixComment = "";
			}
			else{
				suffixComment = comment.substring(asteriskEnd+2).trim();
			}			
			
			int colon = comment.indexOf(":");
			// denotes the end of action1 or action2 respectively
			int actionsEnd;
			if (colon != -1 && colon < asteriskEnd){
				actionsEnd = colon;
			}
			else{
				actionsEnd = asteriskEnd;
			}			
			
			int hyphenPos = comment.indexOf("-");
			
			// Does the action consist of two parts?
			if (hyphenPos > -1 && hyphenPos < actionsEnd){
				action1 = comment.substring(asteriskStart+3, hyphenPos);
				action2 = comment.substring(hyphenPos+1, actionsEnd);
			}
			else{
				action1 = comment.substring(asteriskStart+3, actionsEnd);
			}

			// Are there parameters?
			if (colon != -1 && colon < asteriskEnd){
				String tmp = comment.substring(colon+1, asteriskEnd);
				tmp = tmp.trim();
				parameters = tmp.split("\\|");
			}
		}
		// There is NOT something of the form /* ... */
		else{
			suffixComment = comment;
		}
		
		property = getProperty(suffixComment);
		dataValue = getDataValue(suffixComment);
		itemValue = getItemValue(suffixComment);
		
		
		action1 = trim(action1);
		action2 = trim(action2);
		
		for(int i = 0; i < parameters.length; i++){
			parameters[i] = trim(parameters[i]);
		}
		
		return result;
	}
	
	private static String trim(String str){
		String result = str;
		if (str != null){			
			result = str.trim();
		}
		return result;
	}
	
	public static boolean isRollback(String comment){
		boolean result = false;
		
		if (comment != null){
			String tmp = comment.trim();
			//result =  tmp.startsWith("Reverted");
			
			result = ROBUST_ROLLBACK_PATTERN.matcher(tmp).matches();
			
			if (result != PRECISE_ROLLBACK_PATTERN.matcher(tmp).matches()){
				logger.debug("Robust but not precise rollback match (result = " + result + ") : " + tmp);
			}
		}
		
		return result;
	}
	
	public static boolean isUndo(String comment){
		boolean result = false;
		if (comment != null){
			String tmp = comment.trim();
			//result = (tmp.startsWith("Undid") || tmp.startsWith("Undo")) ;
			
			result = ROBUST_UNDO_PATTERN.matcher(comment).matches();
			
			if (logger.isDebugEnabled()){
				if (result != PRECISE_UNDO_PATTERN.matcher(tmp).matches()){
					logger.debug("Robust but not precise undo match (result = " + result + ") : " + tmp);
				}
			}
		}

		return result;
	}
	
	public static boolean isRestore(String comment){
		boolean result = false;
		
		if (comment != null){
			String tmp = comment.trim();
			//result = (tmp.startsWith("Restored") || tmp.startsWith("Restore"));
			
			result = ROBUST_RESTORE_PATTERN.matcher(tmp).matches();
			
			if(logger.isDebugEnabled()){
				if (result != PRECISE_RESTORE_PATTERN.matcher(tmp).matches()){
					logger.debug("Robust but not precise restore match (result = " + result + ") : " + tmp);
				}
			}

		}
		
		return result;
	}
	
	public static boolean isPageCreation(String comment){
		boolean result = false;
		
		if(comment != null){
			String tmp = comment.trim();
			result = (tmp.startsWith("Created page"));
		}
		
		return result;
	}
	
	public static boolean isSetPageProtection(String comment){
		boolean result = false;
		
		if(comment != null){
			String tmp = comment.trim();
			result = tmp.startsWith("Protected");
		}
		
		return result;
	}
	
	public static boolean isRemovePageProtection(String comment){
		boolean result = false;
		
		if(comment != null){
			String tmp = comment.trim();
			result = tmp.startsWith("Removed protection");
		}
		
		return result;
	}
	
	public static boolean isChangePageProtection(String comment){
		boolean result = false;
		
		if(comment != null){
			String tmp = comment.trim();
			result = tmp.startsWith("Changed protection");
		}
		
		return result;
	}
	
	private static String getProperty(String comment){
		String result = null;
		
		if (comment != null){
			String pattern = "[[Property:";
			
			int index1 = comment.indexOf(pattern);
			int index2 = comment.indexOf("]]", index1 + pattern.length());
			
			if (index1 != -1 && index2 != -1){
				result = comment.substring(index1 + pattern.length(), index2);
			}
		}
		
		return result;
	}
	
	private static String getDataValue(String comment){
		String result = null;
		
		if (comment != null){
			String antiPattern = "]]: [[Q";
			
			if (!comment.contains(antiPattern)){		
				String pattern = "]]: ";
				
				int index1 = comment.indexOf(pattern);
				
				if (index1 != -1 ){
					result = comment.substring(index1 + pattern.length());
				}
			}
		}
		
		return result;		
	}
	
	private static String getItemValue(String comment){
		String result = null;
		
		if (comment != null){
			String pattern = "]]: [[Q";
			
			int index1 = comment.indexOf(pattern);
			int index2 = comment.indexOf("]]", index1 + pattern.length());
			
			if (index1 != -1 && index2 !=-1){
				result = comment.substring(index1 + pattern.length(), index2);
			}
		}
		
		return result;		
	}
	
	public static String getRevertedContributor(String comment){
		String origResult = null;
		String pattern = "[[Special:Contributions/";
		int startIndex = comment.indexOf(pattern);
		int endIndex = comment.indexOf("|");
		if (endIndex > startIndex){
			origResult = comment.substring(startIndex + pattern.length(), endIndex);			
		}
		
		String result = "null";
		Matcher matcher = ROBUST_ROLLBACK_PATTERN.matcher(comment);
		if (matcher.matches()){
			result = matcher.group(1);
		}
		
		if(!result.equals(origResult)){
			logger.warn("Difference to original contributor: " + comment);
		}
				
		return result;
	}
	
	public static String getRevertedToContributor(String comment){
		String result = "null";
		Matcher matcher = PRECISE_ROLLBACK_PATTERN.matcher(comment);
		if (matcher.matches()){
			result = matcher.group(2);
		}
		return result;		
	}
	
	public static long getUndoneRevisionId(String comment){
		long result;
		
		Matcher matcher = ROBUST_UNDO_PATTERN.matcher(comment);		
		if (matcher.matches()){
			String str = matcher.group(2);
			result = Long.parseLong(str);
		}
		else{
			result = -1;
		}
		return result;
	}
	
	public static long getRestoredRevisionId(String comment){
		long result;
		
		Matcher matcher = ROBUST_RESTORE_PATTERN.matcher(comment);
		if (matcher.matches()){
			String str = matcher.group(1);
			result = Long.parseLong(str);
		}
		else{
			result = -1;
		}
		return result;
	}
	
	public String getText() {
		return text;
	}


	public String getAction1() {
		return action1;
	}

	public String getAction2() {
		return action2;
	}
	
	public String[] getParameters(){
		return parameters;
	}

	public String getSuffixComment() {
		return suffixComment;
	}
	
	public String getProperty() {
		return property;
	}

	public String getDataValue() {
		return dataValue;
	}

	public String getItemValue() {
		return itemValue;
	}

}
