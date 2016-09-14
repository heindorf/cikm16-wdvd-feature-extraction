package de.upb.wdqa.wdvd.features.revision.misc;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class ParentRevisionInCorpus extends FeatureImpl {

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		Boolean result = null;
		Revision prevRevision = revision.getPreviousRevision();
		
		if (revision.getParentId() != null && prevRevision != null){
			
			long parentId = Long.parseLong(revision.getParentId());
			long prevRevisionId = prevRevision.getRevisionId();
			
			result = (parentId == prevRevisionId);
		}		
		
		return new FeatureBooleanValue(result);
	}

}
