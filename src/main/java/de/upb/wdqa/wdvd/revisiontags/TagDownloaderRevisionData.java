package de.upb.wdqa.wdvd.revisiontags;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.db.implementation.DbTagFactory;
import de.upb.wdqa.wdvd.db.interfaces.DbRevision;
import de.upb.wdqa.wdvd.db.interfaces.DbTag;

// Estimated Memory Consumption
// sha1 (binary): 21 byte
// Tags (BitSet): 5 byte
// Length: 2 byte
// Sum: 28 bytes
public class TagDownloaderRevisionData{
	
	public final static int ELEMENT_SIZE = 28;
	
	static final Logger logger = LoggerFactory.getLogger(TagDownloaderRevisionData.class);
	
	private byte[] sha1;
	private byte[] tags;

	private DbTagFactory tagFactory;
	
	public TagDownloaderRevisionData(DbRevision revision, DbTagFactory tagFactory){
		this.tagFactory = tagFactory;
		this.sha1 = SHA1Converter.parseByte36(revision.getSha1());
		this.tags = tagsToBytes(revision.getTags());
	}
	
	
	/**
	 * Converts the tags to a memory efficient byte[] representation
	 */
	private byte[] tagsToBytes(Set<DbTag> tags){
		BitSet bitSet= new BitSet();
		
		if(tags != null){
			for(DbTag tag: tags){
				bitSet.set(tag.getTagId());			
			}
		}
		
		return bitSet.toByteArray();		
	}
	
	/**
	 * Converts a memory efficient byte[] representation to a more user friendly List of Strings
	 */
	private Set<DbTag> bytesToTags(byte[] bytes){
		Set<DbTag> result = new HashSet<DbTag>();
		BitSet bitSet = BitSet.valueOf(bytes);
		
		for(int i=bitSet.nextSetBit(0); i > -1;  i=bitSet.nextSetBit(i+1)){
			result.add(tagFactory.getTagById(i));
		}
		return result;	
	}
	


	public String getSha1(){
		return SHA1Converter.getBase36(sha1);
	}
	
	public Set<DbTag> getTags(){
		return bytesToTags(tags);
	}
	
	// sha1Length, sha1, tagsLength, tags
	public byte[] getByteArray(){
		int elementSize = 1 + sha1.length + 1 + tags.length;
				
		if(elementSize > ELEMENT_SIZE){
			throw new IllegalStateException("elementSize is larger than ELEMENT_SIZE: " + elementSize);
		}
		
		byte[] result = new byte[elementSize];
		result[0] = (byte) sha1.length;		
		System.arraycopy(sha1, 0, result, 1              , sha1.length);
		result[1 + sha1.length] = (byte)tags.length;		
		System.arraycopy(tags, 0, result, 2 + sha1.length, tags.length);
		
		return result;
	}	
	
	public TagDownloaderRevisionData(byte bytes[], DbTagFactory tagFactory) {
		this.tagFactory = tagFactory;
		
		byte sha1Length = bytes[0];
		byte tagsLength = bytes[1 + sha1Length];
		
		sha1 = new byte[sha1Length];
		tags = new byte[tagsLength];
		
		System.arraycopy(bytes, 1             , sha1, 0, sha1.length);
		System.arraycopy(bytes, 2 + sha1Length, tags, 0, tags.length);
	}
	
	   @Override
	    public int hashCode() {
	        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
	            append(sha1).
	            append(tags).
	            toHashCode();
	    }

	    @Override
	    public boolean equals(Object obj) {
	       if (!(obj instanceof TagDownloaderRevisionData))
	            return false;
	        if (obj == this)
	            return true;

	        TagDownloaderRevisionData rhs = (TagDownloaderRevisionData) obj;
	        return new EqualsBuilder().
	            append(sha1, rhs.sha1).
	            append(tags, rhs.tags).
	            isEquals();
	    }	
}