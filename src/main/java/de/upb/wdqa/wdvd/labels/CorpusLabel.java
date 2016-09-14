/*
 * WDVC-2015 Statistics Example
 * 
 * Copyright (C) 2015 Stefan Heindorf
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.upb.wdqa.wdvd.labels;

/**
 * Representation of one label in the corpus (one line in the CSV file).
 */
public class CorpusLabel {
	private long revisionId;
	private long groupId;
	private boolean wasRollbackReverted;
	private boolean wasUndoRestoreReverted;

	public long getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(long revisionId) {
		this.revisionId = revisionId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public boolean wasRollbackReverted() {
		return wasRollbackReverted;
	}

	public void setRollbackReverted(boolean wasRollbackReverted) {
		this.wasRollbackReverted = wasRollbackReverted;
	}

	public boolean wasUndoRestoreReverted() {
		return wasUndoRestoreReverted;
	}

	public void setUndoRestoreReverted(boolean wasUndoRestoreReverted) {
		this.wasUndoRestoreReverted = wasUndoRestoreReverted;
	}

	public String toString() {
		return "Revision " + revisionId + " (revision group " + groupId + ") "
				+ " rollback reverted: " + wasRollbackReverted
				+ ", undo-restore reverted: " + wasUndoRestoreReverted;
	}
}
