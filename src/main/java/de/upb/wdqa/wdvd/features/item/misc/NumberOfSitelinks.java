package de.upb.wdqa.wdvd.features.item.misc;

import java.util.Map;

import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.SiteLink;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class NumberOfSitelinks extends FeatureImpl {
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		int result = 0;
		ItemDocument itemDocument = revision.getItemDocument();
		
		if(itemDocument != null){
			Map<String, SiteLink> sitelinks = itemDocument.getSiteLinks();
			
			if (sitelinks != null){
				result = sitelinks.size();
			}
		}

		return new FeatureIntegerValue(result);
	}

}