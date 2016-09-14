package de.upb.wdqa.wdvd.features.revision;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;

public class RevisionPrevAction extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		String result = null;
		Revision prevRevision = revision.getPreviousRevision();
		
		if (prevRevision != null &&	prevRevision.getParsedComment().getAction1() != null){
			
			result = prevRevision.getParsedComment().getAction1();
		}		
		
		return new FeatureStringValue(result);
	}

}
