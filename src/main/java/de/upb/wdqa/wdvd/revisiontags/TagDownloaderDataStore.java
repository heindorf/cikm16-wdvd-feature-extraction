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

import java.util.List;

import de.upb.wdqa.wdvd.db.interfaces.DbRevision;

public interface TagDownloaderDataStore {
	public void connect() throws Exception;	
	public void disconnect() throws Exception;
	public void clear() throws Exception;
	public void flush() throws Exception;
	
	public void putRevision(DbRevision dbRevision) throws Exception;
	public DbRevision getRevision(long revisionId) throws Exception;
	public List<DbRevision> getRevisions(List<Long> revisionIds) throws Exception;
}
