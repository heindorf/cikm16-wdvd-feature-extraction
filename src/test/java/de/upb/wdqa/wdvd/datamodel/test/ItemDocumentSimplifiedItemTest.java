package de.upb.wdqa.wdvd.datamodel.test;

import org.junit.Assert;
import org.junit.Test;
import org.wikidata.wdtk.datamodel.interfaces.StatementRank;

import de.upb.wdqa.wdvd.db.implementation.ItemDocumentDbItem;

public class ItemDocumentSimplifiedItemTest {
	
	@Test
	public void testRankComparison(){
		Assert.assertTrue(ItemDocumentDbItem.firstIsHigherThanSecond(
				StatementRank.NORMAL, StatementRank.DEPRECATED));
		Assert.assertTrue(ItemDocumentDbItem.firstIsHigherThanSecond(
				StatementRank.PREFERRED, StatementRank.DEPRECATED));
		Assert.assertTrue(ItemDocumentDbItem.firstIsHigherThanSecond(
				StatementRank.PREFERRED, StatementRank.NORMAL));
		
		Assert.assertFalse(ItemDocumentDbItem.firstIsHigherThanSecond(
				StatementRank.DEPRECATED, StatementRank.NORMAL));
		Assert.assertFalse(ItemDocumentDbItem.firstIsHigherThanSecond(
				StatementRank.DEPRECATED, StatementRank.PREFERRED));
		Assert.assertFalse(ItemDocumentDbItem.firstIsHigherThanSecond(
				StatementRank.NORMAL, StatementRank.PREFERRED));
	}
	
}

