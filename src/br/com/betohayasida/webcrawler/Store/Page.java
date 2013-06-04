package br.com.betohayasida.webcrawler.Store;

import java.util.Date;

public class Page {
	private String parentName;
	private String url;
	private String source;
	private String text;
	private String retrievedOn;
	
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getRetrievedOn() {
		Date expiry = new Date(Long.parseLong(retrievedOn));
		return expiry.toString();
	}
	public Long getRetrievedOnMili() {
		return Long.parseLong(retrievedOn);
	}
	public void setRetrievedOn(String retrievedOn) {
		this.retrievedOn = retrievedOn;
	}

}
