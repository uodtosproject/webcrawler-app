package br.com.betohayasida.webcrawler.Modules;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import br.com.betohayasida.webcrawler.Store.HTMLPage;
import br.com.betohayasida.webcrawler.Tools.URLQueue;

/**
 * HTML parser
 * @author rkhayasidajunior
 *
 */
public class TextProcessor extends LogProducer {
	
	public TextProcessor(){
		this.logger = new MyLogger("output-text_processor");
	}
	
	public TextProcessor(MyLogger logger){
		this.logger = logger;
		this.debug = true;
	}
	
	/**
	 * Parses the HTML document using the jSoup library
	 * @param file	HTMLPage object, containing the source code of the page.
	 * @param url	String containing the URL of the page.
	 * @return A Document object, parsed by the jSoup library.
	 */
	public Document parse(HTMLPage file, String url){
		log("parsing", "TextProcessor.parse");
		if(file.html().length() > 0) return Jsoup.parse(file.html(), url);
		else return null;
	}
	
	/**
	 * Get the ICO URL
	 * @param doc The HTML page as a Document object
	 * @param url The URL of the page
	 * @return A String containing the ICO URL or, if not found, a blank string. 
	 */
	public String getIco(Document doc, String url){
		String ico = "";
		
		Elements links = doc.getElementsByAttributeValueContaining("rel", "shortcut");
		if(links.size() > 0){
			Element link = links.get(0);
			ico = link.attr("href");
			if(!ico.startsWith("http")){
				if(!ico.startsWith("//")){
					ico = url + ico;
				} else {
					ico = "http:" + ico;
				}
			}
		} else {
			links = doc.getElementsByAttributeValueContaining("rel", "icon");
			Element link = links.get(0);
			ico = link.attr("href");
			if(!ico.startsWith("http")){
				if(!ico.startsWith("//")){
					ico = url + ico;
				} else {
					ico = "http:" + ico;
				}
			}
		}
		return ico;
	}
	
	/**
	 * Cleans an HTML document, leaving only a set of tags
	 * @param page Document object (jSoup)
	 * @return A String with the clean HTML
	 */
	public String clean(Document page){
		String cleaned = new String();
		Whitelist myWhitelist = new Whitelist();
		
		page.select("form").remove();
		page.select("script").remove();
		page.select("a").remove();

		myWhitelist.addTags("p", "b", "i", "br", "h1", "h2", "h3", "h4", "h5", "h6");
		//myWhitelist.addAttributes("a", "href");
		
		cleaned = Jsoup.clean(page.html(), myWhitelist);
		cleaned = cleaned.replace("&nbsp;", " ");
		
		return cleaned;
	}

	/**
	 * Retrieves the relevant links of a page
	 * @param doc Document to be analyzed
	 * @param visited List of links visited
	 * @param analist Analyst object
	 * @return
	 */
	public void addLinks(Document doc, List<String> visited, String domain, String currentUrl, int iteration, URLQueue queue){
		Analyst analyst = new Analyst();
		
		if(doc != null){
			Elements links = (Elements) doc.select("a[href]");
			for(Element link : links){
				if(!visited.contains(link.attr("abs:href")) && link.attr("abs:href").contains(domain)){
					
					if(!analyst.blackListed(link.text()) && !analyst.blackListed(link.attr("abs:href"))){
						if( (analyst.relevantListed(link.text()) || analyst.relevantListed(link.attr("abs:href")))){
							log("adding " + link.attr("abs:href"), "TextProcessor.addLinks");
							queue.add(link.attr("abs:href"), Crawler.MaxIterations - iteration);
						}
					}
					
				}
			}

		}
		
	}
}