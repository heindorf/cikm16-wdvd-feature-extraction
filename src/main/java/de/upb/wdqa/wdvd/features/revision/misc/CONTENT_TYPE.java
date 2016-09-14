package de.upb.wdqa.wdvd.features.revision.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum CONTENT_TYPE {
	TEXT, STATEMENT, SITELINK, MISC, DIFFERENT;
	
	static final Logger logger = LoggerFactory.getLogger(CONTENT_TYPE.class);
	
	static public CONTENT_TYPE getContentType(String action){
		CONTENT_TYPE result;
		
		if (action == null){
			result = CONTENT_TYPE.MISC;
		}
		else{
			switch(action){
			case "wbcreateclaim":
			case "wbsetclaim":
			case "wbremoveclaims":
			case "wbsetclaimvalue":
			case "wbsetreference":
			case "wbremovereferences":
			case "wbsetqualifier":
			case "wbremovequalifiers":
				result= CONTENT_TYPE.STATEMENT;
				break;
			case "wbsetsitelink":
			case "wbcreateredirect":
			case "clientsitelink":
			case "wblinktitles":
				result = CONTENT_TYPE.SITELINK;
				break;
			case "wbsetaliases":
			case "wbsetdescription":
			case "wbsetlabel":
				result= CONTENT_TYPE.TEXT;
				break;
			case "wbeditentity":
			case "wbsetentity":
			case "special":
			case "wbcreate":
			case "wbmergeitems":
			case "rollback":
			case "undo":
			case "restore":
			case "pageCreation":
			case "emptyComment":
			case "setPageProtection":
			case "changePageProtection":
			case "removePageProtection":
			case "unknownCommentType":
			case "null":
			case "":
				result= CONTENT_TYPE.MISC;
				break;
			default:
				logger.warn("Unknown content type of: " + action);
				result= CONTENT_TYPE.MISC;
			}
		}
		return result;
	}
	
	public CONTENT_TYPE combine(CONTENT_TYPE type2) {
		if (this == type2){
			return this;
		}
		else{
			return CONTENT_TYPE.DIFFERENT;
		}
	}
}
