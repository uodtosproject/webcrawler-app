package br.com.betohayasida.webcrawler.Store;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

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

public class ToSStore {
	private String filename;
	private String url;
	private String originUrl;
	private String source;
	private String text;
	private String retrievedOn;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOriginUrl() {
		return originUrl;
	}
	public void setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
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
    		rootElement.setAttribute("id", filename);
    		if(archive) rootElement.setAttribute("archived", "true");
    		else rootElement.setAttribute("archived", "false");
    		doc.appendChild(rootElement);
    		
    		Element originURL = doc.createElement("URL");
    		originURL.appendChild(doc.createTextNode(this.getOriginUrl()));
    		rootElement.appendChild(originURL);
    		
    		Element URL = doc.createElement("ToS_URL");
    		URL.appendChild(doc.createTextNode(this.getUrl()));
    		rootElement.appendChild(URL);
    		
    		Element Text = doc.createElement("text");
    		Text.appendChild(doc.createTextNode(this.getText()));
    		rootElement.appendChild(Text);
    		
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
			JToSStore jTos = new JToSStore(this.filename, this.url, this.originUrl, this.text, archived);
			writer.write(gson.toJson(jTos));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
