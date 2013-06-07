package br.com.betohayasida.tos.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.betohayasida.webcrawler.Modules.Crawler;
import br.com.betohayasida.webcrawler.Store.Archive;
import br.com.betohayasida.webcrawler.Store.Site;

// Extend HttpServlet class
public class ToS extends HttpServlet {
	private static final long serialVersionUID = -3682096299146791172L;
	
	public void init() throws ServletException{
		
  	}

	public void doGet(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("text/html");
		String URI = request.getRequestURI();
    	String[] URIparts = URI.split("/");
    	
    	// PATH: /*
		if(URIparts.length == 3){
			// PATH: /msearch
			if(URIparts[2].equalsIgnoreCase("msearch")){
		    	List<Site> last = Site.last(10);
			    RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/search_multi.jsp");
				request.setAttribute("last", last);
				request.setAttribute("active", "msearch");
				request.setAttribute("title", "Multisearch");
				
				try {
					rd.forward(request,response);
				} catch (Exception e) {
					// e.printStackTrace();
				} 
			} 
			
			// PATH: /search or /*
			else {
			    redirectHome(request,response,null,null);			    
			}
			
		} 
		
		// PATH: /*/*
		else if(URIparts.length == 4){
			
			// PATH /site/[filename]
			if(URIparts[2].equalsIgnoreCase("site")){
			    String name = URIparts[3];
			    if(name != null){

			    	Site site = Site.loadByName(name, false);
			    	if(site != null){

						request.setAttribute("url", site.getUrl()); 
						request.setAttribute("site", site);

						RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/view.jsp");
						request.setAttribute("title", "Results for " + site.getUrl());
						
						try {
							rd.forward(request,response);
						} catch (Exception e) {
							// e.printStackTrace();
						} 
						
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
			    	Site site = Site.loadByName(name, false);
			    	if(site != null){
			    		site.printToXML(response);
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
			    	Site site = Site.loadByName(name, false);
			    	if(site != null){
			    		site.printToJSON(response);
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
			    	List<Site> sites = Site.loadAllByName(name, true);
			    	if(sites.size() > 0){
						request.setAttribute("url", sites.get(0).getUrl()); 
						request.setAttribute("sites", sites);

						RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/archive.jsp");
						request.setAttribute("title", "Archive for " + sites.get(0).getUrl());
						
						try {
							rd.forward(request,response);
						} catch (Exception e) {
							// e.printStackTrace();
						} 
						
			    	} else {
						RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/no_archive.jsp");
						try {
							rd.forward(request,response);
						} catch (Exception e) {
							// e.printStackTrace();
						}
					}
			    	
			    } else {
				    redirectHome(request,response,null,null);
				}
			    
			}
			// PATH /*/*
			else {
				redirectHome(request,response,null,null);
			}
		} 

		// PATH: /*/*/*
		else if(URIparts.length == 5){
			
			// PATH: /archive/[format]/[filename]
			if(URIparts[2].equalsIgnoreCase("archive")){
				
				// PATH: /archive/json/[filename]
				if(URIparts[3].equalsIgnoreCase("json")){
					
				    String name = URIparts[4];
				    if(name != null){

				    	List<Site> sites = Site.loadAllByName(name, true);
				    	if(sites.size() > 0){
					    	Archive arc = new Archive();
					    	arc.setSites(sites);
				    		arc.printToJSON(response);
				    	} else {
						    redirectHome(request,response,null,null);			    
						}
				    	
				    } else {
					    redirectHome(request,response,null,null);
					}
				    
				} 
				
				// PATH: /archive/xml/[filename]
				else if(URIparts[3].equalsIgnoreCase("xml")){
					
				    String name = URIparts[4];
				    if(name != null){
				    	
				    	List<Site> sites = Site.loadAllByName(name, true);
				    	if(sites.size() > 0){
					    	Archive arc = new Archive();
					    	arc.setSites(sites);
				    		arc.printToXML(response);
				    	} else {
						    redirectHome(request,response,null,null);			    
						}
				    	
				    } else {
					    redirectHome(request,response,null,null);
					}

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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		String URI = request.getRequestURI();
    	String[] URIparts = URI.split("/");
    	
    	// PATH: /*
		if(URIparts.length == 3){
			
			// PATH: /msearch
			if(URIparts[2].equalsIgnoreCase("msearch")){
				String urls = (String) request.getParameterValues("urls")[0];
				HashMap<String, Site> results = new HashMap<String, Site>();
				
				Crawler crawler = new Crawler();
				try {
					results  = crawler.mcrawl(urls);
				} catch (Exception e) {
					redirectHome(request,response,"ERROR",e.getMessage());
				} 
				request.setAttribute("results", results);
				
			    RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/view_multi.jsp");
				request.setAttribute("active", "msearch");
				request.setAttribute("title", "Multisearch");
				try {
					rd.forward(request,response);
				} catch (Exception e) {
					// e.printStackTrace();
				} 
				
			} 
			
			// PATH: /search
			else if(URIparts[2].equalsIgnoreCase("search")){
				String url = (String) request.getParameterValues("url")[0];
				Site site = null;
				
				Crawler crawler = new Crawler();
				try {
					
					site  = crawler.crawl(url);

					if(site!=null) System.out.println("Success");
					if(site != null){
						response.sendRedirect(request.getContextPath().toString() + "/site/" + site.getName());
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
	
	protected void redirectHome(HttpServletRequest request, HttpServletResponse response, String alertTitle, String alertText){
    	List<Site> last = Site.last(10);
	    RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/search.jsp");
		request.setAttribute("last", last);
		request.setAttribute("alertText", alertText);
		request.setAttribute("alertTitle", alertTitle);
		request.setAttribute("active", "home");
		request.setAttribute("title", "Home");
		
		try {
			rd.forward(request,response);
		} catch (Exception e) {
			// e.printStackTrace();
		} 
	}

}