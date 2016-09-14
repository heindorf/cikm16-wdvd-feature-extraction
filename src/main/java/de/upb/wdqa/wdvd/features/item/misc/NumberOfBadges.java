package de.upb.wdqa.wdvd.features.item.misc;

import java.util.Map;

import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.SiteLink;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class NumberOfBadges extends FeatureImpl {
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		int result = 0;
		ItemDocument itemDocument = revision.getItemDocument();
		
		if(itemDocument != null){
			Map<String, SiteLink> sitelinks = itemDocument.getSiteLinks();
			
			for (Map.Entry<String, SiteLink> entry: sitelinks.entrySet()){
				SiteLink siteLink = entry.getValue();
				
				result += siteLink.getBadges().size();
			}
		}

		return new FeatureIntegerValue(result);
	}
	
}