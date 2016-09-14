package de.upb.wdqa.wdvd.features.item.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.implementation.ItemDocumentDbItem;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureStringValue;

public class EnglishItemLabel extends FeatureImpl {
	
	static final Logger logger = LoggerFactory.getLogger(LatestEnglishItemLabel.class);

	@Override
	public FeatureStringValue calculate(Revision revision) {
		String result = null;

		ItemDocument itemDocument = revision.getItemDocument();			

		if(itemDocument != null){
			ItemDocumentDbItem item = new ItemDocumentDbItem(itemDocument);
			
			result = item.getLabel();
		}
		
		return new FeatureStringValue(result);
	}

}
