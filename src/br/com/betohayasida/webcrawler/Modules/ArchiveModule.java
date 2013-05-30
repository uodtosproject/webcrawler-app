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
public class ArchiveModule {
	private MongoClient mongoClient = null;
	private DBCollection collection = null;
	private DB db = null;
	public String DBNAME = "crawler";
	public String COLLECTIONNAME = "archive";

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
	
	/**
	 * Insert a document in the DB.
	 * @param filename The file's name.
	 * @param url The file's URL.
	 * @param originUrl The URL of the website.
	 * @param page An HTMLPage object containing the source code.
	 */
	public void insert(ToSStore tos){
		BasicDBObject doc = new BasicDBObject("filename", tos.getFilename()).
                append("url", tos.getUrl()).
                append("originUrl", tos.getOriginUrl()).
                append("source", tos.getSource()).
                append("text", tos.getText()).
                append("retrievedOn", String.valueOf(System.currentTimeMillis()));

		collection.insert(doc);
	}
	
	/**
	 * Reads an entry of the DB
	 * @param filename The filename: URL hashed through MD5
	 * @return List<ToSStore> object
	 */
	public ToSStore read(String filename, String retrievedOn){
		ToSStore tos = null;
		BasicDBObject query = new BasicDBObject("filename", filename).append("retrievedOn", retrievedOn);
		DBCursor cursor = collection.find(query);
		
		try {
			while(cursor.hasNext()) {
				tos = new ToSStore();
				DBObject result = cursor.next();
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

	
	public List<ToSStore> archive(String filename) {
		List<ToSStore> results = new ArrayList<ToSStore>();
		BasicDBObject query = new BasicDBObject("filename", filename);
		DBCursor cursor = collection.find(query);

		while(cursor.hasNext()){
			ToSStore tos = new ToSStore();
			DBObject item = cursor.next();
			tos.setFilename((String) item.get("filename"));
			tos.setOriginUrl((String) item.get("originUrl"));
			tos.setRetrievedOn((String) item.get("retrievedOn"));
			tos.setSource((String) item.get("source"));
			tos.setText((String) item.get("text"));
			tos.setUrl((String) item.get("url"));
			results.add(tos);
		}
		
		return results;
	}
	
}
