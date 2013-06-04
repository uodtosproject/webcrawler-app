package br.com.betohayasida.webcrawler.Store;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.betohayasida.webcrawler.Store.Page;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TOS {
	private String name;
	private String url;
	private List<Page> pages = new ArrayList<Page>();
	private String visitedOn;
	
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
	public void addPage(Page page){
		this.pages.add(page);
	}
	public Page getPage(String url){
		Page page = null;
		for(Page p : this.pages){
			if(p.getUrl().equalsIgnoreCase(url)){
				page = p;
			}
		}
		return page;
	}
	public List<Page> getPages(){
		return this.pages;
	}
	public void setPages(List<Page> list){
		for(Page p : list){
			this.pages.add(p);
		}
	}
	public String getVisitedOn() {
		Date expiry = new Date(Long.parseLong(visitedOn));
		return expiry.toString();
	}
	public Long getVisitedOnMili() {
		return Long.parseLong(visitedOn);
	}
	public void setVisitedOn(String visitedOn) {
		this.visitedOn = visitedOn;
	}
	
	/**
	 * 
	 * @param response
	 * @param archive
	 */
	public void printToXML(HttpServletResponse response, boolean archive){
		response.setContentType("text/xml;charset=UTF-8");
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		     
    		// root elements
    		Document doc = docBuilder.newDocument();
    		
    		Element rootElement = doc.createElement("tos");
    		rootElement.setAttribute("URL", this.url);
    		rootElement.setAttribute("id", this.name);
    		if(archive) rootElement.setAttribute("archived", "true");
    		else rootElement.setAttribute("archived", "false");
    		doc.appendChild(rootElement);
    		
    		for(Page p : this.pages){
	    		Element page = doc.createElement("page");
	    		page.setAttribute("url", p.getUrl());
	    		page.setAttribute("retrievedOn", p.getRetrievedOn());
	    		page.appendChild(doc.createTextNode(p.getText()));
	    		rootElement.appendChild(page);
    		}
    		
    		// write the content into xml file
    		TransformerFactory transformerFactory = TransformerFactory.newInstance();
    		Transformer transformer;
			try {
				transformer = transformerFactory.newTransformer();
	    		DOMSource source = new DOMSource(doc);
	    		StreamResult result = new StreamResult(response.getWriter());
	     
	    		transformer.transform(source, result);
	    		response.getWriter().close();
	    		
			} catch (TransformerConfigurationException e) {
				//e.printStackTrace();
			} catch (TransformerException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
    		
		} catch (ParserConfigurationException e) {
			//e.printStackTrace();
		}
	}
	public void printToJSON(HttpServletResponse response, boolean archived){
		response.setContentType("application/json;charset=UTF-8");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try {
			PrintWriter writer = response.getWriter();
			writer.write(gson.toJson(this));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
