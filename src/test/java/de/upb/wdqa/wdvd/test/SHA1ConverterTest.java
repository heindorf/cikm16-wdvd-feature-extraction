package de.upb.wdqa.wdvd.test;


import org.junit.Assert;
import org.junit.Test;

import de.upb.wdqa.wdvd.revisiontags.SHA1Converter;

public class SHA1ConverterTest {
	
   @Test
   public void testSHA1Converter(){
	   String strNull = null;
	   String strEmpty = "";
	   String base16_1 = "f5c51abf1b5f1812f2af898d9f2cc3dc875ca5c3";
	   String base16_2 = "43a539d89deb9184148007d9256a23f21cafe3b3";
	   String base16Zero = "00c51abf1b5f1812f2af898d9f2cc3dc875ca5c3";
	   
	   Assert.assertEquals(21, SHA1Converter.parseByte16(base16_1).length);
	   Assert.assertEquals(20, SHA1Converter.parseByte16(base16_2).length);

	   Assert.assertEquals(strNull, convertConvert(strNull));
	   Assert.assertEquals(strEmpty, convertConvert(strEmpty));
	   Assert.assertEquals(base16_1, convertConvert(base16_1));
	   Assert.assertEquals(base16_2, convertConvert(base16_2));
	   Assert.assertEquals(base16Zero, convertConvert(base16Zero));	   
   }
   
   private String convertConvert(String str){
	   return SHA1Converter.getBase16(SHA1Converter.parseByte16(str));	   
   }

}
