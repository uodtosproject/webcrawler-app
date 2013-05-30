package br.com.betohayasida.webcrawler.Modules;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.*;

import br.com.betohayasida.webcrawler.Exceptions.DownloadException;
import br.com.betohayasida.webcrawler.Store.HTMLPage;

/**
 * Class responsible for downloading pages and headers.
 */
public class HTTPModule {
	
	/**
	 * Download an URL and save it as an HTMLPage Object
	 * @param url URL of the page
	 * @return An HTMLPage object with the page downloaded.
	 * @throws DownloadException
	 */
	public HTMLPage download(URL url) throws DownloadException{
        String inputLine;
        HTMLPage file = new HTMLPage(url); 
        
        // BEGIN Connect to URL
        try {
        	
	        URLConnection uc = url.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
	        
	        // Write to HTMLPage object
        	while ((inputLine = in.readLine()) != null) {
        		if(!inputLine.trim().isEmpty()){
	        		file.addLine(inputLine.trim());
        		}
        	}

	        in.close();
	        
        } catch (Exception e){
        	//System.out.println(e.getMessage());
        	//throw new DownloadException("Failed while connecting to the URL");
        }
        // END Connect to URL
        
        return file;
	}
	
	/**
	 * Checks the validity of the URL, through HTTP Headers.
	 * @param url URL of the page
	 * @return True if the URL leads to a valid page
	 */
	public boolean checkHeaders(URL url){
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection connection = null;
        boolean valid = false;
        String header = null;
        
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		if(connection != null){
			
			header = connection.getHeaderField(0);
			if(header!=null){
				valid = header.toLowerCase().contains("200") || header.toLowerCase().contains("302") || header.toLowerCase().contains("301");
			} else {
				valid = false;
			}
			
		}

		if(valid){
			Set<String> list = connection.getHeaderFields().keySet();
			Map<String,List<String>> headerF = connection.getHeaderFields();
	        for(String item : list){
	        	System.out.println(item + ": " + headerF.get(item));
	        }
	
			final DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		    Calendar c = new GregorianCalendar();
		    c.setTimeInMillis(connection.getLastModified());
		    System.out.println("Last Modified: " + dateFormat.format(c.getTime()));
		} else {
			System.out.println("Invalid URL");
		}

        return valid;
    }
}
