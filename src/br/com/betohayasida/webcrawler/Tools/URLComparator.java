package br.com.betohayasida.webcrawler.Modules;

import java.util.Comparator;

import br.com.betohayasida.webcrawler.Store.URLItem;

/**
 * Implementation of Comparator Interface for URLQueueStore
 */
public class URLComparator implements Comparator<URLItem> {

	@Override
	public int compare(URLItem x, URLItem y) {
		int i = 0;
	    if (x.getScore() < y.getScore()){
	    	i = 1;
	    }
	    if (x.getScore() > y.getScore()){
	    	i = -1;
	    }
	    return i;
	}

}
