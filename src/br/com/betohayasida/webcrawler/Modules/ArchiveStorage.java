package br.com.betohayasida.webcrawler.Modules;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import br.com.betohayasida.webcrawler.Store.Page;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Module responsible for handling the connection with the DB.
 */
public class ArchiveStorage {
	private MongoClient mongoClient = null;
	private DBCollection collection = null;
	private DB db = null;
	public String DBNAME = "crawler";
	public String COLLECTIONNAME = "archive";
	
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
	 * Returns the DBCollection
	 * @return DBCollection object
	 */
	public DBCollection collection(){
		return this.collection;
	}
	
	/**
	 * Insert a document in the DB.
	 * @param filename The file's name.
	 * @param url The file's URL.
	 * @param originUrl The URL of the website.
	 * @param page An HTMLPage object containing the source code.
	 */
	public void insert(Page page, String parentName){
		BasicDBObject doc = new BasicDBObject("parentName", parentName).
				append("url", page.getUrl()).
                append("source", page.getSource()).
                append("text", page.getText()).
                append("retrievedOn", page.getRetrievedOnMili());
		collection.insert(doc);
		
	}
	
	/**
	 * Reads an entry of the DB
	 * @param url The URL of the page
	 * @return An String containing the source code of the page.
	 */
	public Page read(String url, long retrievedOn){
		Page page = null;
		BasicDBObject query = new BasicDBObject("url", url).append("retrievedOn", retrievedOn);
		DBCursor cursor = collection.find(query);
		
		try {
			while(cursor.hasNext()) {
				DBObject result = cursor.next();
				page = new Page();
				page.setParentName((String) result.get("parentName"));
				page.setUrl((String) result.get("url"));
				page.setSource((String) result.get("source"));
				page.setText((String) result.get("text"));
				page.setRetrievedOn((String) result.get("retrievedOn"));
			}
		} finally {
			cursor.close();
		}
		
		return page;
	}	
	
	/**
	 * Reads an entry of the DB
	 * @param url The URL of the page
	 * @return An String containing the source code of the page.
	 */
	public List<Page> readAll(String url){
		List<Page> pages = new ArrayList<Page>();
		BasicDBObject query = new BasicDBObject("url", url);
		DBCursor cursor = collection.find(query);
		Page page;
		
		try {
			while(cursor.hasNext()) {
				DBObject result = cursor.next();
				page = new Page();
				page.setParentName((String) result.get("parentName"));
				page.setUrl((String) result.get("url"));
				page.setSource((String) result.get("source"));
				page.setText((String) result.get("text"));
				page.setRetrievedOn((String) result.get("retrievedOn"));
				pages.add(page);
			}
		} finally {
			cursor.close();
		}
		
		return pages;
	}	
	
}
