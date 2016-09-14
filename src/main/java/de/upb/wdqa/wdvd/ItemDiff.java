package de.upb.wdqa.wdvd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.Reference;
import org.wikidata.wdtk.datamodel.interfaces.SiteLink;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.Statement;

// Reimplementation of https://github.com/wiki-ai/wb-vandalism/blob/31d74f8a50a8c43dd446d41cafee89ada5a051f8/wb_vandalism/datasources/diff.py
public class ItemDiff {
	
	private ItemDocument oldItem;
	private ItemDocument newItem;
	
	public ItemDiff (ItemDocument oldItem, ItemDocument newItem){
		this.oldItem = oldItem;
		this.newItem = newItem;
	}
	
	public ItemDocument getOldItem(){
		return oldItem;
	}
	
	public ItemDocument getNewItem(){
		return newItem;
	}
	
	public static ItemDiff getItemDiffFromRevision(Revision revision){
		ItemDiff result = null;
		if (revision.getPreviousRevision() != null && revision.getPreviousRevision().getItemDocument()!= null && revision.getItemDocument() !=null){
			result = new ItemDiff(revision.getPreviousRevision().getItemDocument(), revision.getItemDocument());
		}
		return result;
	}
	
	// based on Map<String, MonolingualTextValue>
	public MapDiff getLabelDiff(){
		MapDiff result = new MapDiff(oldItem.getLabels(), newItem.getLabels());	
		
		return result;		
	}
	
	// based on Map<Language, MonolingualTextValue> 
	public MapDiff getDescriptionDiff(){
		MapDiff result = new MapDiff(oldItem.getDescriptions(), newItem.getDescriptions());
		
		return result;
	}
	
	// based on Map<Language, List<MonolingualTextValue>> 
	public MapDiff getAliasDiff(){
		MapDiff result = new MapDiff(oldItem.getAliases(), newItem.getAliases());
		
		return result;
	}
	
    // Creates a Map<StatementId, Statement>
	private Map<Object, Statement> getStatementMap(Iterator<Statement> iter){
		HashMap<Object, Statement> map = new HashMap<Object,Statement>();
		while (iter.hasNext()){
			Statement statement = iter.next();
			
			map.put(statement.getStatementId(), statement);
		}
		
		return map;		
	}
	
	public MapDiff getClaimDiff(){
		Map<Object, Statement> oldMap = getStatementMap(oldItem.getAllStatements());
		Map<Object, Statement> newMap = getStatementMap(newItem.getAllStatements());		
		
		MapDiff result = new MapDiff(oldMap, newMap);
		
		return result;
	}
	
	
//    // Creates a Map<Property, List<Statement>>
//	private Map<Object, List<Statement>> getStatementMap2(Iterator<Statement> iter){
//		HashMap<Object, List<Statement>> map = new HashMap<Object, List<Statement>>();
//		while (iter.hasNext()){
//			Statement statement = iter.next();
//			
//			PropertyIdValue property = statement.getClaim().getMainSnak().getPropertyId();
//			
//			if (!map.containsKey(property)){
//				map.put(property, new ArrayList());
//			}
//			
//			map.get(property).add(statement);
//		}
//		
//		return map;		
//	}
//	
//	public MapDiff getClaimDiff2(){
//		Map<Object, List<Statement>> oldMap = getStatementMap2(oldItem.getAllStatements());
//		Map<Object, List<Statement>> newMap = getStatementMap2(newItem.getAllStatements());		
//		
//		MapDiff result = new MapDiff(oldMap, newMap);
//		
//		return result;
//	}
	
	
	public List<Pair<Statement, Statement>> getChangedClaims(){
		Map<Object, Statement> oldMap = getStatementMap(oldItem.getAllStatements());
		Map<Object, Statement> newMap = getStatementMap(newItem.getAllStatements());		
		
		MapDiff diff = new MapDiff(oldMap, newMap);
		
		
		Set<Object> changedKeys = diff.getChangedKeys();
		
		List<Pair<Statement, Statement>> result= new ArrayList<Pair<Statement, Statement>>();
		
		for (Object key: changedKeys){
			result.add(Pair.of(oldMap.get(key), newMap.get(key)));
		}
		
		return result;		
	}
	
