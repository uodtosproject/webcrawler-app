package br.com.betohayasida.webcrawler.Store;

public class JToSStore {
	public String filename;
	public String url;
	public String originUrl;
	public String text;
	public boolean archived;
	
	public JToSStore(String filename, String url, String originUrl, String text, boolean archived){
		this.filename = filename;
		this.url = url;
		this.originUrl = originUrl;
		this.text = text;
		this.archived = archived;
	}

}
