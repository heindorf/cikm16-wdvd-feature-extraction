package de.upb.wdqa.wdvd.features.revision.misc;

import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class TimeSinceLastRevision extends FeatureImpl {
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		Integer result = null;
		Revision prevRevision = revision.getPreviousRevision();
		
		if (revision != null && revision.getTimeStamp() != null &&
				prevRevision != null &&	prevRevision.getTimeStamp() != null){
			
			Calendar newTime = DatatypeConverter.parseDateTime(revision.getTimeStamp());
			Calendar oldTime = DatatypeConverter.parseDateTime(prevRevision.getTimeStamp());
			
			long timeDifference = (newTime.getTimeInMillis() - oldTime.getTimeInMillis()) / 1000;
			
			result = (int)timeDifference;
		}		
		
		return new FeatureIntegerValue(result);
	}

}
