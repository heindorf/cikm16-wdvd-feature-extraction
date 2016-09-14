package de.upb.wdqa.wdvd.features.item.misc;

import java.util.List;
import java.util.Map;

import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class NumberOfAliases extends FeatureImpl {
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {		
		int result = 0;
		ItemDocument itemDocument = revision.getItemDocument();
		
		if(itemDocument != null){
			Map<String, List<MonolingualTextValue>> aliases = itemDocument.getAliases();
		
			if (aliases != null){
				for (Map.Entry<String, List<MonolingualTextValue>> entry : aliases.entrySet()){
					result += entry.getValue().size();
				}
			}
		}

		return new FeatureIntegerValue(result);
	}
	
}