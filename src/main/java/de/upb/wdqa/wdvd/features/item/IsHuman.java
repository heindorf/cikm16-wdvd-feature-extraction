package de.upb.wdqa.wdvd.features.item;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.implementation.ItemDocumentDbItem;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsHuman extends FeatureImpl {
	
	static final Logger logger = LoggerFactory.getLogger(IsHuman.class);
	
	static final int HUMAN_ITEM_ID = 5;

	@Override
	public FeatureBooleanValue calculate(Revision revision) {		
		Boolean result = null;

		ItemDocument itemDocument = revision.getItemDocument();		
		
		if(itemDocument != null){
			ItemDocumentDbItem item = new ItemDocumentDbItem(itemDocument);
				
			Set<Integer> ids = item.getAllInstanceOfIds();
			if (!ids.isEmpty()){
				result = ids.contains(HUMAN_ITEM_ID);
			}			
		}
		
		return new FeatureBooleanValue(result);
	}

}