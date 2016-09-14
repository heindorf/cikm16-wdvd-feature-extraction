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
