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

package de.upb.wdqa.wdvd.revisiontags;

import java.util.ArrayList;
import java.util.List;

import de.upb.wdqa.wdvd.db.implementation.DbRevisionImpl;
import de.upb.wdqa.wdvd.db.implementation.DbTagFactory;
import de.upb.wdqa.wdvd.db.interfaces.DbRevision;
import it.unimi.dsi.fastutil.bytes.ByteBigArrays;

public class TagDownloaderMemoryDataStore implements TagDownloaderDataStore {
	
	// Stores all the data from TagDownloader. The maximal size of an byte array
	// in Java is 2GB. Hence, we use a third party library to circumvent this
	// limitation.
	static byte[][] bigByteArray;
	
	DbTagFactory tagFactory;
	long maxRevisionId;
	
	public TagDownloaderMemoryDataStore(DbTagFactory tagFactory, long maxRevisionId){
		this.tagFactory = tagFactory;
		this.maxRevisionId = maxRevisionId;
	}

	@Override
	public void connect() throws Exception {
		clear();
	}

	@Override
	public void disconnect() throws Exception {
		//clear(); Clear must not be called before the end of the program		
	}

	@Override
	public void putRevision(DbRevision dbRevision) {
		TagDownloaderRevisionData data =
				new TagDownloaderRevisionData(dbRevision, tagFactory);
		
		
		byte[] bytes = data.getByteArray();
		ByteBigArrays.copyToBig(bytes, 0, bigByteArray,
				dbRevision.getRevisionId() * (long)TagDownloaderRevisionData.ELEMENT_SIZE,
				bytes.length);
		
	}

	@Override
	public DbRevision getRevision(long revisionId) {
		byte[] bytes = new byte[TagDownloaderRevisionData.ELEMENT_SIZE];
		if(bigByteArray != null){
			ByteBigArrays.copyFromBig(bigByteArray,
					(long)revisionId * (long)TagDownloaderRevisionData.ELEMENT_SIZE,
					bytes, 0, bytes.length);
		}
		
		TagDownloaderRevisionData data =
				new TagDownloaderRevisionData(bytes, tagFactory);
		
		return new DbRevisionImpl(revisionId, data.getSha1(), data.getTags());
	}
	
	@Override
	public List<DbRevision> getRevisions(List<Long> revisionIds){
		List<DbRevision> result = new ArrayList<DbRevision>(revisionIds.size());
		
		for(Long revisionId: revisionIds){
			result.add(getRevision(revisionId));
		}
		
		return result;
	}

	@Override
	public void clear() throws Exception {
		bigByteArray = null;
		bigByteArray = ByteBigArrays.ensureCapacity(new byte[0][0],
				(long)TagDownloaderRevisionData.ELEMENT_SIZE * (long)maxRevisionId);		
	}

	@Override
	public void flush() throws Exception {
		// do nothing
	}

}
