package br.com.betohayasida.webcrawler.Modules;

import java.util.Comparator;
import java.util.PriorityQueue;

import br.com.betohayasida.webcrawler.Store.URLItem;

/**
 * Implementation of a priority queue for URLs.
 */
public class URLQueue {
	private Comparator<URLItem> comparator = new URLComparator();
    private PriorityQueue<URLItem> queue = new PriorityQueue<URLItem>(20, comparator);
    
    /**
     * Adds an URL to the queue.
     * @param url URL of the page.
     * @param score Score of the link's relevance.
     */
	public void add(String url, int score){
		URLItem urlS = new URLItem();
		urlS.setUrl(url);
		urlS.setScore(score);
		
		queue.add(urlS);
	}
	
	/**
	 * Retrieves the first URL of the queue.
	 * @return The URL of the first link.
	 */
	public String next(){
		String url = null;
		URLItem urlS = queue.poll();
		if(urlS != null){
			url = urlS.getUrl();
		}
		return url;
	}
	
	/**
	 * Checks if the queue is empty.
	 * @return True if the queue is empty.
	 */
	public boolean empty(){
		boolean empty = false;
		if(this.queue.size() == 0){
			empty = true;
		}
		return empty;
	}
	
}
