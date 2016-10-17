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
		return new FeatureBooleanValue(
				userSet.strContains(revision.getContributor()));
	}
	
	public static boolean isRollbacker(String contributor){
		return userSet.contains(contributor);
	}	

}
