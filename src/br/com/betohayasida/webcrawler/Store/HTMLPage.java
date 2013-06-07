package br.com.betohayasida.webcrawler.Store;

import java.net.URL;
import java.util.ArrayList;

/**
 * Class for storing source code of HTML pages.
 */
public class HTMLPage {
	private ArrayList<String> content = new ArrayList<String>();
	private URL url;
	
	/**
	 * Constructor.
	 * @param url	URL of the page
	 */
	public HTMLPage(URL url){
		this.url = url;
	}
	
	/**
	 * Get the page's URL.
	 * @return The page's URL.
	 */
	public URL url() {
		return url;
	}
	
	/**
	 * Add a line to the source code.
	 * @param line A String with the line.
	 */
	public void addLine(String line){
		this.content.add(line);
	}
	
	/**
	 * Get a line of the source code.
	 * @param line	The number of the line.
	 * @return A String with the line.
	 */
	public String getLine(int line){
		return this.content.get(line);
	}
	
	/**
	 * Returns the number of lines of the page.
	 * @return Number of lines.
	 */
	public int size(){
		return this.content.size();
	}
	
	/**
	 * Print the contents of the page to the console.
	 */
	public void print(){
		for(String item : this.content){
			System.out.println(item);
		}
	}
	
	/**
	 * Creates a String containing all the content of the page.
	 * @return A String with the source code of the page.
	 */
	public String html(){
		String r = "";
		for(String item : this.content){
			r = r.concat(item);
		}
		return r;
	}
	
}
