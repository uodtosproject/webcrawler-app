package br.com.betohayasida.webcrawler.Tools;


/**
 * Class for storing items of the URL Priority Queue
 */
public class URLItem {
	private String url;
	private int score;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}