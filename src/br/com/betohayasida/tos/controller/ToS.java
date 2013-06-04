package br.com.betohayasida.tos.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.betohayasida.webcrawler.Modules.Crawler;
import br.com.betohayasida.webcrawler.Modules.SiteStorage;
import br.com.betohayasida.webcrawler.Store.TOS;

// Extend HttpServlet class
public class ToS extends HttpServlet {
	private static final long serialVersionUID = -3682096299146791172L;
	
	public void init() throws ServletException{
		
  	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		String URI = request.getRequestURI();
    	String[] URIparts = URI.split("/");
    	
    	// PATH: /*
		if(URIparts.length == 3){
			// PATH: /msearch
			if(URIparts[2].equalsIgnoreCase("msearch")){
		    	SiteStorage store = new SiteStorage();
		    	store.connect();
		    	List<TOS> last = store.last(10);
			    RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/tos_multi.jsp");
				request.setAttribute("last", last);
				request.setAttribute("active", "msearch");
				request.setAttribute("title", "Multisearch");
				rd.forward(request,response);
			} 
			
			// PATH: /search or /*
			else {
			    redirectHome(request,response,null,null);			    
			}
			
		} 
		
		// PATH: /*/*
		else if(URIparts.length == 4){
			
			// PATH /tos/[filename]
			if(URIparts[2].equalsIgnoreCase("tos")){
			    String name = URIparts[3];
			    if(name != null){
			    	SiteStorage store = new SiteStorage();
			    	store.connect();
			    	TOS tos = store.readName(name);
			    	if(tos != null){
						request.setAttribute("url", tos.getUrl()); 
						request.setAttribute("tos", tos);

						RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/tos_view.jsp");
						request.setAttribute("title", "Search");
						rd.forward(request,response);	
			    	} else {
						redirectHome(request,response,null,null);
					}
			    } else {
					redirectHome(request,response,null,null);
				}
			}
			 // PATH: /xml/[filename]
			else if(URIparts[2].equalsIgnoreCase("xml")){
			    String name = URIparts[3];
			    if(name != null){
			    	SiteStorage store = new SiteStorage();
			    	store.connect();
			    	TOS tos = store.readName(name);
			    	if(tos != null){
			    		tos.printToXML(response, false);
			    	} else {
					    redirectHome(request,response,null,null);			    
					}
			    } else {
				    redirectHome(request,response,null,null);
				}
			} 
			// PATH: /json/[filename]
			else if(URIparts[2].equalsIgnoreCase("json")){
			    String name = URIparts[3];
			    if(name != null){
			    	SiteStorage store = new SiteStorage();
			    	store.connect();
			    	TOS tos = store.readName(name);
			    	if(tos != null){
			    		tos.printToJSON(response, false);
			    	} else {
					    redirectHome(request,response,null,null);			    
					}
			    } else {
				    redirectHome(request,response,null,null);
				}
			} 
			// PATH: /archive/[filename]
			else if(URIparts[2].equalsIgnoreCase("archive")){
			    String name = URIparts[3];
			    if(name != null){
			    	redirectHome(request,response,null,null);
			    	
			    } else {
				    redirectHome(request,response,null,null);
				}
			}
			// PATH /*/*
			else {
				redirectHome(request,response,null,null);
			}
		} 

		// PATH: /*/*/*/*
		else if(URIparts.length == 6){
			
			// PATH: /archive/[format]/[filename]/[retrievedOn]
			if(URIparts[2].equalsIgnoreCase("archive")){
				
				// PATH: /archive/json/[filename]/[retrievedOn]
				if(URIparts[3].equalsIgnoreCase("json")){
					redirectHome(request,response,null,null);
				} 
				
				// PATH: /archive/xml/[filename]/[retrievedOn]
				else if(URIparts[3].equalsIgnoreCase("xml")){
					redirectHome(request,response,null,null);

				} else {
				    redirectHome(request,response,null,null);
				}
				
			} else {
			    redirectHome(request,response,null,null);
			}
			
		}
		
		// PATH: /**
		else {
			redirectHome(request,response,null,null);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String URI = request.getRequestURI();
    	String[] URIparts = URI.split("/");
    	
    	// PATH: /*
		if(URIparts.length == 3){
			
			// PATH: /msearch
			if(URIparts[2].equalsIgnoreCase("msearch")){
				String urls = (String) request.getParameterValues("urls")[0];
				HashMap<String, TOS> results = new HashMap<String, TOS>();
				
				Crawler crawler = new Crawler();
				try {
					results  = crawler.mcrawl(urls);
				} catch (Exception e) {
					redirectHome(request,response,"ERROR",e.getMessage());
				} 
				request.setAttribute("results", results);
				
			    RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/tos_multi_results.jsp");
				request.setAttribute("active", "msearch");
				request.setAttribute("title", "Multisearch");
				rd.forward(request,response);
				
			} 
			
			// PATH: /search
			else if(URIparts[2].equalsIgnoreCase("search")){
				String url = (String) request.getParameterValues("url")[0];
				TOS tos = null;
				
				Crawler crawler = new Crawler();
				try {
					
					tos  = crawler.crawl(url);
					
					if(tos != null){
						response.sendRedirect(request.getContextPath().toString() + "/tos/" + tos.getName());
					} else {
						redirectHome(request,response,"Not found!", "No results found were found for \"" + url + "\"");
					}
					
				} catch (Exception e) {
					redirectHome(request,response,"ERROR",e.getMessage());
				}
			} 
			
			// PATH: /*
			else {
			    redirectHome(request,response,null,null);
			}
		}
    	
	}
	
	protected void redirectHome(HttpServletRequest request, HttpServletResponse response, String alertTitle, String alertText) throws ServletException, IOException{
    	SiteStorage store = new SiteStorage();
    	store.connect();
    	List<TOS> last = store.last(10);
	    RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/tos_search.jsp");
		request.setAttribute("last", last);
		request.setAttribute("alertText", alertText);
		request.setAttribute("alertTitle", alertTitle);
		request.setAttribute("active", "home");
		request.setAttribute("title", "Home");
		rd.forward(request,response);
	}

}