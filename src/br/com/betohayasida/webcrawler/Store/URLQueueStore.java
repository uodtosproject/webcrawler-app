package br.com.betohayasida.webcrawler.Store;

import java.net.URL;

/**
 * Class for storing items of the URL Priority Queue
 */
public class URLQueueStore {
	private URL url;
	private int score;
	
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}