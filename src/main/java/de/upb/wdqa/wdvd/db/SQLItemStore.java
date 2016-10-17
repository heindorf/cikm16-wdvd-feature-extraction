/*
 * Wikidata Vandalism Detector 2016 (WDVD-2016)
 * 
 * Copyright (c) 2016 Stefan Heindorf, Martin Potthast, Benno Stein, Gregor Engels
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.upb.wdqa.wdvd.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.db.implementation.DbItemImpl;
import de.upb.wdqa.wdvd.db.interfaces.DbItem;


public class SQLItemStore implements ItemStore {
	static final Logger logger = LoggerFactory.getLogger(SQLItemStore.class);
	
//	final static int BATCH_SIZE = 1; // just for testing
	static final int BATCH_SIZE = 10000;
	
	private static Connection connection = null;
	
	static String insertItemSQL = "INSERT INTO item"
			+ "(item_id, item_label, item_instance_of_id, item_subclass_of_id, item_part_of_id) VALUES"
			+ "(?,?,?,?,?)";
	
	static String getItemSQL = "SELECT * FROM item WHERE item_id = ?";
	
	static String deleteItems = "TRUNCATE item";
	
	static PreparedStatement insertItemPreparedStatement;
	static PreparedStatement getItemPreparedStatement;
	static int numInsertedStatements;
	
	@Override
	public void connect() {
		try {
		// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			// Setup the connection with the DB
			connection = DriverManager
				.getConnection("jdbc:mysql://localhost/wdqa?useServerPrepStmts=false&rewriteBatchedStatements=true",
						"wdqa", "5bI1vjEXasqjxOeWJXKu");
	
			// Statements allow to issue SQL queries to the database
			insertItemPreparedStatement = connection.prepareStatement(insertItemSQL);
			getItemPreparedStatement = connection.prepareStatement(getItemSQL);
		} catch (ClassNotFoundException | SQLException e) {
			logger.error("", e);
		}
	}
	
	/**
	 * Inserts an item into the database. This method is not thread-safe.
	 */
	@Override
	public void insertItem(DbItem item) {
		try {
			insertItemPreparedStatement.setInt(1, item.getItemId());
			insertItemPreparedStatement.setString(2, item.getLabel());
			
			if (item.getInstanceOfId() != null) {
				insertItemPreparedStatement.setInt(3, item.getInstanceOfId());
			} else {
				insertItemPreparedStatement.setNull(3, java.sql.Types.INTEGER);
			}
			
			if (item.getSubclassOfId() != null) {
				insertItemPreparedStatement.setInt(4, item.getSubclassOfId());
			} else {
				insertItemPreparedStatement.setNull(4, java.sql.Types.INTEGER);
			}
			
			if (item.getPartOfId() != null) {
				insertItemPreparedStatement.setInt(5, item.getPartOfId());
			} else {
				insertItemPreparedStatement.setNull(5, java.sql.Types.INTEGER);
			}
			
			insertItemPreparedStatement.addBatch();
			numInsertedStatements++;
			if (numInsertedStatements >= BATCH_SIZE) {
				insertItemPreparedStatement.executeBatch();
				numInsertedStatements = 0;
			}
			
		} catch (SQLException e) {
			String currentItem = "";
			
			if (item != null) {
				currentItem = "Item " + item.getItemId();
			}
			
			logger.error(currentItem, e);
		}
	}
	
	@Override
	public void flushItems() {
		try {
			insertItemPreparedStatement.executeBatch();
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	@Override
	public DbItem getItem(int itemId) {
		DbItem item = null;
		
		try {
			getItemPreparedStatement.setInt(1, itemId);

		ResultSet resultSet = getItemPreparedStatement.executeQuery();
		
		if (resultSet.first()) {
			item = new DbItemImpl(resultSet.getInt("item_id"),
					resultSet.getString("item_label"),
					resultSet.getInt("item_instance_of_id"),
					resultSet.getInt("item_subclass_of_id"),
					resultSet.getInt("item_part_of_id"));
		}

		} catch (SQLException e) {
			logger.error("", e);
		}
		
		return item;
	}
	
	@Override
	public void deleteItems() {
		try {
			Statement statement = connection.createStatement();
			statement.execute(deleteItems);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	@Override
	public void close() {
		try {
			flushItems();
			connection.close();
			
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

}
