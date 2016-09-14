package de.upb.wdqa.wdvd.revisiontags;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.db.implementation.DbRevisionImpl;
import de.upb.wdqa.wdvd.db.implementation.DbTagFactory;
import de.upb.wdqa.wdvd.db.interfaces.DbRevision;
import de.upb.wdqa.wdvd.db.interfaces.DbTag;


// data store based on database (uses little memory, but disk access is slow)
public class TagDownloaderDbDataStore implements TagDownloaderDataStore {
	
	static final Logger logger = LoggerFactory.getLogger(TagDownloaderDbDataStore.class);
	
	static int NUMBER_OF_THREADS = 4;
	static int MAX_SIZE_OF_QUEUE = 100000; // for inserts
	
	DbTagFactory tagFactory;
	
	BlockingQueue<DbRevision> queue = new ArrayBlockingQueue<>(MAX_SIZE_OF_QUEUE);
	
	List<ConnectionThread> threads = new ArrayList<ConnectionThread>();
	
	Connection connection = null;
	
	// Key Checks
	String disableForeignKeyChecksSQL = "SET FOREIGN_KEY_CHECKS = 0";
	String enableForeignKeyChecksSQL  = "SET FOREIGN_KEY_CHECKS = 1";
	
	// Revision
	String getRevisionSQL = "SELECT * FROM revision WHERE rev_id = ?";
	String getMultipleRevisionsSQL = "SELECT * FROM revision WHERE rev_id IN (?)";
	String truncateRevisionsSQL = " TRUNCATE revision";	
	PreparedStatement getRevisionPreparedStatement;
	PreparedStatement getMultipleRevisionsPreparedStatement;
	
	// Tag
	String getTagSQL = "SELECT * FROM tag WHERE tag_id = ?";
	String truncateTagsSQL = "TRUNCATE tag";	
	PreparedStatement getTagPreparedStatement;
	PreparedStatement deleteTagsPreparedStatement;
	
	// Revision_Tag
	String getRevisionTagSQL = "SELECT * FROM revision_tag WHERE rev_id = ?";
	String truncateRevisionTags = "TRUNCATE revision_tag";	
	PreparedStatement getRevisionTagPreparedStatement;
	
	public TagDownloaderDbDataStore(DbTagFactory tagFactory) {
		this.tagFactory = tagFactory;
	}
	
	@Override
	public void connect() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");

	    // Setup the connection with the DB
	    connection = DriverManager
	    		.getConnection("jdbc:mysql://localhost/wdqa?useServerPrepStmts=false&rewriteBatchedStatements=true",
	    				"wdqa", "5bI1vjEXasqjxOeWJXKu");

      // Statements allow to issue SQL queries to the database
	  getTagPreparedStatement = connection.prepareStatement(getTagSQL);
	  deleteTagsPreparedStatement = connection.prepareStatement(truncateTagsSQL);	    
      getRevisionPreparedStatement = connection.prepareStatement(getRevisionSQL);
      getMultipleRevisionsPreparedStatement = connection.prepareStatement(getMultipleRevisionsSQL);
      getRevisionTagPreparedStatement = connection.prepareStatement(getRevisionTagSQL);
      
      clear();
      
