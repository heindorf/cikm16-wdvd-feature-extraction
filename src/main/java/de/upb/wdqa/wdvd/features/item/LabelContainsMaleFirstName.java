package de.upb.wdqa.wdvd.features.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.implementation.ItemDocumentDbItem;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.word.misc.MaleFirstNamesWordlist;

public class LabelContainsMaleFirstName extends FeatureImpl {
	
	static final Logger logger = LoggerFactory.getLogger(LabelContainsMaleFirstName.class);
	
	private final static Pattern pattern;
	static {
		List<String> tokens = new ArrayList<String>(
				Arrays.asList(MaleFirstNamesWordlist.maleFirstNames));

		String patternString = ".*\\b(" + StringUtils.join(tokens, "|")
				+ ")\\b.*";
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