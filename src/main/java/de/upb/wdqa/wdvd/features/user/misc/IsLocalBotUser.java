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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsLocalBotUser extends FeatureImpl {
	
	final static Logger logger = LoggerFactory.getLogger(IsLocalBotUser.class);
	
	final static UserSet userSet;
	static {
		// Taken from http://www.wikidata.org/wiki/Wikidata:Bots
		// Last updated on December 11, 2014
		// When updating this list, pay special attention to GPUBot because it
		// was blocked
		// String[] botnames = { "AGbot", "AHbot", "Addbot", "AinaliBot",
		// "AkkakkBot", "AlepfuBot", "Aplikasi-Bot", "AudeBot",
		// "AvocatoBot", "AyackBot", "BaseBot", "BeneBot*", "BetaBot",
		// "BinBot", "BotMultichill", "BotMultichillT", "Botapyb",
		// "Botik", "BoulaurBot", "BrackiBot", "BraveBot", "Byrialbot",
		// "CalakBot", "CennoBot", "Chembot", "Chobot", "Choboty",
		// "Citing Bot", "Cyberbot I", "D2-bot", "DSisyphBot",
		// "DangSunBot", "DangSunBot2", "DanmicholoBot", "Dcirovicbot",
		// "Dexbot", "DidymBot", "Dima st bk bot",
		// "Dipsacus fullonum bot", "DixonDBot", "Docu w. script",
		// "Dom bot", "DrTrigonBot", "DæghrefnBot", "EdinBot",
		// "EdwardsBot", "EmausBot", "Escabot", "Faebot", "FischBot",
		// "Frettiebot", "FrigidBot", "FuzzyBot", "GPUBot",
		// "GZWDer (flood)", "GrammarwareBot", "Hawk-Eye-Bot",
		// "HaxpettBot", "Hazard-Bot", "Hoo Bot", "InductiveBot",
		// "InfoRobBot", "InkoBot", "JAnDbot", "JVbot", "JWbot", "JYBot",
		// "JackieBot", "JhsBot", "KLBot2", "Kompakt-bot", "KrBot",
		// "Krdbot", "L PBot", "Legobot", "Liangent-bot",
		// "LinkRecoveryBot", "Louperibot", "MBAreaBot", "MagulBot",
		// "MahdiBot", "Makecat-bot", "MalarzBOT", "MarmaseBot",
		// "MatSuBot", "MatmaBot", "MedalBot",
		// "MediaWiki message delivery", "MerlIwBot", "Miguillen-bot",
		// "MineoBot", "Nicolas1981Bot", "Nullzerobot", "OctraBot",
		// "OrlodrimBot", "PBot", "PLbot", "Peter17-Bot",
		// "Pigsonthewing-bot", "PoulpyBot", "ProteinBoxBot",
		// "Ra-bot-nik", "ReimannBot", "Reinheitsgebot", "Revibot",
		// "Rezabot", "RoboViolet", "RobotMichiel1972", "Ruud Koot (bot)",
		// "SDrewthbot", "SKbot", "SLiuBot", "SamoaBot", "SanniBot",
		// "SbisoloBot", "ShinobiBot", "ShonagonBot", "Shuaib-bot",
		// "Sk!dbot", "Smbbot", "SpBot", "StackerBot", "Steenthbot",
		// "SteinsplitterBot", "SuccuBot", "Svenbot", "Symac bot",
		// "TambonBot", "The Anonybot", "ThieolBot", "TptBot",
		// "Translation Notification Bot", "UnderlyingBot", "VIAFbot",
		// "ValterVBot", "ViscoBot", "VollBot", "VsBot", "WYImporterBot",
		// "Whymbot", "Widar of zolo", "YasBot", "ZaBOTka", "ZedlikBot" };

		// Bots from the file "finalListOfLocalBots.txt" in the SVN
		String[] botnames = { "AGbot", "AHbot", "Addbot", "AinaliBot",
				"AkkakkBot", "AlepfuBot", "Aplikasi-Bot", "AudeBot",
				"AvocatoBot", "AyackBot", "BaseBot", "BeneBot*", "BetaBot",
				"BinBot", "BotMultichill", "BotMultichillT", "Botapyb",
				"Botik", "BoulaurBot", "BrackiBot", "BraveBot", "Byrialbot",
				"CalakBot", "CennoBot", "Chembot", "Chobot", "Choboty",
				"Citing Bot", "Cyberbot I", "D2-bot", "DSisyphBot",
				"DangSunFlood", "DangSunBot", "DangSunFlood2", "DangSunBot2",
				"DanmicholoBot", "Dcirovicbot", "Dexbot", "DidymBot",
				"Dima st bk bot", "Dipsacus fullonum bot", "DixonDBot",
				"Docu with script", "Docu w. script", "Dom bot", "DrTrigonBot",
				"DynBot Srv2", "DynamicBot Srv2", "EdinBot", "EdwardsBot",
				"ElphiBot", "EmausBot", "Escabot", "Faebot", "FischBot",
				"Frettiebot", "FrigidBot", "GPUBot", "GZWDer (flood)",
				"GrammarwareBot", "Hawk-Eye-Bot", "HaxpettBot", "Hazard-Bot",
				"Hoo Bot", "Hurricanefan25 in the storm", "InductiveBot",
				"InfoRobBot", "InkoBot", "Innocent bot", "JackieBot",
				"JAnDbot", "JVbot", "JWbot", "JYBot", "JhsBot", "KLBot2",
				"Kompakt-bot", "KrBot", "KrattBot", "Krdbot", "L PBot",
				"Legobot", "Liangent-bot", "LinkRecoveryBot", "Louperibot",
				"MBAreaBot", "MagulBot", "MahdiBot", "Makecat-bot",
				"MalarzBOT", "MarmaseBot", "MatSuBot", "MatmaBot", "MedalBot",
				"MerlBot", "MerlIwBot", "Miguillen-bot", "MineoBot",
				"Nicolas1981Bot", "Nullzerobot", "OctraBot", "OrlodrimBot",
				"PBot", "PLbot", "Peter17-Bot", "Pigsonthewing-bot",
				"PoulpyBot", "ProteinBoxBot", "Ra-bot-nik", "ReimannBot",
				"Reinheitsgebot", "Revibot", "Rezabot", "RoboViolet",
				"RobotGMwikt", "RobotMichiel1972", "Ruud Koot (bot)", "SKbot",
				"SLiuBot", "SamoaBot", "SanniBot", "SbisoloBot", "sDrewthbot",
				"SDrewthbot", "ShinobiBot", "ShonagonBot", "Shuaib-bot",
				"Sk!dbot", "smbbot", "Smbbot", "SpBot", "StackerBot",
				"Steenthbot", "SuccuBot", "Svenbot", "Symac bot", "TambonBot",
				"The Anonybot", "ThieolBot", "TptBot",
				"Translation Notification Bot", "UnderlyingBot", "VIAFBot",
				"VIAFbot", "ValterVBot", "ViscoBot", "VollBot", "VsBot",
				"WYImporterBot", "Whymbot", "Widar of zolo", "YasBot",
				"ZaBOTka", "ZedlikBot" };

		userSet = new UserSet(botnames);
	}

	@Override
	public FeatureBooleanValue calculate(Revision revision) {		
		String contributor = revision.getContributor();

		boolean result = userSet.contains(contributor);
		
//		String lowerCase = contributor.toLowerCase();		
//		if (result == false && lowerCase.endsWith("bot")){
//			logger.debug("Is this a bot? It is not listed as one: " + contributor);
//		}
		
		return new FeatureBooleanValue(result);
	}
	
	public static boolean isLocalBot(String contributor){
		return userSet.contains(contributor);		
	}

}
