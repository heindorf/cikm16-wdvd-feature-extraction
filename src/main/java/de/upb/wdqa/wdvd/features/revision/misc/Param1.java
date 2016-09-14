package de.upb.wdqa.wdvd.features.revision.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.ParsedComment;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class Param1 extends FeatureImpl {
	
	final static Logger logger = LoggerFactory.getLogger(Param1.class);

	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		ParsedComment comment = revision.getParsedComment();		
		String[] params = comment.getParameters();
		
		Integer result = null;
		if (params.length >= 1){
			try{
				result = Integer.parseInt(params[0]);
			}
			catch(NumberFormatException e){
				logger.debug("Revision " + revision.getRevisionId() + ": param1 is not numeric: " + result + " (comment: " + comment.getText() + ")", e);
			}
		}	
		
		return new FeatureIntegerValue(result);
	}

}
