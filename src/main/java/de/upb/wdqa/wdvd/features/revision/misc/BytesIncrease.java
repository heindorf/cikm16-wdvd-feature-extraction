package de.upb.wdqa.wdvd.features.revision.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class BytesIncrease extends FeatureImpl {

	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		Integer result = null;
		
		Revision prevRevision = revision.getPreviousRevision();
		
		if (revision != null && revision.getText() != null &&
				prevRevision != null && prevRevision.getText() != null){
			
			int newLength = revision.getText().length();
			int oldLength = prevRevision.getText().length();
			
			result = newLength - oldLength;
		}		
		
		return new FeatureIntegerValue(result);
	}

}
