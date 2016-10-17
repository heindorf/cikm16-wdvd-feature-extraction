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

public class IsGlobalRollbackerUser extends FeatureImpl {

	final static UserSet userSet;
	static {
		
		// Taken from
		// http://meta.wikimedia.org/w/index.php?title=Special%3AGlobalUsers&username=&group=global-rollbacker&limit=500
		// Last updated: December 28, 2014
		String[] globalRollbackerNames = { ".snoopy.", "Addihockey10",
				"Ah3kal", "Alan", "Aldnonymous", "Alison", "Avicennasis",
				"Az1568", "BRUTE", "Baiji", "Beetstra", "Church of emacs",
				"Courcelles", "Dalibor Bosits", "Defender", "Deu", "EdBever",
				"Erwin", "Erwin85", "Ezarate", "Fabexplosive", "FalconL",
				"Finnrind", "Gaeser", "Glaisher", "Hazard-SJ", "Hercule",
				"Holder", "Hu12", "Hydriz", "Igna", "Iluvatar", "Incnis Mrsi",
				"Iste Praetor", "Jafeluv", "Jamesofur", "Jasper Deng",
				"JenVan", "Jorunn", "Juliancolton", "Kanjy", "Krinkle", "Leyo",
				"LlamaAl", "Lukas²³", "Maximillion Pegasus", "Mercy", "Mh7kJ",
				"Mike.lifeguard", "MoiraMoira", "Morphypnos", "Nastoshka",
				"NuclearWarfare", "Petrb", "PiRSquared17", "Pmlineditor",
				"Reder", "Restu20", "Ruy Pugliesi", "Seewolf", "Stryn",
				"Syum90", "Techman224", "Tiptoety", "Toto Azéro", "VasilievVV",
				"Vogone", "Waihorace", "Werdan7", "Wiki13", "Xqt", "Ymblanter",
				"YourEyesOnly" };

		userSet = new UserSet(globalRollbackerNames);
	}

	@Override
	public FeatureBooleanValue calculate(Revision revision) {		
		return new FeatureBooleanValue(userSet.strContains(revision.getContributor()));
	}
	
	public static boolean isGlobalRollbacker(String contributor){
		return userSet.contains(contributor);
	}

}
