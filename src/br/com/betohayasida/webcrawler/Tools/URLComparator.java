package br.com.betohayasida.webcrawler.Tools;

import java.util.Comparator;


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
