package de.upb.wdqa.wdvd.features.diff;

import de.upb.wdqa.wdvd.ItemDiff;
import de.upb.wdqa.wdvd.MapDiff;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class NumberOfClaimsChanged extends FeatureImpl {
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		Integer result = -1;
		
		ItemDiff itemDiff = ItemDiff.getItemDiffFromRevision(revision);
		
		if (itemDiff != null){					
			MapDiff mapDiff = itemDiff.getClaimDiff();
			
			result = mapDiff.getChangedKeys().size();
//			System.out.println(result);
		}
		
		return new FeatureIntegerValue(result);
	}
}