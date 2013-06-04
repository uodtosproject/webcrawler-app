package br.com.betohayasida.webcrawler.Modules;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import br.com.betohayasida.webcrawler.Store.GoogleResults;
import br.com.betohayasida.webcrawler.Store.LinkStore;
import br.com.betohayasida.webcrawler.Store.Result;

public class GoogleSearch {

	public static void main(String[] args) throws Exception{
		GoogleSearch s = new GoogleSearch();
		List<LinkStore> list = s.search("facebook.com deceased");
		for(LinkStore link : list){
			System.out.println(link.getText() + ": " + link.getUrl());
		}
	}
	
	public List<LinkStore> search(String query) throws Exception {
		List<LinkStore> list = new ArrayList<LinkStore>();
	    String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
	    String search = "http://www.facebook.com" + " deceased";
	    String charset = "UTF-8";

	    URL url = new URL(google + URLEncoder.encode(search, charset));
	    Reader reader = new InputStreamReader(url.openStream(), charset);
	    GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

	    // Show title and URL of 1st result.
	    List<Result> resultsList = results.getResponseData().getResults();
	    for(Result r : resultsList){
	    	LinkStore link = new LinkStore();
	    	link.setText(r.getTitle());
	    	link.setUrl(r.getUrl());
	    	list.add(link);
	    }
	    
	    return list;
	}

}
