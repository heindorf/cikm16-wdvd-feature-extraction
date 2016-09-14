package de.upb.wdqa.wdvd.features.user.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsGlobalStewardUser extends FeatureImpl {

	final static UserSet userSet;
	static {
		
		// Taken from
		// http://meta.wikimedia.org/w/index.php?title=Special%3AGlobalUsers&username=&group=steward&limit=500
		// Last updated: December 28, 2014
		String[] stewardnames = { "Ajraddatz", "Avraham", "Barras", "Bencmq",
				"Bennylin", "Billinghurst", "Bsadowski1", "DerHexer", "Elfix",
				"Hoo man", "J.delanoy", "Jyothis", "M7", "MBisanz",
				"MF-Warburg", "Mardetanha", "Matanya", "Mathonius", "Melos",
				"Mentifisto", "Pundit", "Quentinv57", "QuiteUnusual",
				"Rschen7754", "Ruslik0", "SPQRobin", "Savh", "Shanmugamp7",
				"Shizhao", "Snowolf", "Tegel", "Teles", "Trijnstel", "Vituzzu",
				"Wikitanvir" };

		userSet = new UserSet(stewardnames);
	}

	@Override
	public FeatureBooleanValue calculate(Revision revision) {		
		return new FeatureBooleanValue(userSet.strContains(revision.getContributor()));
	}
	
	public static boolean isGlobalSteward(String contributor){
		return userSet.contains(contributor);
	}

}
