package de.upb.wdqa.wdvd.features.user.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsPropertyCreatorUser extends FeatureImpl {
	
	final static UserSet userSet;
	static {

		// source:
		// http://www.wikidata.org/w/index.php?title=Special%3AListUsers&username=&group=propertycreator&limit=50&uselang=en
		// last updated: December 13, 2014
		String[] propertyCreatorNames = { "Danrok", "Emw", "Fralambert",
				"GZWDer", "Ivan A. Krestinin", "Joshbaumgartner", "Kolja21",
				"MichaelSchoenitzer", "Micru", "Nightwish62", "Paperoastro",
				"PinkAmpersand", "Superm401", "Viscontino" };

		userSet = new UserSet(propertyCreatorNames);
	}	

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		return new FeatureBooleanValue(userSet.strContains(revision.getContributor()));
	}

}
