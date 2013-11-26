package br.com.betohayasida.webcrawler.DB;

import java.util.ArrayList;
import java.util.List;

import br.com.betohayasida.webcrawler.Store.Page;
import br.com.betohayasida.webcrawler.Store.Site;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Module responsible for handling the connection with the DB, Sites collection
 */
public class SiteStorage extends MongoBase {
	private String DBNAME = "crawler";
	private String COLLECTIONNAME = "sites";
	private PageStorage pageStorage = new PageStorage();
	
	/**
	 * Saves a Site in the DB
	 * @param site Site to be saved
	 */
	public void save(Site site){
		if(this.connect()){
			BasicDBObject query = new BasicDBObject("name", site.getName());
			DBCursor cursor = collection.find(query);
			
			BasicDBObject doc = new BasicDBObject("name", site.getName()).
	                append("url", site.getUrl()).
	                append("domain", site.getDomain()).
	                append("visitedOn", site.getVisitedOn()).
            		append("ico", site.getIco());
			
			try {
				if(cursor.hasNext()) {
					BasicDBObject newDocument = new BasicDBObject();
					newDocument.append("$set", doc);
					collection.update(query, newDocument);
				} else {
					collection.insert(doc);
				}
			} finally {
				
				// Only saves the pages if it's not archived
				if(!site.isArchived()){
					List<Page> pages = site.getPages();
					for(Page page : pages){
						pageStorage.save(page);
					}
				}
				
				cursor.close();
			}
			
			this.close();
		}
	}
	
	/**
	 * Retrieves an entry from the DB
	 * @param url URL to be searched for
	 * @return A Site object, or null if none is found
	 */
	public Site getByUrl(String url){
		Site site = null;
		
		if(this.connect()){
			BasicDBObject query = new BasicDBObject("url", url);
			DBCursor cursor = collection.find(query);

			try {
				
				while(cursor.hasNext()) {
					DBObject result = cursor.next();
					
					site = new Site((String) result.get("name"), (String) result.get("url"), (String) result.get("visitedOn"), (String) result.get("domain"));
					site.addPages(pageStorage.get(site.getName() + "|" + site.getVisitedOnMili()));
					String ico = (String) result.get("ico");
					site.setIco(ico == null ? "" : ico);
					
				}
				
			} finally {
				cursor.close();
			}
			
			this.close();
		}
		
		return site;
	}	
	
	/**
	 * Retrieves an entry from the DB
	 * @param name Name to be searched for
	 * @return A Site object, or null if none is found
	 */
	public Site getByName(String name){
		Site site = null;
		
		if(this.connect()){
			BasicDBObject query = new BasicDBObject("name", name);
			DBCursor cursor = collection.find(query);

			try {
				
				while(cursor.hasNext()) {
					DBObject result = cursor.next();
					
					site = new Site((String) result.get("name"), (String) result.get("url"), (String) result.get("visitedOn"), (String) result.get("domain"));
					site.addPages(pageStorage.get(site.getName() + "|" + site.getVisitedOnMili()));
					String ico = (String) result.get("ico");
					site.setIco(ico == null ? "" : ico);
					
				}
				
			} finally {
				cursor.close();
			}
			
			this.close();
		}
		
		return site;
	}	
	
	/**
	 * Retrieves all entries from the DB
	 * @param name Name to be searched for
	 * @return A Site object, or null if none is found
	 */
	public List<Site> getAllByName(String name){
		List<Site> sites = new ArrayList<Site>();
		
		if(this.connect()){
			BasicDBObject query = new BasicDBObject("name", name);
			DBCursor cursor = collection.find(query);

			try {
				
				while(cursor.hasNext()) {
					DBObject result = cursor.next();
					
					Site site = new Site((String) result.get("name"), (String) result.get("url"), (String) result.get("visitedOn"), (String) result.get("domain"));
					site.addPages(pageStorage.get(site.getName() + "|" + site.getVisitedOnMili()));
					sites.add(site);
					String ico = (String) result.get("ico");
					site.setIco(ico == null ? "" : ico);
					
				}
				
			} finally {
				cursor.close();
			}
			
			this.close();
		}
		
		return sites;
	}
	
	/**
	 * Retrieves all entries from the DB
	 * @param name Name to be searched for
	 * @return A Site object, or null if none is found
	 */
	public List<Site> getAllByUrl(String url){
		List<Site> sites = new ArrayList<Site>();
		
		if(this.connect()){
			BasicDBObject query = new BasicDBObject("url", url);
			DBCursor cursor = collection.find(query);

			try {
				
				while(cursor.hasNext()) {
					DBObject result = cursor.next();
					
					Site site = new Site((String) result.get("name"), (String) result.get("url"), (String) result.get("visitedOn"), (String) result.get("domain"));
					site.addPages(pageStorage.get(site.getName() + "|" + site.getVisitedOnMili()));
					sites.add(site);
					String ico = (String) result.get("ico");
					site.setIco(ico == null ? "" : ico);
				}
				
			} finally {
				cursor.close();
			}
			
			this.close();
		}
		
		return sites;
	}
	
	/**
	 * Retrieves the last added entries
	 * @param limit Limit of entries to be retrieves
	 * @return List<Site> object
	 */
	public List<Site> last(int limit) {
		List<Site> results = new ArrayList<Site>();
		
		if(this.connect()){
			BasicDBObject sortPredicate = new BasicDBObject();
			sortPredicate.put("retrievedOn", -1);
			
			DBCursor cursor = this.collection().find().sort(sortPredicate);
	
			int i = 0;
			while(cursor.hasNext() && i < limit) {
				DBObject result = cursor.next();
				
				Site site = new Site();
				site.setName((String) result.get("name"));
				site.setUrl((String) result.get("url"));
				site.setVisitedOn((String) result.get("visitedOn"));
				site.setDomain((String) result.get("domain"));
				String ico = (String) result.get("ico");
				site.setIco(ico == null ? "" : ico);
				
				results.add(site);
				
			}
			this.close();
		}
		
		return results;
	}
	
	/**
	 * Connects to the DB
	 * @return True if the connection is successful
	 */
	public boolean connect(){
		return super.connect(DBNAME, COLLECTIONNAME);
	}
	
	public List<Site> getSites() {
		List<Site> results = new ArrayList<Site>();
		
		if(this.connect()){
			BasicDBObject sortPredicate = new BasicDBObject();
			
			DBCursor cursor = this.collection().find().sort(sortPredicate);
	
			while(cursor.hasNext()) {
				DBObject result = cursor.next();
				
				Site site = new Site();
				site.setName((String) result.get("name"));
				site.setVisitedOn((String) result.get("visitedOn"));
				site.setUrl((String) result.get("url"));
				site.setDomain((String) ((result.get("domain")!=null) ? result.get("domain") : site.getUrl()));
				results.add(site);
				
			}
			this.close();
		}
		
		return results;
	}
}
