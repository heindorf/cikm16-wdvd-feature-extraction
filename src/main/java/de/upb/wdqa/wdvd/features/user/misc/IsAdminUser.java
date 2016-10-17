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
		return new FeatureBooleanValue(
				userSet.strContains(revision.getContributor()));
	}
	
	public static boolean isAdmin(String contributor) {
		return userSet.contains(contributor);
	}

}
