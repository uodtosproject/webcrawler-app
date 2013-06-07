package br.com.betohayasida.webcrawler.Tools;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class Common {
	/**
	 * Generate a unique filename based on the URL using MD5.
	 * @param url	URL of the page.
	 * @return	A String with the hashed filename.
	 */
	public static String hashMD5(URL url){
		String filename = url.toString();
		MessageDigest md;
		
		// Hash the URL through MD5
		try {
			md = MessageDigest.getInstance("MD5");
			filename = (new HexBinaryAdapter()).marshal(md.digest(url.toString().getBytes()));
		} catch (NoSuchAlgorithmException e) {
			// e.printStackTrace();
		}
		
		return filename;
	}
	
	/**
	 * Generate a unique filename based on the URL using MD5.
	 * @param url	URL of the page.
	 * @return	A String with the hashed filename.
	 */
	public static String hashMD5(String text){
		MessageDigest md;
		String r = text;
		
		// Hash the String through MD5
		try {
			md = MessageDigest.getInstance("MD5");
			r = (new HexBinaryAdapter()).marshal(md.digest(text.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			// e.printStackTrace();
		}
		
		return r;
	}
}
