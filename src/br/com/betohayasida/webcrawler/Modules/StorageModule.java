package br.com.betohayasida.webcrawler.Modules;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import br.com.betohayasida.webcrawler.Store.ToSStore;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Module responsible for handling the connection with the DB.
 */
public class StorageModule {
	private MongoClient mongoClient = null;
	private DBCollection collection = null;
	private DB db = null;
	public String DBNAME = "crawler";
	public String COLLECTIONNAME = "websites";
	
	/**
	 * Connect to the DB.
	 * @param name DB's name
	 */
	public void connect(){
		
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		db = mongoClient.getDB(this.DBNAME);
		collection = db.getCollection(this.COLLECTIONNAME);
		
	}
	
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
	public void insert(ToSStore tos){
		BasicDBObject query = new BasicDBObject("url", tos.getUrl());
		DBCursor cursor = collection.find(query);

		BasicDBObject doc = new BasicDBObject("filename", tos.getFilename()).
                append("url", tos.getUrl()).
                append("originUrl", tos.getOriginUrl()).
                append("source", tos.getSource()).
                append("text", tos.getText()).
                append("retrievedOn", String.valueOf(System.currentTimeMillis()));

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
	}
	
	/**
	 * Reads an entry of the DB
	 * @param url The URL of the page
	 * @return An String containing the source code of the page.
	 */
	public ToSStore read(String url){
		ToSStore tos = null;
		BasicDBObject query = new BasicDBObject("originUrl", url);
		DBCursor cursor = collection.find(query);
		
		try {
			while(cursor.hasNext()) {
				DBObject result = cursor.next();
				tos = new ToSStore();
				tos.setFilename((String) result.get("filename"));
				tos.setOriginUrl((String) result.get("originUrl"));
				tos.setUrl((String) result.get("url"));
				tos.setSource((String) result.get("source"));
				tos.setText((String) result.get("text"));
				tos.setRetrievedOn((String) result.get("retrievedOn"));
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
	public ToSStore readFilename(String filename){
		ToSStore tos = null;
		BasicDBObject query = new BasicDBObject("filename", filename);
		DBCursor cursor = collection.find(query);
		
		try {
			while(cursor.hasNext()) {
				DBObject result = cursor.next();
				tos = new ToSStore();
				tos.setFilename((String) result.get("filename"));
				tos.setOriginUrl((String) result.get("originUrl"));
				tos.setUrl((String) result.get("url"));
				tos.setSource((String) result.get("source"));
				tos.setText((String) result.get("text"));
				tos.setRetrievedOn((String) result.get("retrievedOn"));
			}
		} finally {
			cursor.close();
		}
		
		return tos;
	}
	
	public List<ToSStore> last(int limit) {
		List<ToSStore> results = new ArrayList<ToSStore>();
		
		BasicDBObject sortPredicate = new BasicDBObject();
		sortPredicate.put("retrievedOn", -1);
		
		DBCursor cursor = this.collection().find().sort(sortPredicate);

		int i = 0;
		while(cursor.hasNext() && i < limit){
			ToSStore tos = new ToSStore();
			DBObject item = cursor.next();
			tos.setFilename((String) item.get("filename"));
			tos.setOriginUrl((String) item.get("originUrl"));
			tos.setRetrievedOn((String) item.get("retrievedOn"));
			tos.setSource((String) item.get("source"));
			tos.setText((String) item.get("text"));
			tos.setUrl((String) item.get("url"));
			results.add(tos);
			i++;
		}
		
		return results;
	}
}
