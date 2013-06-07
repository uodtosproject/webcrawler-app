package br.com.betohayasida.webcrawler.Modules;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "https://www.google.com";
		Crawler crawler = new Crawler();
		try {
			crawler.crawl(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
