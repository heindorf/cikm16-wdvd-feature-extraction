package de.upb.wdqa.wdvd.db.interfaces;

public interface DbItem {	
	// The item id
	public int getItemId();

	// English Label or an appropriate fallback
	public String getLabel();

	// The first claim with the property "P31" of the highest available rank
	public Integer getInstanceOfId();
	
	// The first claim with property "P279" of the highest available rank
	public Integer getSubclassOfId();

	// The first claim with property "P361" of the highest available rank
	public Integer getPartOfId();
}
