package de.upb.wdqa.wdvd.features.user.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsBotUser extends FeatureImpl {
	
	final static Logger logger = LoggerFactory.getLogger(IsBotUser.class);

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		String contributor = revision.getContributor();
		if (contributor == null){
			return new FeatureBooleanValue(null);
		}		

		boolean result = isBot(revision.getContributor());
		
		return new FeatureBooleanValue(result);
	}
	
	public static boolean isBot(String contributor){
		if (contributor == null)
			return false;
		
		boolean result = IsLocalBotUser.isLocalBot(contributor)	
				|| IsGlobalBotUser.isGlobalBot(contributor)
				|| IsExtensionBotUser.isExtensionBot(contributor);
		
		String tmp = contributor.trim();
		tmp = tmp.toLowerCase();
		
//		if (result == false && tmp.endsWith("bot")){
//			logger.debug("Is this a bot? It is not listed as one: " + contributor);
//		}
				
		return result;
	}

}
