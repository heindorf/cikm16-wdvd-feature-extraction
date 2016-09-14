package de.upb.wdqa.wdvd.features.item.misc;

import java.util.List;

import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.StatementGroup;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class IsLivingPerson extends FeatureImpl {
	
	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		ItemDocument itemDocument = revision.getItemDocument();
		
		boolean hasDateOfBirth = false;
		boolean hasDateOfDeath = false;
		
		if(itemDocument != null){		
			List<StatementGroup> statementGroups = itemDocument.getStatementGroups();
			
			for (StatementGroup statementGroup: statementGroups){
				String property = statementGroup.getProperty().getId();
				if(property.equals("P569")){
					hasDateOfBirth = true;
				}
				if(property.equals("P570")){
					hasDateOfDeath = true;
				}
			}
		}
		
		boolean result = hasDateOfBirth && !hasDateOfDeath;

		return new FeatureBooleanValue(result);
	}

}