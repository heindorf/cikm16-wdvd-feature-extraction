package de.upb.wdqa.wdvd.features.user.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsRollbackerUser extends FeatureImpl {
	
	final static UserSet userSet;
	static {
		// source:
		// http://www.wikidata.org/w/index.php?title=Special:ListUsers&limit=500&username=&group=rollbacker&uselang=en
		// last updated: December 13, 2014
		String[] rollbackerNames = { "Abián", "Aconcagua", "Aftabuzzaman",
				"Amire80", "Andrew Gray", "Aude", "AutomaticStrikeout",
				"Avocato", "Base", "Bináris", "Bluemersen", "Brackenheim",
				"Byfserag", "Callanecc", "Cekli829", "ChongDae", "Closeapple",
				"DangSunAlt", "Danrok", "David1010", "Dereckson", "Deskana",
				"Dough4872", "Dusti", "Emaus", "Espeso", "Eurodyne", "FDMS4",
				"Faux", "Fredddie", "GZWDer", "GeorgeBarnick", "H.b.sh",
				"Haglaz", "Haplology", "Holger1959", "IXavier", "Indu",
				"It Is Me Here", "Jasper Deng", "Jayadevp13", "Jeblad",
				"JohnLewis", "Kevinhksouth", "Koavf", "KrBot", "Lakokat",
				"M4r51n", "MZMcBride", "Makecat", "Marek Mazurkiewicz",
				"Mateusz.ns", "Mediran", "Meisam", "Merlissimo", "Milad A380",
				"Morgankevinj", "NBS", "NahidSultan", "Namnguyenvn",
				"Natuur12", "Nirakka", "Osiris", "Palosirkka", "Petrb",
				"PinkAmpersand", "Powerek38", "Pratyya Ghosh",
				"PublicAmpersand", "Py4nf", "Pzoxicuvybtnrm", "Razr Nation",
				"Reach Out to the Truth", "Revi", "Rschen7754 public",
				"SHOTHA", "Silvonen", "Simeondahl", "Soap", "Steinsplitter",
				"Sumone10154", "TBrandley", "The Anonymouse", "The Herald",
				"The Rambling Man", "Tom Morris", "Totemkin", "Vacation9",
				"WTM", "Wnme", "Wylve", "XOXOXO", "Yair rand", "Yamaha5",
				"Ypnypn", "Zerabat", "Йо Асакура", "آرش", "درفش کاویانی",
				"فلورانس", "محمد عصام" };

		userSet = new UserSet(rollbackerNames);
	}
	

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		return new FeatureBooleanValue(userSet.strContains(revision.getContributor()));
	}
	
	public static boolean isRollbacker(String contributor){
		return userSet.contains(contributor);
	}	

}
