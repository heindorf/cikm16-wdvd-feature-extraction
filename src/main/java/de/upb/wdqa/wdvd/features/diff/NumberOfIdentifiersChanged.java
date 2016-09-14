package de.upb.wdqa.wdvd.features.diff;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StringValue;
import org.wikidata.wdtk.datamodel.interfaces.ValueSnak;

import de.upb.wdqa.wdvd.ItemDiff;
import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class NumberOfIdentifiersChanged extends FeatureImpl {
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		Integer result = -1;
		
		ItemDiff itemDiff = ItemDiff.getItemDiffFromRevision(revision);
		
		if (itemDiff != null){					
			result = getNumberOfIdentifiersChanged(itemDiff);
//			System.out.println(result);
		}
		
		return new FeatureIntegerValue(result);
	}
	
	// Returns the number of changed claims whose target/object is of type 'string'
	private int getNumberOfIdentifiersChanged(ItemDiff itemDiff){
	
		List<Pair<Statement, Statement>>  changedClaims = itemDiff.getChangedClaims();
		
		int counter = 0;
	    for (Pair<Statement, Statement> changedClaim: changedClaims){
	    	Statement oldClaim = changedClaim.getLeft();
	    	
	    	Snak snak = oldClaim.getClaim().getMainSnak();
	    	if (snak instanceof ValueSnak){
	    		if (((ValueSnak)snak).getValue() instanceof StringValue){
		    		counter++;
		    	}	    		
	    	}
	    	
	    	
	    }
	    return counter;
	}
}