package br.com.betohayasida.webcrawler.Modules;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * Base Class for MongoDB Classes
 *
 */
public class MongoBase {
	protected MongoClient mongoClient = null;
	protected DBCollection collection = null;
	protected DB db = null;
	protected String DBNAME = "default";
	protected String COLLECTIONNAME = "default";
	
	/**
	 * Connect to the DB.
	 * @param name DB's name
	 */
	public boolean connect(){
		boolean connected = false;
		
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if(mongoClient != null){
			db = mongoClient.getDB(this.DBNAME);
			collection = db.getCollection(this.COLLECTIONNAME);
			
			if(collection != null){
				connected = true;
			}
		}
		
		return connected;
	}
	
	/**
	 * Close the connection with the database
	 */
	public void close(){
		this.mongoClient.close();
	}
	
	/**
	 * Returns the DBCollection
	 * @return DBCollection object
	 */
	public DBCollection collection(){
		return this.collection;
	}

}
