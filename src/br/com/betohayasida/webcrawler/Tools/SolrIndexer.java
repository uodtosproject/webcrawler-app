package br.com.betohayasida.webcrawler.Tools;


import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

public class SolrIndexer {
	static String DEFAULT_URL = "http://localhost:8983/solr";
	Logger logger = Logger.getLogger(SolrIndexer.class);
	Appender myAppender = new ConsoleAppender(new SimpleLayout());
	SolrServer solr = null;
	
	public void init(){

	    BasicConfigurator.configure();
	    logger.setLevel(Level.ALL);
		myAppender.setLayout(new SimpleLayout());
	    logger.addAppender(myAppender);

	}
	
	public void open(String urlString){
		init();
		solr = new HttpSolrServer((urlString == null)? DEFAULT_URL : urlString);
	}

	@SuppressWarnings("unchecked")
	public String add(JSONObject doc){
		String r = null;
		
		SolrInputDocument document = new SolrInputDocument();
		Iterator<Map.Entry<String, JSONArray[]>> i = doc.entrySet().iterator();
		while (i.hasNext()) {
		   Map.Entry<String, JSONArray[]> e = i.next();
		   document.addField(e.getKey(), e.getValue());
		}
		try {
			UpdateResponse response = solr.add(document);
			r = response.toString();
		} catch (Exception e) {
			r = e.getMessage();
		}
		
		return r;
	}
	
	public boolean commit(){
		boolean r = false;
		try{
			solr.commit();
			r = true;
		} catch(Exception e) {}
		return r;
	}
}
