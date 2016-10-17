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
		return new FeatureBooleanValue(
				userSet.strContains(revision.getContributor()));
	}
	
	public static boolean isGlobalBot(String contributor){
		return userSet.contains(contributor);		
	}

}
