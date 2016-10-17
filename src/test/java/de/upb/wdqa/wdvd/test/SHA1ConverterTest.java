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

package de.upb.wdqa.wdvd.test;


import org.junit.Assert;
import org.junit.Test;

import de.upb.wdqa.wdvd.revisiontags.SHA1Converter;

public class SHA1ConverterTest {
	
	@Test
	public void testSHA1Converter() {
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
	   
	private String convertConvert(String str) {
		return SHA1Converter.getBase16(SHA1Converter.parseByte16(str));
	}

}
