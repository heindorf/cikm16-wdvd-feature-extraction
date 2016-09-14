package de.upb.wdqa.wdvd.features.user.misc;


import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsAdminUser extends FeatureImpl {

	static UserSet userSet;
	
	static {
		
		// source:
		// http://www.wikidata.org/w/index.php?title=Special%3AListUsers&username=&group=sysop
		// last updated: December 13, 2014
		String[] adminNames = { "-revi", "555", "Abuse filter", "Addshore",
				"Ajraddatz", "Alan ffm", "AmaryllisGardener", "Andre Engels",
				"Andreasmperu", "Arkanosis", "Ash Crow", "Ayack", "Bene*",
				"Benoit Rochon", "Bill william compton", "Calak", "Cheers!",
				"Chrumps", "Conny", "Courcelles", "Csigabi", "Delusion23",
				"Dexbot", "Ebraminio", "ElfjeTwaalfje", "Epìdosis", "FakirNL",
				"Florn88", "Fomafix", "Gabbe", "Hahc21", "Harmonia Amanda",
				"Hazard-SJ", "Hoo man", "Inkowik", "JAn Dudík", "Jakec",
				"Jasper Deng", "Jdforrester", "Jianhui67", "Jitrixis",
				"John F. Lewis", "Jon Harald Søby", "Ladsgroup", "LadyInGrey",
				"Legoktm", "Lymantria", "Matěj Suchánek", "Multichill",
				"Mushroom", "Nikosguard", "Nizil Shah", "Nouill", "PMG",
				"Pamputt", "Pasleim", "Penn Station", "Place Clichy",
				"Raymond", "Reaper35", "Ricordisamoa", "Rippitippi", "Romaine",
				"Rschen7754", "Rzuwig", "SPQRobin", "Saehrimnir", "Sannita",
				"Scott5114", "Sjoerddebruin", "Sk!d", "Snow Blizzard",
				"Sotiale", "Soulkeeper", "Steenth", "Stryn", "Sven Manguard",
				"TCN7JM", "Taketa", "Tobias1984", "Tpt", "ValterVB", "Vogone",
				"Vyom25", "Wagino 20100516", "Whym", "YMS", "Ymblanter",
				"Zolo", "분당선M", "콩가루" };

		userSet = new UserSet(adminNames);
	}

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		return new FeatureBooleanValue(userSet.strContains(revision.getContributor()));
	}
	
	public static boolean isAdmin(String contributor){
		return userSet.contains(contributor);
	}

}
