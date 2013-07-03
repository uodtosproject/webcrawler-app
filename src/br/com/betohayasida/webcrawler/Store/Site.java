package br.com.betohayasida.webcrawler.Store;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.betohayasida.webcrawler.DB.ArchiveStorage;
import br.com.betohayasida.webcrawler.DB.SiteStorage;


public class Site extends SiteEntry {
	private static SiteStorage siteStorage = new SiteStorage();
	private static ArchiveStorage archiveStorage = new ArchiveStorage();
	private List<Page> pages = new ArrayList<Page>();
	private boolean archive = false;
	
	/**
	 * Returns a List of Site objects retrieved from the DB
	 * @param limit Number limit of items
	 * @return List<Site> object
	 */
	public static List<Site> last(int limit){
		return siteStorage.last(limit);
	}
	
	/**
	 * Returns a Site object retrieved from the DB
	 * @param url URL to be searched
	 * @return Site object or null
	 */
	public static Site loadByUrl(String url, boolean fromArchive){
		return (fromArchive)? archiveStorage.getByUrl(url) : siteStorage.getByUrl(url);
	}

	/**
	 * Returns a Site object retrieved from the DB
	 * @param name Name to be searched
	 * @return Site object or null
	 */
	public static Site loadByName(String name, boolean fromArchive){
		return (fromArchive)? archiveStorage.getByName(name) : siteStorage.getByName(name);
	}

	/**
	 * Returns a List<Site> object retrieved from the DB
	 * @param name Name to be searched
	 * @return List<Site> object
	 */
	public static List<Site> loadAllByName(String name, boolean fromArchive){
		return (fromArchive)? archiveStorage.getAllByName(name) : siteStorage.getAllByName(name);
	}

	/**
	 * Returns a List<Site> object retrieved from the DB
	 * @param name Name to be searched
	 * @return List<Site> object
	 */
	public static List<Site> loadAllByUrl(String url, boolean fromArchive){
		return (fromArchive)? archiveStorage.getAllByUrl(url) : siteStorage.getAllByUrl(url);
	}
	
	/**
	 * Constructor
	 */
	public Site(){
		this.pages = new ArrayList<Page>();
	}
	
	/**
	 * Constructor that fills the SiteEntry
	 * @param name Name of the entry
	 * @param url URL of the entry
	 */
	public Site(String name, String url){
		this.name = name;
		this.url = url;
	}
	
	/**
	 * Constructor that fills the SiteEntry
	 * @param name Name of the entry
	 * @param url URL of the entry
	 * @param visitedOn Epoch time of visit
	 */
	public Site(String name, String url, String visitedOn){
		this.name = name;
		this.url = url;
		this.visitedOn = visitedOn;
	}

	/**
	 * Constructor that fills the SiteEntry
	 * @param name Name of the entry
	 * @param url URL of the entry
	 * @param visitedOn Epoch time of visit
	 * @param domain Domain of the site
	 */
	public Site(String name, String url, String visitedOn, String domain){
		this.name = name;
		this.url = url;
		this.visitedOn = visitedOn;
		this.domain = domain;
	}
	
	/**
	 * Constructor that fills the SiteEntry for an Archive item
	 * @param name Name of the entry
	 * @param url URL of the entry
	 * @param visitedOn Epoch time of visit
	 * @param domain Domain of the site
	 * @param archived Indicates if it's an archived entry
	 */
	public Site(String name, String url, String visitedOn, String domain, boolean archived){
		this.name = name;
		this.url = url;
		this.visitedOn = visitedOn;
		if(archived) this.archive = true;
	}
	
	/**
	 * Adds one page to the Site
	 * @param page Page to be added
	 */
	public void addPage(Page page){
		this.pages.add(page);
	}
	
	/**
	 * Adds multiple pages to the Site
	 * @param list List<Page> with the pages to be added
	 */
	public void addPages(List<Page> list){
		for(Page p : list){
			this.pages.add(p);
		}
	}
	
	/**
	 * Gets a single page
	 * @param url URL of the page to be returned
	 * @return A Page object, null if it cannot be found
	 */
	public Page getPage(String url){
		Page page = null;
		for(Page p : this.pages){
			if(p.getUrl().equalsIgnoreCase(url)){
				page = p;
			}
		}
		return page;
	}
	
	/**
	 * Retrieves all pages
	 * @return List<Page> object
	 */
	public List<Page> getPages(){
		return this.pages;
	}
	
	/**
	 * Clear the list of pages
	 */
	public void dropPages(){
		this.pages.clear();
	}
	
	/**
	 * Saves the entry in the DB
	 */
	public void save(){
		siteStorage.save(this);
	}
	
	/**
	 * Archive the entry in the DB
	 */
	public void archive(){
		archive = true;
		archiveStorage.save(this);
	}
	
	/**
	 * Checks if the site is archived
	 * @return True if it is archived
	 */
	public boolean isArchived(){
		return this.archive;
	}
	
	/**
	 * Print the entry to XML
	 * @param response HttpServletResponse object
	 */
	public void printToXML(HttpServletResponse response){
		response.setContentType("text/xml;charset=UTF-8");
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		     
    		// root elements
    		Document doc = docBuilder.newDocument();
    		
    		Element root = doc.createElement("site");
    		root.setAttribute("URL", this.url);
    		root.setAttribute("id", this.name);
    		root.setAttribute("visitedOn", this.visitedOn);
    		root.setAttribute("domain", this.domain);
    		root.setAttribute("archived", (this.archive) ? "true" : "false");
    		doc.appendChild(root);
    		
    		for(Page p : this.pages){
	    		Element page = doc.createElement("page");
	    		page.setAttribute("name", p.getName());
	    		page.setAttribute("parent", p.getParent());
	    		page.setAttribute("retrievedOn", p.getRetrievedOn());
	    		page.setAttribute("title", p.getTitle());
	    		page.setAttribute("url", p.getUrl());
	    		page.appendChild(doc.createTextNode(p.getText()));
	    		root.appendChild(page);
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
	    		
			} catch (Exception e) {
				//e.printStackTrace();
			} 
    		
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Print the entry to JSON
	 * @param response HttpServletResponse object
	 */
	public void printToJSON(HttpServletResponse response){
		response.setContentType("application/json;charset=UTF-8");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try {
			PrintWriter writer = response.getWriter();
			writer.write(gson.toJson(this));
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
