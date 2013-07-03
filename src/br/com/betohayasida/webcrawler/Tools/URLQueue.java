package br.com.betohayasida.webcrawler.Tools;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import br.com.betohayasida.webcrawler.Modules.LogProducer;
import br.com.betohayasida.webcrawler.Modules.MyLogger;
import br.com.betohayasida.webcrawler.Store.Link;

/**
 * Implementation of a priority queue for URLs.
 */
public class URLQueue extends LogProducer {
	private Comparator<URLItem> comparator = new URLComparator();
    private PriorityQueue<URLItem> queue = new PriorityQueue<URLItem>(20, comparator);
    
    public URLQueue(){
    	this.logger = new MyLogger("output-urlqueue.txt");
    }
    
    public URLQueue(MyLogger logger){
    	this.logger = logger;
    	this.debug = true;
    }
    
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
		log("URL(" + urlS.getUrl() + ") added to queue, with score " + score, "URLQueue.add");
	}
	
	/**
     * Adds a list of URLs to the queue.
     * @param list List of URLs.
     * @param score Score of the links' relevance.
     */
	public void add(List<Link> list, int score){
		for(Link ls : list){
			URLItem url = new URLItem();
			url.setUrl(ls.getUrl());
			url.setScore(score);
			queue.add(url);
			log("URL(" + url.getUrl() + ") added to queue, with score " + score, "URLQueue.add");
		}
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
		log("Still " + queue.size() + " in queue", "URLQueue.next");
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