	// Map based on Map<SiteID, SiteLink> 
	public MapDiff getSitelinkDiff(){
		MapDiff result = new MapDiff(oldItem.getSiteLinks(), newItem.getSiteLinks());	
		
		return result;
	}
	

	
	// Creates a Map<SourceSnakHash, SourceSnak>
	private Map<Object, Snak> getSourceMap(Iterator<Statement> statements){
		HashMap<Object, Snak> map = new HashMap<Object,Snak>();
		while (statements.hasNext()){
			Statement statement = statements.next();
			
			List<? extends Reference> references = statement.getReferences();
			for (Reference reference: references){
				Iterator<Snak> snaks = reference.getAllSnaks();
				while(snaks.hasNext()){
					Snak snak = snaks.next();
					map.put(snak.hashCode(), snak);
				}				
			}
		}
		
		return map;		
	}
	
	public MapDiff getSourceDiff(){
		Map<Object, Snak> oldMap = getSourceMap(oldItem.getAllStatements());
		Map<Object, Snak> newMap = getSourceMap(newItem.getAllStatements());		
		
		MapDiff result = new MapDiff(oldMap, newMap);
		
		return result;
	}
	
	// Creates a Map<QualifierSnakHash, QualifierSnak>
	private Map<Object, Snak> getQualifierMap(Iterator<Statement> statements){
		HashMap<Object, Snak> map = new HashMap<Object,Snak>();
		while (statements.hasNext()){
			Statement statement = statements.next();
			
			Iterator<Snak> qualifiers = statement.getClaim().getAllQualifiers();
			
			while(qualifiers.hasNext()){
				Snak qualifier = qualifiers.next();
				map.put(qualifier.hashCode(), qualifier);
			}
		}
		
		return map;	
	}
	
	public MapDiff getQualifierDiff(){
		Map<Object, Snak> oldMap = getQualifierMap(oldItem.getAllStatements());
		Map<Object, Snak> newMap = getQualifierMap(newItem.getAllStatements());		
		
		MapDiff result = new MapDiff(oldMap, newMap);
		
		return result;
	}
	
	// Creates a Map<SiteId, List<Badge>>
	private Map<String, List<String>> getBadgeMap(Map<String, SiteLink> sitelinks){
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		
		for (Map.Entry<String, SiteLink> entry: sitelinks.entrySet()){
			result.put(entry.getKey(), entry.getValue().getBadges());			
		}
		
		return result;		
	}
	
	public MapDiff getBadgeDiff(){
		Map<String, List<String>> oldMap = getBadgeMap(oldItem.getSiteLinks());
		Map<String, List<String>> newMap = getBadgeMap(newItem.getSiteLinks());		
		
		MapDiff result = new MapDiff(oldMap, newMap);
		
		return result;
	}
	
	public boolean hasPropertyChanged(String property){
		Set<String> old_statement_ids = new HashSet<String>();
		Set<String> new_statement_ids = new HashSet<String>();
		
		Iterator<Statement> oldStatements = oldItem.getAllStatements();
		while(oldStatements.hasNext()){
			Statement statement = oldStatements.next();
			if(property.equals(statement.getClaim().getMainSnak().getPropertyId().getId())){
				old_statement_ids.add(statement.getStatementId());
			}
		}
		
		Iterator<Statement> newStatements = newItem.getAllStatements();
		while(newStatements.hasNext()){
			Statement statement = newStatements.next();
			if(property.equals(statement.getClaim().getMainSnak().getPropertyId().getId())){
				new_statement_ids.add(statement.getStatementId());
			}
		}
		
		boolean result = !old_statement_ids.equals(new_statement_ids);
		
		return result;		
	}
	
}
