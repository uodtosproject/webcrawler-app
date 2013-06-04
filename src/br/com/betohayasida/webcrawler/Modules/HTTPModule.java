package br.com.betohayasida.webcrawler.Modules;
import java.net.*;
import java.io.*;

import net.sourceforge.jrobotx.RobotExclusion;

import br.com.betohayasida.webcrawler.Exceptions.DownloadException;
import br.com.betohayasida.webcrawler.Store.HTTPHeader;
import br.com.betohayasida.webcrawler.Store.HTMLPage;

/**
 * Class responsible for downloading pages and headers.
 */
public class HTTPModule {
	
	public static void main(String[] args) throws MalformedURLException{
		HTTPModule m = new HTTPModule();
		URL url = new URL("http://www.betohayasida.com.br/cgi-bin/");
		if(m.checkRobots(url)){
			System.out.println("True");
		} else {
			System.out.println("False");
		}
	}
	
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
	public boolean checkHeaders(URL url, HTTPHeader code){
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
			code.setCode(Integer.parseInt(header.split(" ")[1]));
			if(code.getCode() == 302 || code.getCode() == 301){
				code.setLocation(connection.getHeaderField("location"));
			}
		}
		/*if(valid){
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
		}*/

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
