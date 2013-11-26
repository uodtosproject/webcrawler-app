package br.com.betohayasida.webcrawler.Modules;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import br.com.betohayasida.webcrawler.DB.SiteStorage;
import br.com.betohayasida.webcrawler.Store.Page;
import br.com.betohayasida.webcrawler.Store.Site;
import br.com.betohayasida.webcrawler.Tools.SolrIndexer;
/**
 * Indexes pages in Solr
 * @author rkhayasidajunior
 *
 */
public class IndexerSolr {
	private SolrIndexer indexer = new SolrIndexer();
	private List<String> currentParents = new ArrayList<String>();
	
	/**
	 * Get the list of Sites from the DB
	 */
	private void loadSites(){
		List<Site> sites = new ArrayList<Site>();
		SiteStorage siteModel = new SiteStorage();
		if(siteModel.connect()){
			sites = siteModel.getSites();
		}
		for(Site site : sites){
			currentParents.add(site.getName() + "|" + site.getVisitedOn());
		}
	}
	
	/**
	 * Indexes a Site object
	 * @param site
	 */
	public void index(Site site){
		indexer.open(null);
		loadSites();
		
		for(Page page : site.getPages()){
			JSONObject doc = new JSONObject();
			doc.put("id", page.getName() + "|" + page.getRetrievedOnMili().toString());
			doc.put("parent", page.getParent());
			doc.put("name", page.getName());
			doc.put("text", page.getText());
			doc.put("title", page.getTitle());
			doc.put("url", page.getUrl());
            if(currentParents.contains(doc.get("parent"))){
                doc.put("status", "current");
            } else {
                doc.put("status", "archived");
            }
		}
		indexer.commit();
	}
}
