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

package de.upb.wdqa.wdvd.features.user;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.user.misc.IsAdminUser;
import de.upb.wdqa.wdvd.features.user.misc.IsGlobalRollbackerUser;
import de.upb.wdqa.wdvd.features.user.misc.IsGlobalStewardUser;
import de.upb.wdqa.wdvd.features.user.misc.IsGlobalSysopUser;
import de.upb.wdqa.wdvd.features.user.misc.IsRollbackerUser;

/**
 * All users that can perform a rollback on Wikidata.
 *
 */
public class IsPrivilegedUser extends FeatureImpl {

	@Override
	public FeatureBooleanValue calculate(Revision revision) {		
		String contributor = revision.getContributor();
		if (contributor == null){
			return new FeatureBooleanValue(null);
		}		

		boolean result = isPrivilegedUser(contributor);
	
		return new FeatureBooleanValue(result);
	}
	

	public static boolean isPrivilegedUser(String contributor){
		if (contributor == null)
			return false;
		
		boolean result = IsGlobalSysopUser.isGlobalSysop(contributor)	
				|| IsGlobalRollbackerUser.isGlobalRollbacker(contributor)
				|| IsGlobalStewardUser.isGlobalSteward(contributor)
				|| IsAdminUser.isAdmin(contributor)
				|| IsRollbackerUser.isRollbacker(contributor);
				
		return result;
	}

}
