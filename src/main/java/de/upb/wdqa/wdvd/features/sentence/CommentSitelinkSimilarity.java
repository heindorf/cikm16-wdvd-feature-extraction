package de.upb.wdqa.wdvd.features.sentence;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.SiteLink;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureFloatValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class CommentSitelinkSimilarity extends FeatureImpl {
	
	private static String getEnglishSitelink(ItemDocument itemDocument){
		String result = null;
		
		if (itemDocument != null){
			Map<String, SiteLink> map = itemDocument.getSiteLinks();
			
			SiteLink sitelink = map.get("enwiki");
			
			if (sitelink != null){
				result = sitelink.getPageTitle();
			}
		}
			
		return result;		
	}
	
	@Override
	public FeatureFloatValue calculate(Revision revision) {
		Float result = null;
		
		String suffixComment = revision.getParsedComment().getSuffixComment();
		
		if(suffixComment != null){

		
			ItemDocument itemDocument = revision.getItemDocument();
			
			String englishSitelink = getEnglishSitelink(itemDocument);

	
			if(englishSitelink != null){
				englishSitelink = englishSitelink.trim();
				suffixComment = suffixComment.trim();
				
				result = (float) StringUtils.getJaroWinklerDistance(englishSitelink, suffixComment);
			}	
		}		

		return new FeatureFloatValue(result);
	}

}