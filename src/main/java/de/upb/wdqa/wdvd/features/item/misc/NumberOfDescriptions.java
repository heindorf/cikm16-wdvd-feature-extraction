package de.upb.wdqa.wdvd.features.item.misc;

import java.util.Map;

import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class NumberOfDescriptions extends FeatureImpl {
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		int result = 0;
		ItemDocument itemDocument = revision.getItemDocument();
		
		if(itemDocument != null){		
			Map<String, MonolingualTextValue> descriptions = itemDocument.getDescriptions();
			
			if (descriptions != null){
				result = descriptions.size();
			}
		}

		return new FeatureIntegerValue(result);
	}

}