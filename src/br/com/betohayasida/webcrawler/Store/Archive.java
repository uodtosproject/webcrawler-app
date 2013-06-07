package br.com.betohayasida.webcrawler.Store;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Archive {
	private List<Site> archive = null;
	
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
    		Element root = doc.createElement("archive");
    		doc.appendChild(root);
    		
    		for(Site site : archive){
	    		Element siteElem = doc.createElement("item");
	    		siteElem.setAttribute("URL", site.getUrl());
	    		siteElem.setAttribute("id", site.getName());
	    		siteElem.setAttribute("visitedOn", site.getVisitedOn());
	    		siteElem.setAttribute("archived", (site.isArchived()) ? "true" : "false");
	    		root.appendChild(siteElem);
	    		
	    		for(Page p : site.getPages()){
		    		Element page = doc.createElement("page");
		    		page.setAttribute("name", p.getName());
		    		page.setAttribute("parent", p.getParent());
		    		page.setAttribute("retrievedOn", p.getRetrievedOn());
		    		page.setAttribute("title", p.getTitle());
		    		page.setAttribute("url", p.getUrl());
		    		page.appendChild(doc.createTextNode(p.getText()));
		    		siteElem.appendChild(page);
	    		}
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
    		
		} catch (ParserConfigurationException e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Print the entry to XML
	 * @param response HttpServletResponse object
	 */
	public void printToJSON(HttpServletResponse response){
		response.setContentType("application/json;charset=UTF-8");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try {
			PrintWriter writer = response.getWriter();
			writer.write(gson.toJson(this.archive));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public List<Site> getSites() {
		return archive;
	}

	public void setSites(List<Site> sites) {
		this.archive = sites;
	}
}
