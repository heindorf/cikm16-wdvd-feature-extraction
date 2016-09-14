package de.upb.wdqa.wdvd.db.implementation;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.upb.wdqa.wdvd.db.interfaces.DbItem;

// Stores labels and basic membership properties,
// cf., http://www.wikidata.org/wiki/Help:Basic_membership_properties
public class DbItemImpl implements DbItem, Serializable{
	private static final long serialVersionUID = 1L;
	
	private static int NULL_SENTINEL = -1;
	
	private int itemId;
	private String label;
	private int instanceOfId; // null is represented by -1 (to save memory)
	private int subclassOfId; // null is represented by -1 (to save memory)
	private int partOfId; // null is represented by -1 (to save memory)
	
	public DbItemImpl(int itemId, String label, Integer instanceOfId, Integer subclassOfId, Integer partOfId) {
		super();
		this.itemId = itemId;
		this.label = label;
		this.instanceOfId = integerToInt(instanceOfId);
		this.subclassOfId = integerToInt(subclassOfId);
		this.partOfId = integerToInt(partOfId);
	}
	
	public DbItemImpl(DbItem item){
		this.itemId = item.getItemId();
		this.label = item.getLabel();
		this.instanceOfId = integerToInt(item.getInstanceOfId());
		this.subclassOfId = integerToInt(item.getSubclassOfId());
		this.partOfId = integerToInt(item.getPartOfId());
	}	

	@Override
	public int getItemId() {
		return itemId;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Integer getInstanceOfId() {
		return intToInteger(instanceOfId);
	}
	
	@Override
	public Integer getSubclassOfId(){
		return intToInteger(subclassOfId);
	}
	
	@Override
	public Integer getPartOfId(){
		return intToInteger(partOfId);
	}
	
	private static Integer intToInteger(int value){
		if (value == NULL_SENTINEL){
			return null;
		}
		else{
			return Integer.valueOf(value);
		}
	}
	
	private static int integerToInt(Integer value){
		if (value == null){
			return NULL_SENTINEL;
		}
		else if (value == NULL_SENTINEL) {
			throw new RuntimeException("" + value + " is a special value to represent null and should not be used otherwise");
		}		
		else{
			return value;
		}		
	}

	@Override
	public int hashCode() {
		   return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
			        append(itemId).
			        append(label).
			        append(instanceOfId).
			        append(subclassOfId).
			        append(partOfId).
			        toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj == this)
	        return true;
	    if (!(obj instanceof DbItemImpl))
	        return false;

	    DbItemImpl rhs = (DbItemImpl) obj;
	    return new EqualsBuilder().
	        append(itemId, rhs.itemId).
	        append(label, rhs.label).
	        append(instanceOfId, rhs.instanceOfId).
	        append(subclassOfId, rhs.subclassOfId).
	        append(partOfId, rhs.partOfId).
	        isEquals();
	}	
}
