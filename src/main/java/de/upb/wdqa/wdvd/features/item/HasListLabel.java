package de.upb.wdqa.wdvd.features.item;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.implementation.ItemDocumentDbItem;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class HasListLabel extends FeatureImpl {
	
	static final Logger logger = LoggerFactory.getLogger(HasListLabel.class);
	
	private final static Pattern pattern;

	static {
		// Label starts with "List"
		String patternString = "List\\b.*";
		pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.CANON_EQ);
	}

	private final Matcher matcher = pattern.matcher("");
	

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		Boolean result = null;
		
		ItemDocument itemDocument = revision.getItemDocument();		
			
		if(itemDocument != null){
			ItemDocumentDbItem item = new ItemDocumentDbItem(itemDocument);
			
			String label = item.getLabel();
			
			if (label != null) {
				result = matcher.reset(label).matches();
			}
		}

		return new FeatureBooleanValue(result);
	}

}