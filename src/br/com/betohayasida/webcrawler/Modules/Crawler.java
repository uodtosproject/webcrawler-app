package br.com.betohayasida.webcrawler.Modules;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

import br.com.betohayasida.webcrawler.Exceptions.DownloadException;
import br.com.betohayasida.webcrawler.Exceptions.InvalidURLException;
import br.com.betohayasida.webcrawler.Store.HTTPHeader;
import br.com.betohayasida.webcrawler.Store.HTMLPage;
import br.com.betohayasida.webcrawler.Store.LinkStore;
import br.com.betohayasida.webcrawler.Store.Page;
import br.com.betohayasida.webcrawler.Store.TOS;

public class Crawler {
	//private static long oldInterval = 604800000;
	private static long oldInterval = 2;
	
	/**
	 * Multiple crawling
	 * @param urls String of urls separated by whitespaces
	 * @return HashMap<String, TOS>, TOS indexed by URL
	 * @throws InvalidURLException If the URL is invalid
	 * @throws DownloadException If the page cannot be downloaded
	 */
	public HashMap<String, TOS> mcrawl(String urls) throws InvalidURLException, DownloadException{
		HashMap<String, TOS> results = new HashMap<String, TOS>();
		
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
	 * Crawls a website
	 * @param initialUrlString Initial URL
	 * @return A TOS object
	 * @throws InvalidURLException If the URL is invalid
	 * @throws DownloadException If the page cannot be downloaded
	 */
	public TOS crawl(String initialUrlString) throws InvalidURLException, DownloadException {
		boolean debug = false;
		HTTPModule downloader = new HTTPModule();
		URLModule urlMod = new URLModule();
		URLQueue queue = new URLQueue();
		Analist analist = new Analist();
		TextProcessor textProcessor = new TextProcessor();
		SiteStorage siteStore = new SiteStorage();
		TOS tos = null;
		
		// BEGIN Connect to DB
		if(siteStore.connect()){
		
			// BEGIN Create URL object
			URL initalUrl = urlMod.create(initialUrlString);
			// END Create URL object
			
			// BEGIN Check for existing entry in the DB
			tos = siteStore.read(initalUrl.toString());
			boolean old = false;
			if(tos != null){
				Long visitedOn = tos.getVisitedOnMili();
				Long now = System.currentTimeMillis();
				old = (now - visitedOn) >= oldInterval;
				
				if(old){
					//ARCHIVE
					
					tos = null;
				}
			}
			// END Check for existing entry in the DB
			
			// BEGIN If there's no entry
			if(tos == null || old){
				// Loop variables
				boolean found = false;
				boolean end = false;
				List<String> visited = new ArrayList<String>();
				URL url = null;
				int iteration = 0;
				
				String domain = urlMod.getDomain(initalUrl.toString());
				queue.add(initalUrl.toString(), 100);
				
				// BEGIN Crawl loop
				while(!queue.empty() && !end){
					iteration++;
					
					if(iteration > 200){
						end = true;
					}
					
					// Get an URL
					String urlString = queue.next();
					
					// BEGIN If it's the same domain
					if(urlString.contains(domain)){
					
						// BEGIN Create URL object
						url = urlMod.create(urlString);
						// END Create URL object
	
						// BEGIN If haven't visited the page
						if(url!= null && !visited.contains(url.toString()) && downloader.checkRobots(url)){
							visited.add(urlString);
							
							if(debug) System.out.println("START " + urlString);
						
							// BEGIN If it's a valid URL
							HTTPHeader header = new HTTPHeader();
							if(downloader.checkHeaders(url, header)){
			
								if(debug) System.out.println("HTTP HEAD: " + header.getCode());
								
								// BEGIN If it's 302 or 301
								if(header.getCode() == 302 || header.getCode() == 301){
									
									if(debug) System.out.println("Adding HTTP Head location: " + header.getLocation());
									queue.add(header.getLocation(), 100);
									
								// ELSE If it's 302 or 301
								} else {
									
									// Download page
									HTMLPage file = downloader.download(url);
									
									// BEGIN If it's a valid file
									if(file != null){
										
										// Parse page
										Document doc = analist.parse(file, url.toString());
										if(debug) System.out.println("Parsing " + url.toString());
										
										// Analyze page
										found = analist.analyse(doc, url.toString()); 
										if(debug) System.out.println("Analysing " + url.toString());
										
										// BEGIN If it's the Terms and Conditions page
										if(found){
											
											if(debug) System.out.println("Adding page " + url.toString());
											
											if(tos == null){
												tos = new TOS();
												tos.setName(file.filename());
												tos.setUrl(initialUrlString);
											}
											
											Page page = new Page();
											page.setParentName(tos.getName());
											page.setSource(file.html());
											page.setText(textProcessor.clean(doc));
											page.setUrl(url.toString());
											tos.addPage(page);
											
										// ELSE If not, retrieve relevant links
										} else {
											
											// Retrieve the relevant links
											List<LinkStore> listOfLinks = analist.links(doc, visited);
											
											// BEGIN Add links to the queue
											for(LinkStore item : listOfLinks){
												
												// BEGIN Check if it's not the same page
												if(!item.getUrl().equalsIgnoreCase(url.toString())){
													int score = 200 - iteration;
													
													// If the link seems relevant, higher score
													if(analist.relevantPLink(item.getUrl()) || analist.relevantPLink(item.getText())){ 
														score = 200;
													}
													
													// Add link to the queue
													queue.add(item.getUrl(), score);
													if(debug) System.out.println("Adding " + item.getUrl() + " to the queue");
													
												} // END Check if it's not the same page
												
											} // END Add links to the queue
											
										} // END If it's the Terms and Conditions page
										
									} // END If it's a valid file
									
								} // END If it's 302 or 301
								
							} // END If it's a valid URL
							
						} // END Haven't visited the page
						
					} // END If it's the same domain
					
				} // END Crawl loop
				
				if(tos != null){
					// Save entry
					siteStore.insert(tos);
				}
				
			} // END If there's no entry
			
		} // END Connect to DB
		
		siteStore.close();
		
		return tos;
		
	}
	
}
