package de.upb.wdqa.wdvd;

import org.junit.Assert;
import org.junit.Test;

import de.upb.wdqa.wdvd.ParsedComment;

public class ParsedCommentTest {

   @Test
   public void testRollbackTrue() {
	  String str1 = "Reverted edits by [[Special:Contributions/171.96.245.117|171.96.245.117]] ([[User talk:171.96.245.117|talk]]) to last revision by [[User:Dexbot|Dexbot]]";
	  Assert.assertTrue(ParsedComment.isRollback(str1));
   }
   
   @Test
   public void testRollbackFalse(){
	  String str1 = "All vandal reverted : Undid revision 55014499 by [[Special:Contributions/Kedchathuranga|Kedchathuranga]] ([[User talk:Kedchathuranga|talk]])";
	  Assert.assertFalse(ParsedComment.isRollback(str1));	   
   }
   
   @Test
   public void testUndoTrue(){
	   String str1 = "revert vandalism: Undid revision 102361613 by [[Special:Contributions/24.176.177.113|24.176.177.113]] ([[User talk:24.176.177.113|talk]])";
	   Assert.assertTrue(ParsedComment.isUndo(str1));
   }
   
   @Test
   public void testUndoFalse(){
	   // no revision id given
	   String str1 = "Undid revisions by [[Special:Contributions/Juusohe|Juusohe]] ([[User talk:Juusohe|talk]]) - this applies to the [[Q29999|Kingdom of the Netherlands]]";
	   Assert.assertFalse(ParsedComment.isUndo(str1));	   
   }
   
   @Test
   public void testRestoreTrue(){
	   String str1 = "rev. vandalism by IP 189.24.235.198; restored revision 37350798 by [[Special:Contributions/VIAFbot|VIAFbot]]";
	   Assert.assertTrue(ParsedComment.isRestore(str1));
   }
   
   @Test
   public void testRestoreFalse(){
	   //No revision id given
	   String str1 = "Restored date of death";
	   Assert.assertFalse(ParsedComment.isRestore(str1));
   }
}

