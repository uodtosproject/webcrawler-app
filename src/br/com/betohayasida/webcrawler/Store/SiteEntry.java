package br.com.betohayasida.webcrawler.Store;

import java.util.Date;

public class SiteEntry {
	protected String name = null;
	protected String url = null;
	protected String visitedOn = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getVisitedOnString() {
		Date expiry = new Date(Long.parseLong(this.visitedOn));
		return expiry.toString();
	}
	public Long getVisitedOnMili() {
		return Long.parseLong(this.visitedOn);
	}
	public String getVisitedOn() {
		return this.visitedOn;
	}
	public void setVisitedOn(String visitedOn) {
		this.visitedOn = visitedOn;
	}
}
