package de.upb.wdqa.wdvd.features.diff;

import de.upb.wdqa.wdvd.ItemDiff;
import de.upb.wdqa.wdvd.MapDiff;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class EnglishLabelTouched extends FeatureImpl {
	
	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		boolean result = false;
		
		ItemDiff itemDiff = ItemDiff.getItemDiffFromRevision(revision);
		
		if (itemDiff != null){
			MapDiff mapDiff = itemDiff.getLabelDiff();
			
			result = mapDiff.getChangedKeys().contains("en");
//			System.out.println(result);
		}
		
		return new FeatureBooleanValue(result);
	}

}