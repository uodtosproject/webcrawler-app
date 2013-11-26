package br.com.betohayasida.webcrawler.DB;

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
	//private static final String USER = "crawlerAdmin";
	//private static final String PWD = "socWAR";
	
	/**
	 * Connect to the DB.
	 * @param DBNAME DB's name
	 * @param COLLECTIONNAME Collection's name
	 * @return If connected, return true; otherwise, false
	 */
	public boolean connect(String DBNAME, String COLLECTIONNAME){
		boolean connected = false;
		//MongoCredential credential = MongoCredential.createMongoCRCredential(USER, DBNAME, PWD.toCharArray());
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("The DB is offline");
		}
		if(mongoClient != null){
			db = mongoClient.getDB(DBNAME);
			collection = db.getCollection(COLLECTIONNAME);
			
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
