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

package de.upb.wdqa.wdvd.features;

import java.text.NumberFormat;
import java.util.Locale;

public class FeatureFloatValue implements FeatureValue {
	
	Float value;

	public FeatureFloatValue(Float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		if (value == null){
			return FeatureValue.MISSING_VALUE_STRING;
		}
		
		//NumberFormat is not thread safe. Hence it is created for every call.
		// An alternative would be to use a static variable of type ThreadLocal<NumberFormat> 
		NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);
		formatter.setMaximumFractionDigits(2);
		formatter.setGroupingUsed(false);
		
		return formatter.format(value);
	}

	public Float getFloat() {
		throw new UnsupportedOperationException();
	}

}
