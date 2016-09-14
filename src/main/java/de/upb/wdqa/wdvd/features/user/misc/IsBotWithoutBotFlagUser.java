package de.upb.wdqa.wdvd.features.user.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsBotWithoutBotFlagUser extends FeatureImpl {
	
	final static UserSet userSet;
	static {
		
		// Taken from http://www.wikidata.org/wiki/Category:Bots_without_botflag
		// Last updated on December 13, 2014
		String[] botsWithoutBotFlagNames = { "1VeertjeBot", "AGbot", "Ajrbot",
				"AlepfuBot", "AxelBot", "BernsteinBot", "Bigbossrobot",
				"BonifazWolbot", "BotNinja", "Cheers!-bot", "Csvtodata",
				"CultureBot", "Danroks bot", "DarafshBot", "DbBot",
				"Descriptioncreator", "Deskanabot", "DidymBot", "DynBot Srv2",
				"DæghrefnBot", "Dɐ-Bot", "ElphiBot", "Faebot", "Fako85bot",
				"Fatemibot", "FischBot-test", "FloBot", "GanimalBot",
				"Gerakibot", "Global Economic Map Bot", "HaroldBot",
				"HaxpettBot", "Hurricanefan25 in the storm", "IDbot",
				"InductiveBot", "InfoRobBot", "JanitorBot", "JhealdBot",
				"JohlBot", "KamikazeBot", "Kartṛ-bot", "KrattBot1", "KRLS Bot",
				"talk:KRLS Bot", "KunMilanoRobot", "LinkRecoveryBot",
				"MarmaseBot", "MastiBot", "MedalBot", "MenoBot", "MergeBot",
				"MerlBot", "Mk-II", "MuBot", "Persian Wikis Janitor Bot",
				"Rodrigo Padula (BOT)", "SaschaBot", "talk:SKbot", "SPQRobot",
				"SteinsplitterBot", "Structor", "SvebertBot",
				"Theo's Little Bot", "ThetaBot", "US National Archives bot",
				"Vadbot", "VlsergeyBot", "Wakebrdkid's bot", "Whymbot",
				"Xaris333Bot", "talk:Xaris333Bot", "Xqbot", "Yuibot",
				"Zielmicha Bot", "ÖdokBot", "레비:봇" };

		userSet = new UserSet(botsWithoutBotFlagNames);
	}

	@Override
	public FeatureBooleanValue calculate(Revision revision) {		
		return new FeatureBooleanValue(userSet.strContains(revision.getContributor()));
	}

}
