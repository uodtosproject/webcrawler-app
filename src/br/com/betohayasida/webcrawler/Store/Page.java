package br.com.betohayasida.webcrawler.Store;

import java.util.Date;

/**
 * Class for storing pages
 * @author rkhayasidajunior
 *
 */
public class Page {
	private String name = null;
	private String parent = null;
	private String retrievedOn = null;
	private String text = null;
	private String title = null;
	private String url = null;
	
	public Page(){
		
	}
	public Page(String name, String parent, String retrievedOn, String text, String title, String url){
		this.title = title;
		this.parent = parent;
		this.name = name;
		this.url = url;
		this.text = text;
		this.retrievedOn = retrievedOn;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
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
