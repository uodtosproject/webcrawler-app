package br.com.betohayasida.webcrawler.Modules;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.betohayasida.webcrawler.Store.HTMLPage;
import br.com.betohayasida.webcrawler.Store.LinkStore;

/**
 * Module responsible for analysing pages, checking their links and content.
 */
public class Analist {
	
	/**
	 * Analyzes if a page is the Terms of Service page.
	 * @param doc	Document object, parsed by the jSoup library.
	 * @param url	String containing the URL of the page.
	 * @return True if it's the page wanted; otherwise, false.
	 */
	public boolean analyse(Document doc, String url){
		boolean targetPage = false;
		boolean moved = false;
		
		// BEGIN Check page title
		Elements titles = doc.getElementsByTag("title");
		for(Element title : titles){
			if(title.text().toLowerCase().contains("301 moved permanently")){
				moved = true;
			}
			
			if(this.relevantTitle(title.text())){
				targetPage = true;
			}
		}
		// END Check page title
		
		// BEGIN Check URL
		if(!moved && !targetPage){
			if(this.relevantLink(url)){
				targetPage = true;
			}
		}
		// END Check URL
		
		return (!moved && targetPage);
	}
	
	/**
	 * Parses the HTML document using the jSoup library
	 * @param file	HTMLPage object, containing the source code of the page.
	 * @param url	String containing the URL of the page.
	 * @return A Document object, parsed by the jSoup library.
	 */
	public Document parse(HTMLPage file, String url){
		return Jsoup.parse(file.html(), url);
	}
	
	/**
	 * Retrieves the relevant links of a page
	 * @param doc	Document object, parsed by the jSoup library.
	 * @return A List of LinkStore objects.
	 */
	public List<LinkStore> links(Document doc, List<String> visited){
		List<LinkStore> list = new ArrayList<LinkStore>();
		List<LinkStore> listNot = new ArrayList<LinkStore>();
		
		if(doc != null){
			Elements links = (Elements) doc.select("a[href]");
			for(Element link : links){
				if(!visited.contains(link.attr("abs:href"))){
					LinkStore linkS = new LinkStore();
					linkS.setUrl(link.attr("abs:href"));
					linkS.setText(link.text());
					
					if(this.relevantPLink(linkS.getText()) || this.relevantPLink(linkS.getUrl())){
						list.add(linkS);
					} else {
						listNot.add(linkS);
					}
				}
			}

		}
		list.addAll(listNot);
		return list;
	}
	
	/**
	 * Analyzes a string of text for relevant terms
	 * @param text The String to be analyzed
	 * @return True if the string contains relevant terms
	 */
	public boolean relevantPLink(String text){
		text = text.toLowerCase();
		return (
				text.contains("account") ||
				text.contains("agreement") ||
				text.contains("continue") ||
				text.contains("deactivat") ||
				text.contains("dead") ||
				text.contains("death") ||
				text.contains("deceased") ||
				text.contains("delete") ||
				text.contains("help") ||
				text.contains("memorializing") ||
				text.contains("polic") ||
				text.contains("privacy") ||
				text.contains("termination") ||
				text.contains("terms") ||
				text.contains("terms of use") ||
				text.contains("terms of service") ||
				text.contains("tos") ||
				text.contains("user agreement") ||
				text.contains("violation")
				);
		
	}
	
	/**
	 * Analyzes a string of text for relevant terms
	 * @param text The String to be analyzed
	 * @return True if the string contains relevant terms
	 */
	public boolean relevantTitle(String text){
		text = text.toLowerCase();
		return (
				(text.contains("deactivat") && text.contains("account")) ||
				text.contains("dead") ||
				text.contains("death") ||
				text.contains("deceased") ||
				(text.contains("delet") && text.contains("account")) ||
				(text.contains("memorializ") && text.contains("account")) ||
				text.contains("termination") ||
				text.contains("terms") ||
				text.contains("terms of use") ||
				text.contains("terms of service") ||
				text.contains("user agreement")
				);
		
	}
	
	/**
	 * Analyzes a string of text for relevant terms
	 * @param text The String to be analyzed
	 * @return True if the string contains relevant terms
	 */
	public boolean relevantLink(String text){
		text = text.toLowerCase();
		return (
				(text.contains("deactivat") && text.contains("account")) ||
				text.contains("dead") ||
				text.contains("death") ||
				text.contains("deceased") ||
				(text.contains("delet") && text.contains("account")) ||
				(text.contains("memorializ") && text.contains("account")) ||
				text.contains("termination") ||
				text.contains("terms") ||
				text.contains("terms of use") ||
				text.contains("terms of service") ||
				text.contains("user agreement")
				);
		
	}
	
}
