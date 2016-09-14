package de.upb.wdqa.wdvd.features.diff;

import de.upb.wdqa.wdvd.ItemDiff;
import de.upb.wdqa.wdvd.MapDiff;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class NumberOfDescriptionsAdded extends FeatureImpl {
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		Integer result = -1;
		
		ItemDiff itemDiff = ItemDiff.getItemDiffFromRevision(revision);
		
		if (itemDiff != null){					
			MapDiff mapDiff = itemDiff.getDescriptionDiff();
			
			result = mapDiff.getAddedKeys().size();
//			System.out.println(result);
		}
		
		return new FeatureIntegerValue(result);
	}
}