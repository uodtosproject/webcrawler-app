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

public class Crawler extends LogProducer {
	// Variables
	//private static long oldInterval = 604800000;
	private static long oldInterval = 5;
	public static int MaxIterations = 180;
	
	// Modules
	private HTTPModule httpMod = null;
	private URLProcessor urlMod = null;
	private URLQueue queue = null;
	private Analyst analyst = null;
	private TextProcessor textProcessor = null;
	private GoogleSearch gSearch = null;
	private IndexerSolr indexer = null;
	
	public Crawler(){
		this.logger = new MyLogger("output.txt");
		try {
			this.logger.setOnFile(true);
		} catch (IOException e) {}
		this.debug = false;

		httpMod = new HTTPModule(logger);
		httpMod.setDebug(debug);
		urlMod = new URLProcessor(logger);
		urlMod.setDebug(debug);
		queue = new URLQueue(logger);
		queue.setDebug(debug);
		analyst = new Analyst(logger);
		analyst.setDebug(debug);
		textProcessor = new TextProcessor(logger);
		textProcessor.setDebug(debug);
		gSearch = new GoogleSearch(logger);
		gSearch.setDebug(debug);
		indexer = new IndexerSolr();
	}
	
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
		List<String> visited = new ArrayList<String>();
		Site site = null;
		String method = "Crawler.crawl";
		
		// Find domain
		String domain = urlMod.getDomain(initialUrlString);
		log("set domain: " + domain, method);
		
		// Create URL object (Get the canonical URL, validate through UrlValidator and 
		// HTTPModule, check if it's in the same domain and if it's been visited already
		URL initialUrl  = urlMod.create(initialUrlString, domain, visited, queue);
		log("initial URL: " + initialUrlString, method);
		
		while(initialUrl == null && !queue.empty()) {
			String tUrl = queue.next();
			log("new initial URL: " + tUrl, method);
			initialUrl = urlMod.create(tUrl, domain, visited, queue);
		}
		
		// BEGIN If it's a valid initial URL
		if(initialUrl != null){

			// BEGIN Check for existing entry in the DB
			// Load the Entry from the DB
			site = Site.loadByUrl(initialUrl.toString(), false);
			log("loading site...", method);
			
			// If it's able to load it
			if(site != null){
				log("loaded successfully", method);
				
				// Calculate the age of the entry
				Long visitedOn = site.getVisitedOnMili();
				Long now = System.currentTimeMillis();
				
				// If it's old, archive it
				if((now - visitedOn) >= oldInterval){
					log("site archived", method);
					
					site.archive();
					site = null;
					visited.clear();
				}
				
			}
			// END Check for existing entry in the DB
		
			// BEGIN If there's no entry or it's been archived
			if(site == null){
				
				// Create entry
				site = new Site(Common.hashMD5(initialUrl), initialUrl.toString());
				site.setDomain(domain);
				log("new site entry created", method);
				
				// Add Google Search Results to the queue with a high score
				queue.add(gSearch.search("" + domain + " deceased.user"), MaxIterations + 2);
				queue.add(gSearch.search("" + domain + " terms of use"), MaxIterations + 2);
				queue.add(gSearch.search("" + domain + " terms of service"), MaxIterations + 2);
				queue.add(gSearch.search("" + domain + " help"), MaxIterations + 2);
				queue.add(gSearch.search("" + domain + " terms and coditions"), MaxIterations + 2);
				queue.add(gSearch.search("" + domain + " privacy policy"), MaxIterations + 2);
				queue.add(gSearch.search("" + domain + " faq"), MaxIterations + 2);
				
				// Add initial URL to the queue with the highest score
				queue.add(initialUrl.toString(), MaxIterations + 3);
				
				// BEGIN Crawl loop
				int iteration = 0;
				boolean firstPage = true;
				while(!queue.empty() && (iteration < MaxIterations)){
					log("\n------------------------------------", method);
					
					URL url = null;
					String tUrl = queue.next();
					log("new URL: " + tUrl, method);
					
					if(tUrl.length()>0){
						// Create URL object for the next item in the queue
						url = urlMod.create(tUrl, domain, visited, queue);
					} else {
						url = null;
					}
					
					// BEGIN If it's a valid URL
					if(url!= null){

						log("crawl " + url.toString(), method);
						iteration++;

						// Download page
						HTMLPage file = httpMod.download(url);
						
						// BEGIN If it's a valid file
						if(file != null){

							// Parse page
							Document doc = textProcessor.parse(file, url.toString());
							if(firstPage){
								String ico = textProcessor.getIco(doc, url.toString());
								if(!ico.equals("")){
									site.setIco(ico);
									firstPage = false;
								}
							}
							// Add links to the queue
							textProcessor.addLinks(doc, visited, domain, url.toString(), iteration, queue);
							
							// BEGIN If it's a relevant page
							if(analyst.relevantDocument(doc, url.toString())){
								log("relevant doc, saving it", method);
								
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
								log("not a relevant doc", method);
							} 
							
							// END If it's a relevant page
							
						} 
						// END If it's a valid file
						
					} 
					// END If it's a valid URL
					
				} 
				// END Crawl loop
				// If no page has been saved yet
				log("save site - " + String.valueOf(site.getPages().size()) + " pages", method);
				if(site.getPages().size() > 0){
					site.save();
					for(Page p : site.getPages()){ 
						log("saved entry: " + p.getUrl(), method);
					}
					indexer.index(site);
				} 
			} // END If there's no entry

		} else {
			log("invalid initial URL", method);
		}
		// END If there's no entry or it's been archived
		
		this.logger.close();
		return site;
	}
}
