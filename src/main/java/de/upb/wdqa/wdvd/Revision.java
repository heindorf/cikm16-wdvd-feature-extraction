package de.upb.wdqa.wdvd;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.dumpfiles.ExtendedMwRevisionImpl;
import org.wikidata.wdtk.dumpfiles.MwRevision;

import de.upb.wdqa.wdvd.features.Feature;
import de.upb.wdqa.wdvd.features.FeatureValue;
import de.upb.wdqa.wdvd.features.revision.misc.CONTENT_TYPE;
import de.upb.wdqa.wdvd.features.user.IsPrivilegedUser;
import de.upb.wdqa.wdvd.features.user.misc.IsBotUser;
import de.upb.wdqa.wdvd.geolocation.GeoInformation;
import de.upb.wdqa.wdvd.labels.RevertMethod;

public class Revision extends ExtendedMwRevisionImpl {
	static final Logger logger = LoggerFactory.getLogger(Revision.class);
	
	private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
	
	// some basic revision attributes
	private Revision prevRevision; // must be thread-safe
	private Revision latestRevision; // must be thread-safe
	private int positionWithinGroup;
	private long revisionGroupId;	

	// revision attributes which are computationally demanding and which are cached
	private ParsedComment parsedComment;
	private TextRegex textRegex = new TextRegex();
	private ItemDocument itemDocument;
	
	// Denotes the latest parsable item document. This might be different from the latestRevision.	
	private ItemDocument latestItemDocument; // must be thread-safe
	private HashMap<Feature, FeatureValue> featureValues = new HashMap<Feature, FeatureValue>();

	// enriching revision with additional (external) data
	private List<String> downloadedTags;
	private String downloadedSha1;
	private GeoInformation geoInformation;	
	private EnumMap<RevertMethod, Boolean> wasReverted = new EnumMap<RevertMethod, Boolean>(RevertMethod.class);

	public Revision() {
		super();
	}
	
	public Revision(MwRevision revision){
		super(revision);
	}
	
	// TODO: Always update this constructor when attributes are added or removed
	@SuppressWarnings("unchecked")
	public Revision(Revision revision) {
		super(revision);
		this.parsedComment = revision.parsedComment; // shallow copy
		this.textRegex = revision.textRegex; // shallow copy
		this.prevRevision = revision.prevRevision;
		this.latestRevision = revision.latestRevision;
		this.positionWithinGroup = revision.positionWithinGroup;
		this.revisionGroupId = revision.revisionGroupId;
		this.wasReverted = revision.wasReverted.clone();// shallow copy of the reverting revisions
		this.downloadedTags = revision.downloadedTags;
		this.downloadedSha1 = revision.downloadedSha1;
		this.geoInformation = revision.geoInformation;
		
		this.itemDocument = revision.itemDocument;
		this.latestItemDocument = revision.latestItemDocument;
		
		this.featureValues = (HashMap<Feature, FeatureValue>)revision.featureValues.clone(); //shallow copy
	}
	
	public ParsedComment getParsedComment() {		
		// Lazy Generation
		if (parsedComment == null){
			parsedComment = new ParsedComment(getComment());
		}		
		
		return parsedComment;
	}
	
	public TextRegex getTextRegex(){
		return textRegex;
	}
	
	public void setTextRegex(TextRegex textRegex){
		this.textRegex = textRegex;
	}
	
	public Revision getPreviousRevision(){
		return prevRevision;
	}
	
	public long getRevisionGroupId(){
		return revisionGroupId;
	}
	
	public void setRollbackReverted(Boolean wasRollbackReverted) {
		this.setReverted(RevertMethod.ROLLBACK, wasRollbackReverted);
	}
	
	public void setUndoRestoreReverted(Boolean wasUndoRestoreReverted) {
		this.setReverted(RevertMethod.UNDO_RESTORE, wasUndoRestoreReverted);
	}
	
	public void setReverted(RevertMethod method, Boolean wasReverted){
		this.wasReverted.put(method, wasReverted);
	}
	
	public boolean hasPrivilegedContributor() {
		return IsPrivilegedUser.isPrivilegedUser(getContributor());
	}
	
