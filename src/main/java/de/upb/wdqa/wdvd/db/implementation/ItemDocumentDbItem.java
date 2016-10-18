/*
 * Wikidata Vandalism Detector 2016 (WDVD-2016)
 * 
 * Copyright (c) 2016 Stefan Heindorf, Martin Potthast, Benno Stein, Gregor Engels
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.upb.wdqa.wdvd.db.implementation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.interfaces.Claim;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementRank;
import org.wikidata.wdtk.datamodel.interfaces.ValueSnak;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.db.interfaces.DbItem;

public class ItemDocumentDbItem implements DbItem {	
	static final Logger logger = LoggerFactory.getLogger(ItemDocumentDbItem.class);
	
	ItemDocument itemDocument;	

	public ItemDocumentDbItem(ItemDocument itemDocument) {
		this.itemDocument = itemDocument;
	}
	
	@Override
	public int getItemId() {
		return Revision.getItemIdFromString(itemDocument.getItemId().getId());
	}

	@Override
	public String getLabel() {
		String result = null;
		
		MonolingualTextValue label = itemDocument.getLabels().get("en");		
		if (label != null){				
			result = label.getText();
		}
		
		return result;
	}

	@Override
	public Integer getInstanceOfId() {		
		return getFirstValueOfPropertyWithHighestRank("P31");
	}
	
	public Set<Integer> getAllInstanceOfIds() {
		return getAllValuesOfProperty("P31");
	}
	
	@Override
	public Integer getSubclassOfId() {
		return getFirstValueOfPropertyWithHighestRank("P279");
	}

	@Override
	public Integer getPartOfId() {
		return getFirstValueOfPropertyWithHighestRank("P361");
	}

	// Finds all statements with the given property and returns the value of the first statement with the highest rank
	private Integer getFirstValueOfPropertyWithHighestRank(String property){
		Iterator<Statement> it = itemDocument.getAllStatements();
		
		Integer result = null;
		StatementRank highestStatementRank = null;
		
		while (it.hasNext()){
			Statement statement = it.next();
			StatementRank rank = statement.getRank();
			
			// Among the "instance of" statements with the highest rank, take the first one
			if(highestStatementRank == null || firstIsHigherThanSecond(rank, highestStatementRank)){
				if (property.equals(getPropertyOfStatement(statement))){				
					Integer itemIdValue = getItemIdValueOfStatement(statement);	
					
					if (itemIdValue != null){
						result = itemIdValue;
						highestStatementRank = rank;
					}
				}
			}
		}
		
		return result;		
	}
	
	private Set<Integer> getAllValuesOfProperty(String property){
		Set<Integer> result = new HashSet<Integer>();
		
		Iterator<Statement> it = itemDocument.getAllStatements();		

		while (it.hasNext()){
			Statement statement = it.next();
			
			if (property.equals(getPropertyOfStatement(statement))){
				Integer itemIdValue = getItemIdValueOfStatement(statement);
				
				if (itemIdValue != null){
					result.add(itemIdValue);
				}
			}
		}
		
		return result;		
	}
	
	
	private static Integer getItemIdValueOfStatement(Statement statement){
		Integer result = null;
		Claim claim = statement.getClaim();
		Snak snak = claim.getMainSnak();	
		
		if (snak instanceof ValueSnak){
			ValueSnak valueSnak = (ValueSnak) snak;
			
			ItemIdValue value = (ItemIdValue) valueSnak.getValue();
			String idStr = value.getId();
			
			if (idStr != null && idStr.length() > 0){
				if (!idStr.startsWith("Q")){
					throw new RuntimeException("Item should start with Q");
				}							
				result = Integer.parseInt(idStr.substring(1));
			}
		}
		
		return result;
	}
	
	private static String getPropertyOfStatement(Statement statement){
		Claim claim = statement.getClaim();
		Snak snak = claim.getMainSnak();
		
		String result = snak.getPropertyId().getId();
		return result;
	}
	
	public static boolean firstIsHigherThanSecond(StatementRank first, StatementRank second){
		return first.compareTo(second) < 0;
	}



}
