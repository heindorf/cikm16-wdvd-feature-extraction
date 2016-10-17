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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;

import de.upb.wdqa.wdvd.db.interfaces.DbRevision;
import de.upb.wdqa.wdvd.db.interfaces.DbTag;

public class DbRevisionImpl implements DbRevision {
	long revisionId;
	String sha1;
	Set<DbTag> tags;
	
	public DbRevisionImpl(long revisionId, String sha1, Set<DbTag> tags) {
		super();
		this.revisionId = revisionId;
		this.sha1 = sha1;
		this.tags = tags;
	}

	public DbRevisionImpl() {

	}


	@Override
	public long getRevisionId() {
		return revisionId;
	}

	@Override
	public String getSha1() {
		return sha1;
	}

	@Override
	public Set<DbTag> getTags() {
		if (tags == null){
			tags = new HashSet<DbTag>();
		}
		
		return tags;
	}
	
	@Override
	public List<String> getTagNames() {
		List<String> result = new ArrayList<String>();
		
		if(tags != null){
			for(DbTag tag: tags){
				result.add(tag.getTagName());
			}
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == this)
	        return true;
	    if (!(obj instanceof DbRevisionImpl))
	        return false;

	    DbRevisionImpl rhs = (DbRevisionImpl) obj;
	    return new EqualsBuilder().
	        append(getRevisionId(), rhs.getRevisionId()).
	        append(getSha1(), rhs.getSha1()).
	        append(getTags(), rhs.getTags()).
	        isEquals();
	}

	@Override
	public String toString(){
		return "Revision " + revisionId + " Tags: " + getTagNames();
	}
	
	public void addTag(DbTag tag){
		if(tags == null){
			tags = new HashSet<DbTag>();
		}
		tags.add(tag);
	}

}
