package de.upb.wdqa.wdvd.features.user.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsExtensionBotUser extends FeatureImpl {

	final static UserSet userSet;	
	static {
		
		// Taken from http://www.wikidata.org/wiki/Category:Extension_bots
		// Last updated on January 03, 2015, cf., bot folder in SVN
		String[] botnames = { "127.0.0.1", "Abuse filter", "Babel AutoCreate", "FuzzyBot", "MediaWiki message delivery", "Translation Notification Bot" };
		
		userSet = new UserSet(botnames);	
	}

	@Override
	public FeatureBooleanValue calculate(Revision revision) {	
		return new FeatureBooleanValue(userSet.strContains(revision.getContributor()));
	}
	
	public static boolean isExtensionBot(String contributor){
		return userSet.contains(contributor);		
	}
	
}
