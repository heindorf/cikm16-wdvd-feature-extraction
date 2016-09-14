package de.upb.wdqa.wdvd.features.user.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsGlobalSysopUser extends FeatureImpl {	
	
	final static UserSet userSet;
	static {
		
		// Taken from
		// http://meta.wikimedia.org/w/index.php?title=Special%3AGlobalUsers&username=&group=global-sysop&limit=500
		// Last updated on December 28, 2014
		String[] sysopnames = { "Alan", "BRUTE", "Defender", "Glaisher",
				"Igna", "Jafeluv", "Kaganer", "Liliana-60", "Mh7kJ",
				"MoiraMoira", "PiRSquared17", "Pmlineditor", "Stryn",
				"Tiptoety", "Toto Azéro", "Vogone", "Wim b" };

		userSet = new UserSet(sysopnames);
	}

	@Override
	public FeatureBooleanValue calculate(Revision revision) {		
		return new FeatureBooleanValue(userSet.strContains(revision.getContributor()));
	}
	

	
	public static boolean isGlobalSysop(String contributor){
		return userSet.contains(contributor);
	}

}
