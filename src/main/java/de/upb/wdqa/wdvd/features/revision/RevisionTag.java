package de.upb.wdqa.wdvd.features.revision;

import org.apache.commons.lang3.StringUtils;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;

public class RevisionTag extends FeatureImpl {

	@Override
	public FeatureStringValue calculate(Revision revision) {
		return new FeatureStringValue(StringUtils.join(revision.getDownloadedTags(), ","));
	}

}
