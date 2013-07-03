package br.com.betohayasida.webcrawler.Modules;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt")), true));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = "https://www.twitter.com";
		Crawler crawler = new Crawler();
		try {
			crawler.crawl(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
