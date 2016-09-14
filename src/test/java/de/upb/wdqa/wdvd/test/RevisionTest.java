package de.upb.wdqa.wdvd.test;

import org.junit.Assert;
import org.junit.Test;

import de.upb.wdqa.wdvd.Revision;

public class RevisionTest {
	
	@Test
	public void testRevert(){
//		String timeString = "2012-11-13T05:34:15Z";
		
		Revision revision = new Revision();
		Assert.assertFalse(revision.wasRollbackReverted());
		
		revision.setRollbackReverted(null);
		Assert.assertFalse(revision.wasRollbackReverted());
		
		revision.setRollbackReverted(true);
		Assert.assertTrue(revision.wasRollbackReverted());
	}
	
	@Test
	public void testCopy(){
		Revision revision = new Revision();
		
		Assert.assertFalse(revision.wasRollbackReverted());
		Assert.assertNull(revision.getPreviousRevision());
		Assert.assertEquals(0, revision.getRevisionGroupId());
		
		revision.setRollbackReverted(true);
		revision.setPreviousRevision(new Revision());
		revision.setRevisionGroupId(42);
		Revision copy = new Revision(revision);
		
		Assert.assertTrue(copy.wasRollbackReverted());
		Assert.assertNotNull(copy.getPreviousRevision());
		Assert.assertEquals(42, copy.getRevisionGroupId());		
	}
}
