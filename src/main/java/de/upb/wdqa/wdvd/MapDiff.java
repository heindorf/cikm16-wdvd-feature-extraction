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

package de.upb.wdqa.wdvd;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//Reimplementation of DictDiffer
// source: https://github.com/wiki-ai/wb-vandalism/blob/31d74f8a50a8c43dd446d41cafee89ada5a051f8/wb_vandalism/datasources/diff.py
// See also: http://stackoverflow.com/questions/10586897/java-compare-two-map

@SuppressWarnings("rawtypes")
public class MapDiff {

	Map oldMap;
	Map newMap;
	
	public MapDiff(Map oldMap, Map newMap) {
		this.oldMap = oldMap;
		this.newMap = newMap;
	}
	
	@SuppressWarnings("unchecked")
	public Set<Object> getAddedKeys() {
		HashSet<Object> result = new HashSet<>(newMap.keySet());
		result.removeAll(oldMap.keySet());
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Set<Object> getRemovedKeys() {
		HashSet<Object> result = new HashSet<>(oldMap.keySet());
		result.removeAll(newMap.keySet());
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Set<Object> getChangedKeys() {
		Set<Object> keyIntersection = new HashSet<Object>(newMap.keySet());
		keyIntersection.retainAll(oldMap.keySet());
		
		HashSet<Object> result = new HashSet<>();
		
		for (Object key: keyIntersection) {
			if (!oldMap.get(key).equals(newMap.get(key))) {
				result.add(key);
			}
		}	
		
		return result;
	}
	
	
	// Assumes that the value of the map is a List
	@SuppressWarnings("unchecked")
	public Integer getNumberOfAddedListValues() {
		int numberOfValuesAdded = 0;
		
		for (Object key: this.getAddedKeys()) {
			List<Object> newListValue = ((List<Object>) newMap.get(key));
			numberOfValuesAdded += newListValue.size();
		}
		
		for (Object key: this.getChangedKeys()) {
			List<Object> newListValue = ((List<Object>) newMap.get(key));
			for (Object value: newListValue) {
				List<Object> oldListValue = ((List<Object>) oldMap.get(key));
				if (!oldListValue.contains(value)) {
					numberOfValuesAdded++;
				}
			}
		}
		
		return numberOfValuesAdded;
	}
	
	// Assumes that the value of the map is a List
	@SuppressWarnings("unchecked")
	public Integer getNumberOfRemovedListValues() {
		int numberOfValuesRemoved = 0;
		
		for (Object key: this.getRemovedKeys()) {
			List<Object> oldListValue = ((List<Object>) oldMap.get(key));
			numberOfValuesRemoved += oldListValue.size();
		}
		
		for (Object key: this.getChangedKeys()) {
			List<Object> oldListValue = ((List<Object>) oldMap.get(key));
			for (Object value: oldListValue) {
				List<Object> newListValue = ((List<Object>) newMap.get(key));
				if (!newListValue.contains(value)) {
					numberOfValuesRemoved++;
				}
			}
		}
		
		return numberOfValuesRemoved;
	}

}