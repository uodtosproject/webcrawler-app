package br.com.betohayasida.webcrawler.Modules;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

import br.com.betohayasida.webcrawler.Exceptions.DownloadException;
import br.com.betohayasida.webcrawler.Exceptions.InvalidURLException;
import br.com.betohayasida.webcrawler.GSearch.GoogleSearch;
import br.com.betohayasida.webcrawler.Store.HTMLPage;
import br.com.betohayasida.webcrawler.Store.Page;
import br.com.betohayasida.webcrawler.Store.Site;
import br.com.betohayasida.webcrawler.Tools.Common;
import br.com.betohayasida.webcrawler.Tools.URLQueue;

public class Crawler {
	//private static long oldInterval = 604800000;
	private static long oldInterval = 2;
	public static int MaxIterations = 200;
	// Modules
	private HTTPModule httpMod = new HTTPModule();
	private URLProcessor urlMod = new URLProcessor();
	private URLQueue queue = new URLQueue();
	private Analyst analyst = new Analyst();
	private TextProcessor textProcessor = new TextProcessor();
	private GoogleSearch gSearch = new GoogleSearch();
	
	/**
	 * Multiple crawling
	 * @param urls String of URLs separated by whitespaces
	 * @return HashMap<String, TOS>, TOS indexed by URL
	 * @throws InvalidURLException If the URL is invalid
	 * @throws DownloadException If the page cannot be downloaded
	 * @throws IOException 
	 */
	public HashMap<String, Site> mcrawl(String urls) throws InvalidURLException, DownloadException, IOException{
		HashMap<String, Site> results = new HashMap<String, Site>();
		
        String[] urlList = urls.split("\\s+"); 
        List<String> pUrlList = new ArrayList<String>();
        for(String url : urlList){
        	url = url.trim();
        	if(!pUrlList.contains(url)){
            	pUrlList.add(url);
            	results.put(url, this.crawl(url));
        	}
        }
		
		return results;
	}
	
	/**
	 * Crawls a site
	 * @param initialUrlString Initial URL
	 * @return A TOS object
	 * @throws InvalidURLException If the URL is invalid
	 * @throws DownloadException If the page cannot be downloaded
	 * @throws IOException 
	 */
	public Site crawl(String initialUrlString) throws InvalidURLException, DownloadException, IOException {
		// Variables
		Site site = null;
		boolean debug = true;
		List<String> visited = new ArrayList<String>();
		int iteration = 0;
		String domain = null;
		URL initialUrl = null;
		
		// Find domain
		domain = urlMod.getDomain(initialUrlString);
		if(debug) System.out.println("Found domain " + domain);
		
		// Create URL object (Get the canonical URL, validate through UrlValidator and 
		// HTTPModule, check if it's in the same domain and if it's been visited already
		initialUrl = urlMod.create(initialUrlString, domain, visited, queue);
		while(initialUrl == null && !queue.empty()) {
			String tUrl = queue.next();
			initialUrl = urlMod.create(tUrl, domain, visited, queue);
		}
		
		// BEGIN If it's a valid initial URL
		if(initialUrl != null){

			// BEGIN Check for existing entry in the DB
			// Load the Entry from the DB
			site = Site.loadByUrl(initialUrl.toString(), false);
			
			// If it's able to load it
			if(site != null){
				if(debug) System.out.println("Found an entry in the DB");
				
				// Calculate the age of the entry
				Long visitedOn = site.getVisitedOnMili();
				Long now = System.currentTimeMillis();
				
				// If it's old, archive it
				if((now - visitedOn) >= oldInterval){
					if(debug) System.out.println("Archived old entry");
					site.archive();
					site = null;
				}
				
			}
			// END Check for existing entry in the DB
		
			// BEGIN If there's no entry or it's been archived
			if(site == null){
				
				// Create entry
				site = new Site(Common.hashMD5(initialUrl), initialUrl.toString());
				
				// Add Google Search Results to the queue with a high score
				queue.add(gSearch.search("site:" + domain + " deceased.user"), MaxIterations + 2);
				queue.add(gSearch.search("site:" + domain + " terms of use"), MaxIterations + 2);
				queue.add(gSearch.search("site:" + domain + " terms of service"), MaxIterations + 2);
				queue.add(gSearch.search("site:" + domain + " help"), MaxIterations + 2);
				
				// Add initial URL to the queue with the highest score
				queue.add(initialUrl.toString(), MaxIterations + 3);
				
				// BEGIN Crawl loop
				while(!queue.empty() && (iteration < MaxIterations)){
					URL url = null;
					String tUrl = queue.next();
					System.out.println(tUrl);
					// Create URL object for the next item in the queue
					url = urlMod.create(tUrl, domain, visited, queue);
					
					// BEGIN If it's a valid URL
					if(url!= null){
						
						if(debug) System.out.print(" -c  " + url.toString());
						iteration++;
							
						// Download page
						HTMLPage file = httpMod.download(url);
						
						// BEGIN If it's a valid file
						if(file != null){
							
							// Parse page
							Document doc = textProcessor.parse(file, url.toString());
							
							// BEGIN If it's a relevant page
							if(analyst.relevantDocument(doc, url.toString())){
								
								if(debug) System.out.print(" (+) \n");

								// If no page has been saved yet
								if(site.getPages().size() == 0){
									site.setVisitedOn(String.valueOf(System.currentTimeMillis()));
								} 
								
								String name = Common.hashMD5(url);
								String parent = site.getName() + "|" + site.getVisitedOn();
								String retrivedOn = String.valueOf(System.currentTimeMillis());
								String text = textProcessor.clean(doc);
								String title = doc.getElementsByTag("title").text();
								
								Page page = new Page(name, parent, retrivedOn, text, title, url.toString());
								site.addPage(page);
								
							// ELSE If it's a relevant page
							} else {
								
								if(debug) System.out.print(" (-) \n");
								
								// Add links to the queue
								textProcessor.addLinks(doc, visited, domain, url.toString(), iteration, queue);
								
							} 
							// END If it's a relevant page
							
						} 
						// END If it's a valid file
						
					} 
					// END If it's a valid URL
					
				} 
				// END Crawl loop
				// If no page has been saved yet
				if(site.getPages().size() > 0){
					site.save();
					if(debug) System.out.println("Saved entry in the DB");
				} 
			} // END If there's no entry

		}
		// END If there's no entry or it's been archived
		return site;
	}
	
}
