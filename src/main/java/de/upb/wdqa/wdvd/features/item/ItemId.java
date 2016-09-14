package de.upb.wdqa.wdvd.features.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class ItemId extends FeatureImpl {

	static final Logger logger = LoggerFactory.getLogger(ItemId.class);

	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		return new FeatureIntegerValue(revision.getItemId());
	}
	
}
