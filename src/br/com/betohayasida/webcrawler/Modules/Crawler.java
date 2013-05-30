package br.com.betohayasida.webcrawler.Modules;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

import br.com.betohayasida.webcrawler.Exceptions.DownloadException;
import br.com.betohayasida.webcrawler.Exceptions.InvalidURLException;
import br.com.betohayasida.webcrawler.Store.HTMLPage;
import br.com.betohayasida.webcrawler.Store.LinkStore;
import br.com.betohayasida.webcrawler.Store.ToSStore;

public class Crawler {
	private static long oldInterval = 604800000;
	//private static long oldInterval = 2;
	
	public HashMap<String, ToSStore> mcrawl(String urls){
		HashMap<String, ToSStore> results = new HashMap<String, ToSStore>();
		
        String[] urlList = urls.split("\\s+"); 
        for(String url : urlList){
        	url = url.trim();
        	results.put(url, this.crawl(url));
        }
		
		return results;
	}
 
	public ToSStore crawl(String sUrl) {
		boolean debug = false;
		HTTPModule downloader = new HTTPModule();
		URLModule urlMod = new URLModule();
		URLQueue queue = new URLQueue();
		AnalysisModule analyst = new AnalysisModule();
		TextProcessingModule tpros = new TextProcessingModule();
		StorageModule store = new StorageModule();
		String originUrl = null;
		List<String> visited = new ArrayList<String>();
		ToSStore tos = null;
		
		// Connect to the DB
		store.connect();
		
		// BEGIN Create URL object
		URL url = null;
		try {
			url = urlMod.create(sUrl);
		} catch (InvalidURLException e) {
			// If it's an invalid URL
			if(debug) System.out.println(e.getMessage());
		}
		// END Create URL object
		
		if(url != null){
			tos = store.read(url.toString());
			boolean old = false;
			if(tos != null){
				Long retrievedOn = tos.getRetrievedOnMili();
				Long now = System.currentTimeMillis();
				old = (now - retrievedOn) >= oldInterval;
			}
			// BEGIN Check if it's valid
			if((tos == null || old) && downloader.checkHeaders(url)){
				
				if(old){
					ArchiveModule archive = new ArchiveModule();
					archive.connect();
					archive.insert(tos);
				}
				
				boolean found = false;
				int iteration = 1;
				
				originUrl = url.toString();
				
				// Add URL to the queue
				queue.add(url, 100);
				
				// BEGIN Crawl loop
				while(!queue.empty() && !found){
					if(debug) System.out.println();
					
					// Get an URL
					url = queue.next();
					visited.add(url.toString());
					if(debug) System.out.println("Fetching " + url.toString());
					
					// BEGIN Download page
					HTMLPage file = null;
					try {
						file = downloader.download(url);
					} catch (DownloadException e) {
						if(debug) System.out.println(e.getMessage());
					}
					// END Download page
					
					// BEGIN If it's a valid file
					if(file != null){
						
						// Parse page
						Document page = analyst.parse(file, url.toString());
						if(debug) System.out.println("Parsing " + url.toString());
						if(debug) System.out.println(page.toString());
						
						// Analyze page
						found = analyst.analyse(page, url.toString()); 
						if(debug) System.out.println("Analysing " + url.toString());
						
						// BEGIN If it's the Terms and Conditions page
						if(found){
							
							if(debug) System.out.println("FOUND! " + url.toString());
							tos = new ToSStore();
							tos.setFilename(file.filename());
							tos.setOriginUrl(originUrl);
							tos.setUrl(url.toString());
							tos.setSource(file.html());
							tos.setText(tpros.clean(page));
							
							store.insert(tos);
							tos = store.read(originUrl);
							//store.list();
							
						// ELSE If not, retrieve relevant links
						} else {
							
							// Retrieve the relevant links
							List<LinkStore> listOfLinks = analyst.links(page, visited);
							
							// BEGIN Add links to the queue
							for(LinkStore item : listOfLinks){
								
								// Check if it's not the same page
								if(!item.getUrl().equalsIgnoreCase(url.toString())){
									int score = 100 - iteration;
									
									// If the link seems relevant, higher score
									if(item.getText().toLowerCase().contains("terms") || item.getText().toLowerCase().contains("agreement")){ 
										score = 100 - iteration + 2;
									}
									
									// BEGIN Create URL object
									URL tUrl = null;
									try {
										tUrl = urlMod.create(item.getUrl());
									} catch (InvalidURLException e) {
										// If it's an invalid URL
										if(debug) System.out.println(e.getMessage());
									}
									// END Create URL object
									
									// if it's a valid URL
									if(tUrl != null){
										queue.add(tUrl, score);
										if(debug) System.out.println("Adding " + tUrl.toString() + " to the queue");
									}
									
								}
							}
							// END Add links to the queue
							
						}
						// END If it's the Terms and Conditions page
						
					}
					// END If it's a valid file
					
				}
				// END Crawl loop
				
				if(!found){
					if(debug) System.out.println("NOT FOUND!");
				}
				
			} else {
				if(debug) System.out.println("Invalid URL!");
			}
			// END Check if it's valid
		
		}
		return tos;
		
	}
	
}
