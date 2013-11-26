package br.com.betohayasida.webcrawler.Store;

import java.util.Date;

/**
 * Class for storing Site Entries
 * @author rkhayasidajunior
 *
 */
public class SiteEntry {
	protected String name = null;
	protected String url = null;
	protected String visitedOn = null;
	protected String domain = null;
	protected String ico = null;
	
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
	public String getDomain(){
		return this.domain;
	}
	public void setDomain(String domain){
		this.domain = domain;
	}
	public String getIco(){
		return this.ico == null? "" : this.ico;
	}
	public void setIco(String ico){
		this.ico = ico;
	}
}