      for(int i = 0; i < NUMBER_OF_THREADS; i++){
    	  ConnectionThread thread = new ConnectionThread(queue, tagFactory);
    	  thread.setName("Connection Thread " + i);
    	  
    	  threads.add(thread);
    	  thread.start();
      }
      
//      connection.setAutoCommit(false);
	}


	@Override
	public void disconnect() throws Exception {
		// Notify threads to stop their work
		for(int i = 0; i < NUMBER_OF_THREADS; i++){
			queue.put(ConnectionThread.DONE);
		}
		
		// Wait until all threads have stopped their work
		for(ConnectionThread thread: threads){
			thread.join();
		}
//		connection.commit();
	}

	@Override
	public void putRevision(DbRevision dbRevision) throws Exception {		
		queue.put(dbRevision);
	}

	@Override
	public DbRevision getRevision(long revisionId) throws Exception {
		List<Long> list = new ArrayList<Long>(1);
		list.add(revisionId);		

		List<DbRevision> queryResult = getRevisions(list);
		
		DbRevision result = queryResult.get(0);

		return result;
	}
	
	@Override
	public List<DbRevision> getRevisions(List<Long> revisionIds) throws Exception {
		HashMap<Long, DbRevisionImpl> revisions = getPureRevisions(revisionIds);
		addTags(revisions);
		
		// Sort revisions in the same order as the revisionIds
		List<DbRevision> result = new ArrayList<DbRevision>(revisionIds.size());
		for(Long revisionId: revisionIds){
			result.add(revisions.get(revisionId));
		}
	
		return result;
	}

	private HashMap<Long, DbRevisionImpl> getPureRevisions(List<Long> revisionIds) throws Exception{
		HashMap<Long, DbRevisionImpl> revisions = new HashMap<Long, DbRevisionImpl>();
		
		if(revisionIds.size() > 0){			
			StringBuilder statementSQL = new StringBuilder();
			statementSQL.append("SELECT * FROM revision WHERE rev_id IN (");
			
			for(int i = 0; i < revisionIds.size(); i++){
				statementSQL.append("?");
				if(i != revisionIds.size() -1){
					statementSQL.append(",");
				}
				else{
					statementSQL.append(")");
				}
			}
			
			PreparedStatement prepStatement = connection.prepareStatement(statementSQL.toString());
			
			for (int i = 0; i < revisionIds.size(); i++){
				prepStatement.setLong(i + 1, revisionIds.get(i));
			}
	
			ResultSet resultSet = prepStatement.executeQuery();
			
			while(resultSet.next()){
				DbRevisionImpl revision = new DbRevisionImpl(resultSet.getInt("rev_id"),
						resultSet.getString("rev_sha1"),
						null);	
				
				revisions.put(revision.getRevisionId(), revision);
			}
		}
		return revisions;
	}
	
	private void addTags(HashMap<Long, DbRevisionImpl> revisions) throws SQLException {
		if(revisions.size() > 0){			
			StringBuilder statementSQL = new StringBuilder();
			statementSQL.append("SELECT * FROM revision_tag WHERE rev_id IN (");
			
			for(int i = 0; i < revisions.size(); i++){
				statementSQL.append("?");
				if(i != revisions.size() -1){
					statementSQL.append(",");
				}
				else{
					statementSQL.append(")");
				}
			}
			
			PreparedStatement prepStatement = connection.prepareStatement(statementSQL.toString());
			
			int i = 1;
			for(DbRevision dbRevision: revisions.values()){
				prepStatement.setLong(i, dbRevision.getRevisionId());
				i++;
			}
		
			ResultSet resultSet = prepStatement.executeQuery();
			
			while(resultSet.next()){
				long revisionId = resultSet.getLong("rev_id");
				int tagId = resultSet.getInt("tag_id");
				
				DbRevisionImpl revision = revisions.get(revisionId);
				DbTag tag = tagFactory.getTagById(tagId);
				
				revision.addTag(tag);
			}
		}
	}

	@Override
	public void clear() throws Exception {
		Statement statement = connection.createStatement();
		
		statement.execute(disableForeignKeyChecksSQL);
		statement.execute(truncateRevisionTags);
		statement.execute(truncateRevisionsSQL);
		statement.execute(truncateTagsSQL);
		statement.execute(enableForeignKeyChecksSQL);
	}

	@Override
	public void flush() throws Exception {	
		// notify all threads about the flush
		for (int i = 0; i < threads.size(); i++){
			queue.put(ConnectionThread.FLUSH);
		}
		
		// wait until all threads have got it
		for (ConnectionThread thread: threads){
			while(!thread.flushed){
				Thread.sleep(100);
			}
		}

		// resume normal operation	
		for (ConnectionThread thread2: threads){
			thread2.resetFlushed();
		}
	}
}

class ConnectionThread extends Thread{
	
	static final Logger logger = LoggerFactory.getLogger(ConnectionThread.class);
	
	// Special revisions to control the threads
	static final DbRevision DONE = new DbRevisionImpl();
	static final DbRevision FLUSH = new DbRevisionImpl();
	
	final static int BATCH_SIZE = 10000;
	
