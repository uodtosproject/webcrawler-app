package br.com.betohayasida.webcrawler.Modules;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class TextProcessingModule {
	
	public String clean(Document page){
		String cleaned = new String();
		Whitelist myWhitelist = new Whitelist();
		
		page.select("form").remove();
		page.select("script").remove();

		myWhitelist.addTags("div","p", "a", "b", "i", "br", "h1", "h2", "h3", "h4", "h5", "h6");
		myWhitelist.addAttributes("a", "href");
		
		cleaned = Jsoup.clean(page.html(), myWhitelist);
		cleaned = cleaned.replace("&nbsp;", " ");
		
		return cleaned;
	}
	
	public String process(Document page){
		String processed = new String();
		Elements elems = page.getAllElements();
		for(Element item : elems){
			if(item.text().contains("death")){
				System.out.println(item.html());
				System.out.println();
			}
		}
		return processed;
	}
}