	public boolean hasBotContributor() {
		return IsBotUser.isBot(getContributor());
	}
	
	public int getPositionWithinGroup() {
		return positionWithinGroup;
	}

	public void setPositionWithinGroup(int positionInRevisionGroup) {
		this.positionWithinGroup = positionInRevisionGroup;
	}
	
	public void setRevisionGroupId(long revisionGroupId){
		this.revisionGroupId = revisionGroupId;
	}
	

	public void setPreviousRevision(Revision prevRevision) {
		this.prevRevision = prevRevision;		
	}
	
	public boolean wasRollbackReverted() {
		return wasReverted(RevertMethod.ROLLBACK);
	}
	
	public boolean wasUndoRestoreReverted() {
		return wasReverted(RevertMethod.UNDO_RESTORE);
	}
	
	public boolean wasReverted(RevertMethod method){
		boolean result = false;
		
		if(wasReverted != null && wasReverted.get(method) != null){
			result = wasReverted.get(method);
		}

		return result;
	}
	
	public void setDownloadedSha1(String downloadedSha1){
		this.downloadedSha1 = downloadedSha1;
	}
	
	public String getDownloadedSHA1() {		
		return downloadedSha1;
	}	

	public void setDownloadedTags(List<String> downloadedTags){
		this.downloadedTags = downloadedTags;
	}

	public List<String> getDownloadedTags() {
		return downloadedTags;
	}
	
	public void setGeoInformation(GeoInformation geoInformation){
		this.geoInformation = geoInformation;
	}
	
	public GeoInformation getGeoInformation(){
		return geoInformation;
	}
	
	public int getItemId(){
		return getItemIdFromString(getPrefixedTitle());	
	}
	
	public Date getDate(){
		Date result = null;
		
		try{
			String timestamp = getTimeStamp();
			
			result = formatter.parse(timestamp);
		}
		catch (ParseException e){
			logger.warn("Invalid time stamp: " + getTimeStamp());
		}
		
		return result;		
	}
	
	public CONTENT_TYPE getContentType(){
		return CONTENT_TYPE.getContentType(getParsedComment().getAction1());
	}
	
	public void setLatestRevision(Revision revision){
		this.latestRevision = revision;
	}
	
	public Revision getLatestRevision(){
		return latestRevision;
	}
	
	public ItemDocument getLatestItemDocument(){
		return latestItemDocument;
	}
	
	public void setLatestItemDocument(ItemDocument latestItemDocument) {
		
		// Depending on the exact type of ItemDocument, a deep copy might be
		// necessary here. JacksonItemDocument is not thread-safe (the various
		// ArrayList iterators within ItemDocument throw
		// ConcurrentModificationException otherwise.) However,
		// ItemDocumentImpl which is currently used seems to be thread-safe.

		// DatamodelConverter converter = new DatamodelConverter(new DataObjectFactoryImpl());
		// this.latestItemDocument = converter.copy(latestItemDocument);
		
		// Temporary solution to fix ConcurrentModificationException
		// this.latestItemDocument = null;	
		
		this.latestItemDocument = latestItemDocument;	
	}
	
	public ItemDocument getItemDocument() {
		return itemDocument;
	}
	
	public void setItemDocument(ItemDocument itemDocument) {
		this.itemDocument = itemDocument;
	}
	
	public void setFeatureValue (Feature f, FeatureValue v){
		featureValues.put(f, v);
	}
	
	public HashMap<Feature, FeatureValue> getFeatureValues(){
		return featureValues;
	}
	

	
	public boolean contributorEquals(Revision revision){
		boolean notNull = this.getContributor() != null &&
				revision != null;
		
		return notNull && this.getContributor().equals(revision.getContributor());
	}
	
	public static int getItemIdFromString(String str){
		int result = -1;
		if(str != null && str.startsWith("Q")){
			str = str.substring(1);
		}
		else{
			logger.warn("Title: " + str + ": non-well-formed item string: " + str);
		}
		try{
			result = Integer.parseInt(str);
		}
		catch(NumberFormatException e){
			logger.warn("Title: " + str + ": non-well-formed item string: " + str);
		}
		
		return result;
	}

}

