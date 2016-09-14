package de.upb.wdqa.wdvd.labels;

public enum RevertMethod {
	ROLLBACK ("rollback"),
	UNDO_RESTORE ("undo_restore");
	
    private final String text;

    /**
     * @param text
     */
    private RevertMethod(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
