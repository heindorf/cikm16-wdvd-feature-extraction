package de.upb.wdqa.wdvd.features.user.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsGlobalBotUser extends FeatureImpl {

	final static UserSet userSet;
	static {
		
		// Taken from
		// http://meta.wikimedia.org/w/index.php?title=Special:GlobalUsers&offset=&limit=500&username=&group=global-bot
		// Last updated: December 28, 2014
		String[] globalBotNames = { "AHbot", "Addbot", "Aibot", "Alexbot",
				"AlleborgoBot", "AnankeBot", "ArthurBot", "AvicBot",
				"AvocatoBot", "BOT-Superzerocool", "BenzolBot",
				"BodhisattvaBot", "BotMultichill", "Broadbot", "ButkoBot",
				"CandalBot", "CarsracBot", "Chobot", "ChuispastonBot",
				"CocuBot", "D'ohBot", "DSisyphBot", "DarafshBot", "Dexbot",
				"Dinamik-bot", "DirlBot", "DixonDBot", "DragonBot", "Ebrambot",
				"EmausBot", "Escarbot", "FiriBot", "FoxBot", "Gerakibot",
				"GhalyBot", "GrouchoBot", "HRoestBot", "HerculeBot",
				"HydrizBot", "Invadibot", "JAnDbot", "JYBot", "JackieBot",
				"JhsBot", "Jotterbot", "Justincheng12345-bot", "KLBot2",
				"KamikazeBot", "LaaknorBot", "Louperibot", "Loveless",
				"Luckas-bot", "MSBOT", "MalafayaBot", "MastiBot", "MenoBot",
				"MerlIwBot", "Movses-bot", "MystBot", "NjardarBot",
				"Obersachsebot", "PixelBot", "Ptbotgourou", "RedBot",
				"Rezabot", "Ripchip Bot", "Robbot", "RobotQuistnix", "RoggBot",
				"Rubinbot", "SamoaBot", "SassoBot", "SieBot", "SilvonenBot",
				"Soulbot", "Synthebot", "Sz-iwbot", "TXiKiBoT", "Tanhabot",
				"Thijs!bot", "TinucherianBot II", "TjBot", "Ver-bot",
				"VolkovBot", "WarddrBOT", "WikitanvirBot", "Xqbot",
				"YFdyh-bot", "Zorrobot", "タチコマ robot" };

		userSet = new UserSet(globalBotNames);
	}

	@Override
	public FeatureBooleanValue calculate(Revision revision) {		
		return new FeatureBooleanValue(userSet.strContains(revision.getContributor()));
	}
	
	public static boolean isGlobalBot(String contributor){
		return userSet.contains(contributor);		
	}

}
