package org.wikidata.wdtk.dumpfiles;

import org.wikidata.wdtk.dumpfiles.MwRevisionImpl;

public class ExtendedMwRevisionImpl extends MwRevisionImpl {
	
	String sha1;
	boolean isMinor;
	String parentId;
	boolean isCommentDeleted;
	boolean isTextDeleted;
	
	public ExtendedMwRevisionImpl() {
		super();
	}

	public ExtendedMwRevisionImpl(MwRevision mwRevision) {
		super(mwRevision);
		if (mwRevision instanceof ExtendedMwRevisionImpl){
			this.sha1 = ((ExtendedMwRevisionImpl) mwRevision).sha1;
			this.isMinor = ((ExtendedMwRevisionImpl) mwRevision).isMinor;
			this.parentId = ((ExtendedMwRevisionImpl) mwRevision).parentId;
			this.isCommentDeleted = ((ExtendedMwRevisionImpl) mwRevision).isCommentDeleted;
			this.isTextDeleted = ((ExtendedMwRevisionImpl) mwRevision).isTextDeleted;
		}
	}
	
	@Override
	void resetCurrentRevisionData() {
		super.resetCurrentRevisionData();
		sha1 = null;
		isMinor = false;
		parentId = null;
	}


	
	public String getSHA1(){
		return sha1;
	}
	
	public boolean isMinor(){
		return isMinor;
	}
	
	public boolean isCommentDeleted(){
		return isCommentDeleted;
	}
	
	public boolean isTextDeleted(){
		return isTextDeleted;
	}
	
	public String getParentId(){
		return parentId;
	}

	
	@Override
	public String toString() {
		
		String textLength = (this.text!=null)?"" + this.text.length():"null";
		
		// avoid null pointer exception
		return "Revision " + this.revisionId + " of page " + this.prefixedTitle
				+ " (ns " + this.namespace + ", id " + this.pageId
				+ "). Created at " + this.timeStamp + " by " + this.contributor
				+ " (" + this.contributorId + ") with comment \""
				+ this.comment + "\". Model " + this.model + " (" + this.format
				+ "). Text length: " + textLength; 
	}

}
