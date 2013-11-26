package br.com.betohayasida.tos.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.betohayasida.webcrawler.Modules.Crawler;
import br.com.betohayasida.webcrawler.Store.Site;
import br.com.betohayasida.webcrawler.Store.Status;

/**
 * Controller for REST calls
 * @author rkhayasidajunior
 *
 */
public class Rest extends HttpServlet {
	private static final long serialVersionUID = -3682096299146791172L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		String URI = request.getRequestURI();
    	String[] URIparts = URI.split("/");
		if(URIparts[2].equalsIgnoreCase("crawl")){
			StringBuffer jb = new StringBuffer();
			String line = null;
			Status status = new Status();
			status.setStatus("Created");
			
			try {
				BufferedReader reader = request.getReader();
			    while ((line = reader.readLine()) != null)
			    	jb.append(line);
			} catch (Exception e) { /*report an error*/ }
			
			if(jb != null && jb.length() > 0){
				String url = jb.toString();
				Site site = null;
				
				Crawler crawler = new Crawler();
				try {
					
					site  = crawler.crawl(url);
	
					if(site != null){
						status.setStatus("OK");
						status.setMessage(site.getUrl());
					} else {
						status.setStatus("Failed");
						status.setMessage("Failed to crawl");
					}
					
				} catch (Exception e) {}
			}
			
			response.setContentType("application/json;charset=UTF-8");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			try {
				PrintWriter writer = response.getWriter();
				writer.write(gson.toJson(status));
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}