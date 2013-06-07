package br.com.betohayasida.webcrawler.Modules;

import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.*;

import net.sourceforge.jrobotx.RobotExclusion;

import br.com.betohayasida.webcrawler.Exceptions.DownloadException;
import br.com.betohayasida.webcrawler.Exceptions.InvalidURLException;
import br.com.betohayasida.webcrawler.Store.HTTPHeader;
import br.com.betohayasida.webcrawler.Store.HTMLPage;
import br.com.betohayasida.webcrawler.Tools.URLQueue;

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
	 * Validates the URL by checking the robots.txt and the HTTP Headers Status codes
	 * @param iUrl URL to be validated
	 * @param queue URLQueue
	 * @return A valid URL object or null if iUrl is invalid
	 * @throws InvalidURLException
	 * @throws IOException
	 */
	public URL validate(URL iUrl, URLQueue queue) throws InvalidURLException, IOException{
		URL url = iUrl;
		HTTPHeader header = new HTTPHeader();

		// Check robots.txt
		if(this.checkRobots(url) || url.toString().contains("facebook")){

			// Check HTTP headers
			if(this.checkHeaders(url, header)){

				// If it's 302 or 301
				if(header.getCode() == 303 || header.getCode() == 302 || header.getCode() == 301){

					queue.add(header.getLocation(), Crawler.MaxIterations);
					url = null;
					
				}
				
			} else {
				System.out.println("Invalid HTTP Headers for " + url + " " + header.getCode());
				url = null;
			}
			
		} else {
			System.out.println("Robot.txt does not allow this page to be crawled");
			url = null;
		}
		return url;
	}
	
	/**
	 * Checks the validity of the URL, through HTTP Headers.
	 * @param url URL of the page
	 * @return True if the URL leads to a valid page
	 * @throws IOException 
	 */
	public boolean checkHeaders(URL url, HTTPHeader code) throws IOException{
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection connection = null;
        boolean valid = false;
        boolean print = false;
        String header = null;
        Integer[] acceptableStatusCodes = new Integer[]{200, 201, 202, 301, 302, 303};
        List<Integer> listAcceptableStatusCodes = new ArrayList<Integer>(Arrays.asList(acceptableStatusCodes));
        
		connection = (HttpURLConnection) url.openConnection();
		
		if(connection != null){
			
			header = connection.getHeaderField(0);
			if(header!=null){
				int codeNumber = Integer.parseInt(header.split(" ")[1]);
				code.setCode(codeNumber);
				valid = listAcceptableStatusCodes.contains(codeNumber);
				
				if(valid){
					if(codeNumber == 302 || codeNumber == 301 || codeNumber == 303){
						code.setLocation(connection.getHeaderField("location"));
					}
				}
			}
			
			if(print){
				Set<String> list = connection.getHeaderFields().keySet();
				Map<String,List<String>> headerF = connection.getHeaderFields();
	        	System.out.println();
		        for(String item : list){
		        	System.out.println(item + ": " + headerF.get(item));
		        }
	        	System.out.println();
			}
			
		}

        return valid;
    }
	
	/**
	 * Checks if it's permitted to crawl an URL
	 * @param url URL to be crawled
	 * @return True, if robots.txt allows it
	 */
	public boolean checkRobots(URL url){
		String UserAgent = "Java/1.6.0_26";
		RobotExclusion robotExclusion = new RobotExclusion();
		return robotExclusion.allows(url, UserAgent);
	}
}
