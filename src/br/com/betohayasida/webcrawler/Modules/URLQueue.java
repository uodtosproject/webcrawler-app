package br.com.betohayasida.webcrawler.Modules;

import java.net.URL;
import java.util.Comparator;
import java.util.PriorityQueue;

import br.com.betohayasida.webcrawler.Store.URLQueueStore;

/**
 * Implementation of a priority queue for URLs.
 */
public class URLQueue {
	private Comparator<URLQueueStore> comparator = new URLComparator();
    private PriorityQueue<URLQueueStore> queue = new PriorityQueue<URLQueueStore>(20, comparator);
    
    /**
     * Adds an URL to the queue.
     * @param url URL of the page.
     * @param score Score of the link's relevance.
     */
	public void add(URL url, int score){
		URLQueueStore urlS = new URLQueueStore();
		urlS.setUrl(url);
		urlS.setScore(score);
		
		queue.add(urlS);
	}
	
	/**
	 * Retrieves the first URL of the queue.
	 * @return The URL of the first link.
	 */
	public URL next(){
		URL url = null;
		URLQueueStore urlS = queue.poll();
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
