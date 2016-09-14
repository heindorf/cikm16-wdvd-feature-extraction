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
