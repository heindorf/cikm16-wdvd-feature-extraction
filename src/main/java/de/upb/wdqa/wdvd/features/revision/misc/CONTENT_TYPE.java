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

package de.upb.wdqa.wdvd.features.revision.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum CONTENT_TYPE {
	TEXT, STATEMENT, SITELINK, MISC, DIFFERENT;
	
	static final Logger logger = LoggerFactory.getLogger(CONTENT_TYPE.class);
	
	public static CONTENT_TYPE getContentType(String action) {
		CONTENT_TYPE result;
		
		if (action == null) {
			result = CONTENT_TYPE.MISC;
		} else {
			switch (action) {
			case "wbcreateclaim":
			case "wbsetclaim":
			case "wbremoveclaims":
			case "wbsetclaimvalue":
			case "wbsetreference":
			case "wbremovereferences":
			case "wbsetqualifier":
			case "wbremovequalifiers":
				result = CONTENT_TYPE.STATEMENT;
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
				result = CONTENT_TYPE.TEXT;
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
				result = CONTENT_TYPE.MISC;
				break;
			default:
				logger.warn("Unknown content type of: " + action);
				result = CONTENT_TYPE.MISC;
			}
		}
		return result;
	}
	
	public CONTENT_TYPE combine(CONTENT_TYPE type2) {
		if (this == type2) {
			return this;
		} else {
			return CONTENT_TYPE.DIFFERENT;
		}
	}

}
