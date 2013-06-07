package br.com.betohayasida.webcrawler.DB;

import br.com.betohayasida.webcrawler.Store.Site;
import com.mongodb.BasicDBObject;


/**
 * Module responsible for handling the connection with the DB.
 */
public class ArchiveStorage extends SiteStorage{
	private String DBNAME = "crawler";
	private String COLLECTIONNAME = "archive";
	
	/**
	 * Connects to the DB
	 * @return True if the connection is successful
	 */
	public boolean connect(){
		return super.connect(DBNAME, COLLECTIONNAME);
	}
	

	/**
	 * Saves a Site in the DB
	 * @param site Site to be saved
	 */
	public void save(Site site){
		if(this.connect()){
			
			BasicDBObject doc = new BasicDBObject("name", site.getName()).
	                append("url", site.getUrl()).
	                append("visitedOn", site.getVisitedOn());
			
			collection.insert(doc);

			this.close();
		}
	}
}
