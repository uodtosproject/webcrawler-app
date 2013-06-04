package br.com.betohayasida.webcrawler.Modules;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import br.com.betohayasida.webcrawler.Store.TOS;
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
public class SiteStorage {
	private MongoClient mongoClient = null;
	private DBCollection collection = null;
	private DB db = null;
	private PageStorage pageStorage = new PageStorage();
	public String DBNAME = "crawler";
	public String COLLECTIONNAME = "websites";
	
	/**
	 * Connect to the DB.
	 * @param name DB's name
	 */
	public boolean connect(){
		boolean connected = false;
		
		// create client
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		if(mongoClient != null){
			db = mongoClient.getDB(this.DBNAME);
			collection = db.getCollection(this.COLLECTIONNAME);
			
			connected = pageStorage.connect();
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
	public void insert(TOS tos){
		// Insert or Update 'websites' collection
		BasicDBObject query = new BasicDBObject("url", tos.getUrl());
		DBCursor cursor = collection.find(query);

		BasicDBObject doc = new BasicDBObject("name", tos.getName()).
                append("url", tos.getUrl()).
                append("visitedOn", String.valueOf(System.currentTimeMillis()));

		try {
			if(cursor.hasNext()) {
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append("$set", doc);
				collection.update(query, newDocument);
		   } else {
				collection.insert(doc);
		   }
		} finally {
		   cursor.close();
		}
		
		// Insert or Update 'pages' collection
		for(Page page : tos.getPages()){
			pageStorage.insert(page, tos.getName());
		}
	}
	
	/**
	 * Reads an entry of the DB
	 * @param url The URL of the page
	 * @return An String containing the source code of the page.
	 */
	public TOS read(String url){
		// Read from 'websites' collection
		TOS tos = null;
		BasicDBObject query = new BasicDBObject("originUrl", url);
		DBCursor cursor = collection.find(query);
		
		try {
			while(cursor.hasNext()) {
				DBObject result = cursor.next();
				tos = new TOS();
				tos.setName((String) result.get("name"));
				tos.setUrl((String) result.get("url"));
				tos.setVisitedOn((String) result.get("visitedOn"));
				// Read from 'pages' collection
				tos.setPages(pageStorage.readAll(tos.getName()));
			}
		} finally {
			cursor.close();
		}
		
		
		return tos;
	}	
	
	/**
	 * Reads an entry of the DB
	 * @param filename The filename: URL hashed through MD5
	 * @return ToSStore object
	 */
	public TOS readName(String name){
		// Read from 'websites' collection
		TOS tos = null;
		BasicDBObject query = new BasicDBObject("name", name);
		DBCursor cursor = collection.find(query);
		
		try {
			while(cursor.hasNext()) {
				DBObject result = cursor.next();
				tos = new TOS();
				tos.setName((String) result.get("name"));
				tos.setUrl((String) result.get("url"));
				tos.setVisitedOn((String) result.get("visitedOn"));
			}
		} finally {
			cursor.close();
		}
		
		// Read from 'pages' collection
		tos.setPages(pageStorage.readAll(tos.getName()));
		
		return tos;
	}
	
	public List<TOS> last(int limit) {
		List<TOS> results = new ArrayList<TOS>();
		
		BasicDBObject sortPredicate = new BasicDBObject();
		sortPredicate.put("retrievedOn", -1);
		
		DBCursor cursor = this.collection().find().sort(sortPredicate);

		int i = 0;
		while(cursor.hasNext() && i < limit){
			TOS tos = new TOS();
			DBObject item = cursor.next();
			tos.setName((String) item.get("name"));
			tos.setUrl((String) item.get("url"));
			tos.setPages(pageStorage.readAll(tos.getName()));
			tos.setVisitedOn((String) item.get("visitedOn"));
			results.add(tos);
			i++;
		}
		
		return results;
	}
}
