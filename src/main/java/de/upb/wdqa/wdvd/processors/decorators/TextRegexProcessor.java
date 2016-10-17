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

package de.upb.wdqa.wdvd.processors.decorators;

import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.word.ProportionOfLinksAdded;
import de.upb.wdqa.wdvd.features.word.ProportionOfQidAdded;
import de.upb.wdqa.wdvd.features.word.misc.ProportionOfLanguageAdded;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class TextRegexProcessor implements RevisionProcessor {
	
	static final Logger logger =
			LoggerFactory.getLogger(TextRegexProcessor.class);
	
	private boolean matchLanguages;
	
	private Matcher languageMatcher;
	private Matcher linkMatcher;
	private Matcher qidMatcher;
	
	public TextRegexProcessor(boolean matchLanguages) {
		this.matchLanguages = matchLanguages;
	}
	
	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		
		languageMatcher = ProportionOfLanguageAdded.pattern.matcher("");
		linkMatcher = ProportionOfLinksAdded.pattern.matcher("");
		qidMatcher = ProportionOfQidAdded.pattern.matcher("");		
	}

	@Override
	public void processRevision(Revision revision) {		
		int numberOfLanguageWords = 0;
		if (matchLanguages) {
			numberOfLanguageWords =
					regexCount(revision.getText(), languageMatcher);
		}
		
		int numberOfLinks = regexCount(revision.getText(), linkMatcher);
		int numberOfQids = regexCount(revision.getText(), qidMatcher);
		
		revision.getTextRegex().setNumberOfLanguageWords(numberOfLanguageWords);
		revision.getTextRegex().setNumberOfLinks(numberOfLinks);
		revision.getTextRegex().setNumberOfQids(numberOfQids);		
	}

	@Override
	public void finishRevisionProcessing() {
		logger.debug("Finished.");		
	}
	
	// Matcher is not thread safe and should only be used within one thread
	private static int regexCount(String str, Matcher matcher) {
		int count = 0;
		
		if (str != null) {
			matcher.reset(str);
			while (matcher.find()) {
				count++;
			}
		}
		
		return count;
	}

}
