package br.com.betohayasida.webcrawler.Modules;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Module responsible for analyzing pages, checking their links and content.
 */
public class Analyst {
	private List<String> relevantlist;
	private List<String> blacklist;
	
	public Analyst(){
		relevantlist = new ArrayList<String>();
		relevantlist.add("account");
		relevantlist.add("agreement");
		relevantlist.add("continue");
		relevantlist.add("conditions");
		relevantlist.add("eula");
		relevantlist.add("faq");
		relevantlist.add("forum");
		relevantlist.add("help");
		relevantlist.add("policy");
		relevantlist.add("policies");
		relevantlist.add("privacy");
		relevantlist.add("support");
		relevantlist.add("terms");
		relevantlist.add("terms of use");
		relevantlist.add("terms of service");
		relevantlist.add("tos");
		relevantlist.add("user agreement");
		
		blacklist = new ArrayList<String>();
		blacklist.add("lang="); 
		blacklist.add("RMNixonDeceased");
		blacklist.add("language");
		blacklist.add("directory");
		blacklist.add("appcenter");
		blacklist.add("redirect");
		blacklist.add("photo.php");
		blacklist.add("r.php");
		blacklist.add("support.google.com/maps/");
		blacklist.add("support.google.com/youtube/");
	}
	
	/**
	 * Analyzes if a page is the Terms of Service page.
	 * @param doc	Document object, parsed by the jSoup library.
	 * @param url	String containing the URL of the page.
	 * @return True if it's the page wanted; otherwise, false.
	 */
	public boolean relevantDocument(Document doc, String url){
		boolean targetPage = false;
		boolean moved = false;
		
		// BEGIN Check page title
		Elements titles = doc.getElementsByTag("title");
		for(Element title : titles){
			if(title.text().toLowerCase().contains("301 moved permanently")){
				moved = true;
			} else if(this.relevant(title.text()) && !this.titleRegex(title.text())){
				targetPage = true;
			}
		}
		// END Check page title
		
		// BEGIN Check URL
		if(!moved){
			if(this.relevant(url)){
				targetPage = true;
			}
		}
		// END Check URL
		
		return targetPage;
	}
	
	/**
	 * Analyzes a string of text for relevant terms
	 * @param text The String to be analyzed
	 * @return True if the string contains relevant terms
	 */
	public boolean relevant(String text){
		return (relevantListed(text) && !blackListed(text));
	}

	/**
	 * Checks if a string contains any relevant terms
	 * @param text The String to be analyzed
	 * @return True if the string contains relevant terms
	 */
	public boolean relevantListed(String text){
		for(String term : this.relevantlist){
			if(text.toLowerCase().contains(term)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a string contains any blacklisted terms
	 * @param text The String to be analyzed
	 * @return True if the string contains blacklisted terms
	 */
	public boolean blackListed(String text){
		for(String term : this.blacklist){
			if(text.toLowerCase().contains(term)) return true;
		}
		return false;
	}
	
	public boolean titleRegex(String title){
		return title.matches("^(\\w+\\s+)+\\(.*\\) on Twitter$");
	}
}
