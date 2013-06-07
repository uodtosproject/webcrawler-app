package br.com.betohayasida.webcrawler.GSearch;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import br.com.betohayasida.webcrawler.Store.Link;

public class GoogleSearch {
	
	/**
	 * Retrieves links from a Google Search through its API
	 * @param query String to be searched
	 * @return A List<Link> with the results
	 */
	public List<Link> search(String query){
		List<Link> list = new ArrayList<Link>();
	    String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
	    String search = query;
	    String charset = "UTF-8";

	    URL url;
		try {
			url = new URL(google + URLEncoder.encode(search, charset));

		    Reader reader;
			try {
				reader = new InputStreamReader(url.openStream(), charset);
			    GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

			    List<Result> resultsList = results.getResponseData().getResults();
			    for(Result r : resultsList){
			    	Link link = new Link();
			    	link.setText(r.getTitle());
			    	link.setUrl(r.getUrl());
			    	System.out.println(" +g " + link.getUrl());
			    	list.add(link);
			    }
			    
			} catch (Exception e) {
				//e.printStackTrace();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	    
	    return list;
	}

}
