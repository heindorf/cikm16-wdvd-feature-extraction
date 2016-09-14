package de.upb.wdqa.wdvd.features.item.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.implementation.ItemDocumentDbItem;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class SuperItemId extends FeatureImpl {
	
	static final Logger logger = LoggerFactory.getLogger(LatestInstanceOfItemId.class);

	@Override
	public FeatureIntegerValue calculate(Revision revision) {		
		Integer result = null;

		ItemDocument itemDocument = revision.getItemDocument();
		if(itemDocument != null){
			ItemDocumentDbItem item = new ItemDocumentDbItem(itemDocument);
				
			result = item.getInstanceOfId();
		}

		return new FeatureIntegerValue(result);
	}

}