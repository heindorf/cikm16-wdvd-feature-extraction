package de.upb.wdqa.wdvd.db.interfaces;

import java.util.List;
import java.util.Set;

public interface DbRevision {
	long getRevisionId();
	
	String getSha1();
	
	Set<DbTag> getTags();
	
	List<String> getTagNames();
}
