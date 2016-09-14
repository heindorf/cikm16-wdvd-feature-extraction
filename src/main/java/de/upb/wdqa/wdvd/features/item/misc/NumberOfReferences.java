package de.upb.wdqa.wdvd.features.item.misc;

import java.util.Iterator;

import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.Statement;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.FeatureIntegerValue;

public class NumberOfReferences extends FeatureImpl {
	
	@Override
	public FeatureIntegerValue calculate(Revision revision) {
		int result = 0;
		ItemDocument itemDocument = revision.getItemDocument();
		
		if(itemDocument != null){		
			Iterator<Statement> statements = itemDocument.getAllStatements();
			
			while(statements.hasNext()){
				Statement statement = statements.next();
				result += statement.getReferences().size();
			}
		}

		return new FeatureIntegerValue(result);
	}

}