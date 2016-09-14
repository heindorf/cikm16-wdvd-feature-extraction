package de.upb.wdqa.wdvd.features.item;

import java.util.regex.Pattern;

import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.implementation.ItemDocumentDbItem;
import de.upb.wdqa.wdvd.features.FeatureFloatValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.Utils;

public class LabelCapitalizedWordRatio extends FeatureImpl {
	
	private final static Pattern pattern;	
	static {
		pattern = Pattern.compile("\\p{Lu}.*");
	}
	
	@Override
	public FeatureFloatValue calculate(Revision revision) {
		Float result = null;
		
		ItemDocument itemDocument = revision.getItemDocument();			

		if(itemDocument != null){
			ItemDocumentDbItem item = new ItemDocumentDbItem(itemDocument);
			
			String label = item.getLabel();
			
			if (label != null) {				
				result = Utils.wordRatio(label, pattern);	
			}
		}		

		return new FeatureFloatValue(result);
	}
}