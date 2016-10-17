/*
 * Wikidata Vandalism Detector 2016 (WDVD-2016)
 * 
 * Copyright (c) 2016 Stefan Heindorf, Martin Potthast, Benno Stein, Gregor Engels
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
		return new FeatureBooleanValue(
				userSet.strContains(revision.getContributor()));
	}

}
