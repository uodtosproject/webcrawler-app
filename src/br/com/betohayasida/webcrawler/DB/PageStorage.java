package br.com.betohayasida.webcrawler.DB;

import java.util.ArrayList;
import java.util.List;

import br.com.betohayasida.webcrawler.Store.Page;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Module responsible for handling the connection with the DB, Pages collection
 */
public class PageStorage extends MongoBase {
	private String DBNAME = "crawler";
	private String COLLECTIONNAME = "pages";

	/**
	 * Insert a Page in the DB
	 * @param page Page to be saved
	 */
	public void save(Page page){
		if(this.connect()){
			BasicDBObject doc = new BasicDBObject("parent", page.getParent()).
	                append("name", page.getName()).
	                append("retrievedOn", String.valueOf(System.currentTimeMillis())).
	                append("title", page.getTitle()).
	                append("url", page.getUrl()).
	                append("text", page.getText());
			
			collection.insert(doc);
			this.close();
		}
	}
	
	/**
	 * Reads the Pages of an SiteEntry
	 * @param parent String identifying the parent
	 * @return A List<Page> with the results
	 */
	public List<Page> get(String parent){
		List<Page> results = new ArrayList<Page>();
		
		if(this.connect()){
			BasicDBObject query = new BasicDBObject("parent", parent);
			DBCursor cursor = collection.find(query);
			
			try {
				
				while(cursor.hasNext()) {
					DBObject result = cursor.next();
					
					Page page = new Page(
							(String) result.get("name"),
							(String) result.get("parent"),
							(String) result.get("retrievedOn"),
							(String) result.get("text"),
							(String) result.get("title"),
							(String) result.get("url"));
					results.add(page);
				}
				
			} finally {
				cursor.close();
			}
			this.close();
		}
		
		return results;
	}
	
	/**
	 * Connect to the DB
	 * @return True if connection was successful
	 */
	private boolean connect(){
		return super.connect(DBNAME, COLLECTIONNAME);
	}
}
