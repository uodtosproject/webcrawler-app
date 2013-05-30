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
public class AnalysisModule {
	
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
			
			if(
					title.text().toLowerCase().contains("terms of service") ||
					title.text().toLowerCase().contains("terms of use") ||
					title.text().toLowerCase().contains("terms and conditions") ||
					title.text().toLowerCase().contains("user agreement")
			){
				targetPage = true;
			}
		}
		// END Check page title
		
		// BEGIN Check URL
		if(!moved && !targetPage){
			if(
					url.toLowerCase().contains("terms") ||
					(url.toLowerCase().contains("user") && url.toLowerCase().contains("agreement")) ||
					url.toLowerCase().contains("/tos")
			){
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
					
					if(
							link.attr("abs:href").toLowerCase().contains("/terms") ||
							link.attr("abs:href").toLowerCase().contains("/tos") ||
							link.attr("abs:href").toLowerCase().contains("agreement") ||
							(link.attr("abs:href").toLowerCase().contains("continue") && link.text().toLowerCase().contains("here"))||
							link.text().toLowerCase().contains("terms of use") ||
							link.text().toLowerCase().contains("terms of service") ||
							link.text().toLowerCase().contains("terms") ||
							link.text().toLowerCase().contains("user agreement")
					){
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

}
