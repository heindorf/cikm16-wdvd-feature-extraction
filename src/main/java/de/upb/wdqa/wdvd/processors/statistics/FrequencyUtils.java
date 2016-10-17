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

package de.upb.wdqa.wdvd.processors.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.math3.stat.Frequency;

public class FrequencyUtils {
	public static String formatFrequency(Frequency frequency){
		return formatFrequency(frequency, Integer.MAX_VALUE);
	}
	
	public static String formatFrequency(Frequency frequency, int maxCount){
		List<Map.Entry<Comparable<?>, Long>> list = sortByFrequency(frequency);		
		String result = "";
		
		for(int i = 0; i < maxCount && i < list.size(); i++){
			String percent = String.format(Locale.US, "%.0f",
					frequency.getPct(list.get(i).getKey()) * 100) + "%";
			
			result += list.get(i).getKey() + "," +
					list.get(i).getValue() + "," +
					percent + "\n";
		}		
		
		return result;		
	}	
	
	public static List<Map.Entry<Comparable<?>, Long>> sortByFrequency(Frequency frequency){
		Iterator<Map.Entry<Comparable<?>, Long>>  iterator =  frequency.entrySetIterator();
		
		List<Map.Entry<Comparable<?>, Long>> list = new ArrayList<Map.Entry<Comparable<?>,Long>>();
		
		while(iterator.hasNext()){
			Map.Entry<Comparable<?>, Long> entry = iterator.next();
			
			list.add(entry);			
		}
		
		Comparator<Map.Entry<Comparable<?>, Long>> comparator =
				new Comparator<Map.Entry<Comparable<?>, Long>>(){

			@Override
			public int compare(Map.Entry<Comparable<?>, Long> arg0,
					Map.Entry<Comparable<?>, Long> arg1) {
				if (arg0== null || arg1 == null){
					throw new NullPointerException();					
				}					
				
				return -Long.compare(arg0.getValue(), arg1.getValue());
			}			
		};
		
		Collections.sort(list, comparator);
		
		return list;		
	}
}
