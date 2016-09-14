package de.upb.wdqa.wdvd.features.sentence;

import org.apache.commons.lang3.StringUtils;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.implementation.ItemDocumentDbItem;
import de.upb.wdqa.wdvd.features.FeatureFloatValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class CommentLabelSimilarity extends FeatureImpl {
	@Override
	public FeatureFloatValue calculate(Revision revision) {
		Float result = null;
		
		String suffixComment = revision.getParsedComment().getSuffixComment();
		
		if(suffixComment != null){
			suffixComment = suffixComment.trim();
		
			ItemDocument itemDocument = revision.getItemDocument();			
	
			if(itemDocument != null){
				ItemDocumentDbItem item = new ItemDocumentDbItem(itemDocument);
				
				String label = item.getLabel();
				
				if (label != null) {
					label = label.trim();
					result = (float) StringUtils.getJaroWinklerDistance(suffixComment, label);
				}
			}	
		}		

		return new FeatureFloatValue(result);
	}

}