package de.upb.wdqa.wdvd.features.user.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsTranslationAdminUser extends FeatureImpl {
	final static UserSet userSet;

	static {

		// source:
		// http://www.wikidata.org/w/index.php?title=Special%3AListUsers&username=&group=translationadmin&limit=500&uselang=en
		// last updated: December 13, 2014
		String[] translationAdminNames = { "Base", "Bene*", "Beta16", "Brackenheim",
				"Chrumps", "Ebraminio", "GZWDer", "GeorgeBarnick",
				"Giftzwerg 88", "Jasper Deng", "John F. Lewis", "Kaganer",
				"Matěj Suchánek", "Michgrig", "Pasleim", "Ricordisamoa",
				"Rschen7754", "Saehrimnir", "Sjoerddebruin", "Vogone", "Whym",
				"Yair rand", "Ата", "분당선M" };

		userSet = new UserSet(translationAdminNames);
	}

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		return new FeatureBooleanValue(userSet.strContains(revision.getContributor()));
	}

}
