package de.upb.wdqa.wdvd.features.diff;

import de.upb.wdqa.wdvd.ItemDiff;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class HasP21Changed extends FeatureImpl {
	
	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		boolean result = false;
		
		ItemDiff itemDiff = ItemDiff.getItemDiffFromRevision(revision);
		
		if (itemDiff != null){
			
			result = itemDiff.hasPropertyChanged("P21");
//			System.out.println(result);
		}
		
		return new FeatureBooleanValue(result);
	}

}