	// tagFactory and queue are accessed by several threads.
	// Hence, they must be thread-safe
	DbTagFactory tagFactory;
	BlockingQueue<DbRevision> queue;
	
	// this variable is accesed by several threads. Hence, it is declared volatile
	volatile boolean flushed;
	
	Connection connection;
	
	String insertRevisionSQL = "INSERT INTO revision"
			+ "(rev_id, rev_sha1) VALUES"
			+ "(?,?)";
	
	String insertTagSQL = "INSERT IGNORE INTO tag"
			+ "(tag_id, tag_name) VALUES"
			+ "(?,?)";
	
	String insertRevisionTagSQL = "INSERT INTO revision_tag"
			+ "(rev_id, tag_id) VALUES"
			+ "(?,?)";
	
	PreparedStatement insertRevisionPreparedStatement;
	PreparedStatement insertTagPreparedStatement;
	PreparedStatement insertRevisionTagPreparedStatement;
	
	int numInsertedStatements;	

	
	public ConnectionThread(BlockingQueue<DbRevision> queue, DbTagFactory tagFactory){
		this.queue = queue;
		this.tagFactory = tagFactory;
	}
	
	@Override
	public void run() {
		try{
			// Create new connection
			connect();	
			
			DbRevision revision = null;
			do{
				do{
					revision = queue.take();
					
					if (revision != DONE && revision != FLUSH){
						addRevisionToBatch(revision);
						numInsertedStatements++;
					}	
				} while(revision != DONE  && revision != FLUSH
						&& numInsertedStatements < BATCH_SIZE);				
	
				executeBatch();
				numInsertedStatements = 0;
				
				if(revision == FLUSH){
					// set the current status to "flushed"
					flushed = true;
					
					// wait until this thread is allowed to continue
					while(flushed){
						Thread.sleep(100);
					}		
				}		
				
			}while (revision != DONE);
					
			// Finally, close the connection
			disconnect();
		}
		catch(Throwable t){
			logger.error("", t);
		}

	}
	
	private void connect() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");

	    // Setup the connection with the DB
	    connection = DriverManager
	    		.getConnection("jdbc:mysql://localhost/wdqa?useServerPrepStmts=false&rewriteBatchedStatements=true",
	    				"wdqa", "5bI1vjEXasqjxOeWJXKu");

      // Statements allow to issue SQL queries to the database
	  insertTagPreparedStatement = connection.prepareStatement(insertTagSQL);	    
      insertRevisionPreparedStatement = connection.prepareStatement(insertRevisionSQL);     
      insertRevisionTagPreparedStatement = connection.prepareStatement(insertRevisionTagSQL);

	}
	
	private void disconnect() throws SQLException {
		connection.close();		
	}
	
	private void addRevisionToBatch(DbRevision dbRevision) throws Exception {
		insertRevisionPreparedStatement.setLong(1, dbRevision.getRevisionId());
		insertRevisionPreparedStatement.setString(2, dbRevision.getSha1());
		insertRevisionPreparedStatement.addBatch();
		
		Set<DbTag> tags = dbRevision.getTags();
		if(tags != null){
			for(DbTag tag: tags){
				insertRevisionTagPreparedStatement.setLong(1, dbRevision.getRevisionId());
				insertRevisionTagPreparedStatement.setLong(2, tag.getTagId());
				insertRevisionTagPreparedStatement.addBatch();
			}
		}

	}
	
	private void executeBatch() {
		try{
			for(DbTag tag: tagFactory.getAllDbTags()){
				insertTagPreparedStatement.setInt(1, tag.getTagId());
				insertTagPreparedStatement.setString(2, tag.getTagName());
				insertTagPreparedStatement.addBatch();
			}
			insertTagPreparedStatement.executeBatch();
			insertTagPreparedStatement.clearBatch();
			
			insertRevisionPreparedStatement.executeBatch();
			insertRevisionPreparedStatement.clearBatch();
			insertRevisionTagPreparedStatement.executeBatch();
			insertRevisionTagPreparedStatement.clearBatch();
		}
		catch(Exception e){
			logger.error("", e);
		}
	}
	
	public boolean flushed(){
		return flushed;
	}
	
	public void resetFlushed(){
		flushed = false;
	}	
}
