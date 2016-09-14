package de.upb.wdqa.wdvd.processors;

import de.upb.wdqa.wdvd.Revision;

public interface RevisionProcessor {
	abstract void startRevisionProcessing();
	
	abstract void processRevision(Revision revision);
	
	abstract void finishRevisionProcessing();
	
}
