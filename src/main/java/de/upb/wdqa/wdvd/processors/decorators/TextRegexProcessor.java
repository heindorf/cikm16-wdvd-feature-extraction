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
	
	static final Logger logger = LoggerFactory.getLogger(TextRegexProcessor.class);
	
	private boolean matchLanguages;
	
	private Matcher languageMatcher;
	private Matcher linkMatcher;
	private Matcher qidMatcher;
	
	public TextRegexProcessor(boolean matchLanguages){
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
		if(matchLanguages){
			numberOfLanguageWords = regexCount(revision.getText(), languageMatcher);
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
	private static int regexCount(String str, Matcher matcher ){
		int count = 0;
		
		if (str != null){
			matcher.reset(str);
			while(matcher.find()){
				count++;
			}
		}
		
		return count;
	}

}
