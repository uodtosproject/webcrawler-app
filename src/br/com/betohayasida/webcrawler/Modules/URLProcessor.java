package br.com.betohayasida.webcrawler.Modules;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.validator.routines.UrlValidator;

import br.com.betohayasida.webcrawler.Exceptions.InvalidURLException;

/**
 * Module responsible for handling URLs.
 */
public class URLModule {
    
	/**
	 * Creates an URL object, after canonicalising and validating the address.
	 * @param sUrl String containing the URL
	 * @return URL object or null if invalid
	 * @throws InvalidURLException
	 */
	public URL create(String sUrl) throws InvalidURLException{
		URL url = null;
		
		// Canonicalise the URL
		
		String cUrl = this.canonicalise(sUrl);
		
		if(cUrl != null){
			// Check if it's valid
			boolean valid = this.valid(cUrl); 
			
			if(valid){
				
				// Create URL object
				try {
					url = new URL(cUrl);
				} catch (MalformedURLException e) {
					//e.printStackTrace();
				}
			} else {
				//throw new InvalidURLException("Invalid URL");
			}
		}
		return url;
	}
	
	/**
	 * Get the domain of an URL by removing protocols, www, and file paths.
	 * @param url String of the complete URL
	 * @return String with the domain
	 */
	public String getDomain(String url){
		
		url = url.replace("http://", "");
		url = url.replace("https://", "");
		url = url.replace("www", "");
		
		String[] strips = url.split("/");
		if(strips.length >= 2){
			url = strips[0];
		}
		
		return url;
	}
	
	/**
	 * Validates an URL
	 * @param url String containg the URL
	 * @return True for valid URL
	 */
	public boolean valid(String url){
		String[] schemes = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
		UrlValidator urlValidator = new UrlValidator(schemes);

		return urlValidator.isValid(url);
	}
	
	/**
	 * Canonicalises the URL.
	 * @param URL String containing the URL
	 * @return String with canonicalised URL
	 */
	private String canonicalise(String URL){
		String cURL = URL;
		
		if(!URL.contains("lang=")){
			// Remove anchors
			if(cURL.contains("#")){
				cURL = cURL.substring(0, cURL.indexOf("#"));
			}
			
			// Remove anchors
			if(cURL.endsWith("=")){
				cURL = cURL.substring(0, cURL.indexOf("?") - 1);
			}
			
			// Remove anchors
			if(cURL.endsWith("/")){
				cURL = cURL.replace(".com/", ".com");
				cURL = cURL.replace(".co.uk/", ".co.uk");
				cURL = cURL.replace(".net/", ".net");
				cURL = cURL.replace(".org/", ".org");
			}
					
			// Encoding
			cURL = this.encode(cURL);
			
			
			// Fix protocol
			if(!(cURL.startsWith("http://") || cURL.startsWith("https://"))){
				if(cURL.contains("twitter") || cURL.contains("facebook") || cURL.contains("foursquare")){
					cURL = "https://".concat(cURL);
				} else {
					cURL = "http://".concat(cURL);
				}
			}
			
			// Twitter-fix 
			if(cURL.contains("www.twitter")){
				cURL = cURL.replace("www.twitter", "twitter");
			} 
			// Foursquare-fix
			else if(cURL.contains("www.foursquare")){
				cURL = cURL.replace("www.foursquare", "foursquare");
			} 
			
			// Google-fix
			else if(cURL.contains("http://www.google.co.uk/intl/en/policies/terms/")){
				cURL = cURL.replace("http://www.google.co.uk/intl/en/policies/terms/", "http://www.google.com/intl/en/policies/terms/");
			}
			else if(cURL.equalsIgnoreCase("https://accounts.google.com/tos?hl=en")){
				cURL = "http://www.google.co.uk/intl/en/policies/terms/regional.html";
			}
			
			// Convert to lowercase
			String[] strips = cURL.split("//");
			if(strips.length > 2){
				String[] strips2 = strips[1].split("/");
				cURL = strips[0].concat("//" + strips2[0].toLowerCase() + "/");
				int size = strips2.length - 1;
				int i = 0;
				for(i = 1; i <= size; i++){
					cURL = cURL.concat(strips2[i]);
				}
				if(strips.length >= 3){
					size = strips.length;
					for(i = 2; i < size; i++){
						cURL = cURL.concat("//" + strips[i]);
					}
				}
			}
	
			
			// Eliminate /?
			if(cURL.contains("/?")){
				String[] strips3 = cURL.split("/\\?");
				if(strips3.length >= 2){
					cURL = strips3[0];
				}
			}
		} else {
			cURL = null;
		}
		
		return cURL;
	}
	
	/**
	 * Encodes the URL
	 * @param URL String containing the URL
	 * @return String with encoded URL
	 */
	private String encode(String URL){
		String encodedURL = URL;
		if(!URL.contains("&amp;") && !URL.contains("%")){
			if(URL.contains("?")){
				// Break the string in two parts, before and after the "?"
				String subURL = URL.substring(URL.indexOf("?"));
				String newURL = URL.substring(0, URL.indexOf("?"));
				
				// Replace characters with ASCII
				subURL = subURL.replace("%","%25");
				subURL = subURL.replace(" ","%20");
				subURL = subURL.replace("!","%21");
				subURL = subURL.replace("\"","%22");
				subURL = subURL.replace("$","%24");
				subURL = subURL.replace("&","%26");
				subURL = subURL.replace("\'","%27");
				subURL = subURL.replace("(","%28");
				subURL = subURL.replace(")","%29");
				subURL = subURL.replace("*","%2A");
				subURL = subURL.replace("+","%2B");
				subURL = subURL.replace(",","%2C");
				subURL = subURL.replace("-","%2D");
				subURL = subURL.replace(".","%2E");
				subURL = subURL.replace("/","%2F");
				
				// Put the two parts together again
				encodedURL = newURL.concat(subURL);
			}
		}
		return encodedURL;
	}

}
