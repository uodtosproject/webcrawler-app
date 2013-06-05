package br.com.betohayasida.webcrawler.Modules;

import java.util.ArrayList;
import java.util.List;

import br.com.betohayasida.webcrawler.Store.Page;
import br.com.betohayasida.webcrawler.Store.TOS;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Module responsible for handling the connection with the DB.
 */
public class ArchiveStorage extends MongoBase{
	public String DBNAME = "crawler";
	public String COLLECTIONNAME = "archive";

	public boolean connect(){
		return super.connect(DBNAME, COLLECTIONNAME);
	}
	
	/**
	 * Insert a document in the DB.
	 * @param filename The file's name.
	 * @param url The file's URL.
	 * @param originUrl The URL of the website.
	 * @param page An HTMLPage object containing the source code.
	 */
	public void insert(TOS tos){
		if(this.connect()){
			BasicDBObject doc = new BasicDBObject("name", tos.getName()).
	                append("url", tos.getUrl()).
	                append("visitedOn", String.valueOf(System.currentTimeMillis()));
			
			BasicDBObject psdoc = new BasicDBObject();
			int i = 1;
			List<Page> pages = tos.getPages();
			for(Page page : pages){
				psdoc.append(String.valueOf(i++), new BasicDBObject("url", page.getUrl()).
	                append("source", page.getSource()).
	                append("text", page.getText()).
	                append("retrievedOn", String.valueOf(System.currentTimeMillis())));
			}
	
			doc.append("pages", psdoc);
			collection.insert(doc);
			this.close();
		}
	}
	
	/**
	 * Reads an entry of the DB
	 * @param url The URL of the page
	 * @return An String containing the source code of the page.
	 */
	public TOS read(String name, long retrievedOn){
		TOS tos = null;
		
		if(this.connect()){
			BasicDBObject query = new BasicDBObject("name", name).append("retrievedOn", String.valueOf(retrievedOn));
			DBCursor cursor = collection.find(query);
			
			try {
				
				while(cursor.hasNext()) {
					DBObject result = cursor.next();
					
					tos = new TOS();
					tos.setName((String) result.get("name"));
					tos.setUrl((String) result.get("url"));
					tos.setVisitedOn((String) result.get("visitedOn"));
					
					BasicDBObject pages = (BasicDBObject) result.get("pages");
					int size = pages.size();
					int i;
					for(i = 1; i <= size; i++){
						
						BasicDBObject pageDBObj = (BasicDBObject) pages.get(String.valueOf(i));
						Page page = new Page();
						page.setParentName((String) pageDBObj.get("parentName"));
						page.setRetrievedOn((String) pageDBObj.get("retrievedOn"));
						page.setSource((String) pageDBObj.get("source"));
						page.setText((String) pageDBObj.get("text"));
						page.setUrl((String) pageDBObj.get("url"));
						tos.addPage(page);
						
					}
					
				}
				
			} finally {
				cursor.close();
			}
			this.close();
		}
		return tos;
	}	
	
	/**
	 * Reads an entry of the DB
	 * @param url The URL of the page
	 * @return An String containing the source code of the page.
	 */
	public List<TOS> readAll(String name){
		List<TOS> results = new ArrayList<TOS>();
		
		if(this.connect()){
			BasicDBObject query = new BasicDBObject("name", name);
			DBCursor cursor = collection.find(query);
			
			try {
				
				while(cursor.hasNext()) {
					DBObject result = cursor.next();
					
					TOS tos = new TOS();
					tos.setName((String) result.get("name"));
					tos.setUrl((String) result.get("url"));
					tos.setVisitedOn((String) result.get("visitedOn"));
					
					BasicDBObject pages = (BasicDBObject) result.get("pages");
					int size = pages.size();
					int i;
					for(i = 1; i <= size; i++){
						
						BasicDBObject pageDBObj = (BasicDBObject) pages.get(String.valueOf(i));
						Page page = new Page();
						page.setParentName((String) pageDBObj.get("parentName"));
						page.setRetrievedOn((String) pageDBObj.get("retrievedOn"));
						page.setSource((String) pageDBObj.get("source"));
						page.setText((String) pageDBObj.get("text"));
						page.setUrl((String) pageDBObj.get("url"));
						tos.addPage(page);
						
					}
					results.add(tos);
					
				}
				
			} finally {
				cursor.close();
			}
			this.close();
			
		}
		return results;
	}	
	
}
