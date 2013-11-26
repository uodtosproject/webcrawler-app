package br.com.betohayasida.tos.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Controller for Home page
 * @author rkhayasidajunior
 *
 */
public class Home extends HttpServlet {
	private static final long serialVersionUID = -3682096299146791172L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		String URI = request.getRequestURI();
    	String[] URIparts = URI.split("/");
		if(URIparts[2].equalsIgnoreCase("about")){
		    RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/about.jsp");
			request.setAttribute("active", "about");
			request.setAttribute("title", "About");
			rd.forward(request,response);
		} else if(URIparts[2].equalsIgnoreCase("contact")){
		    RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/contact.jsp");
			request.setAttribute("active", "contact");
			request.setAttribute("title", "Contact");
			rd.forward(request,response);
		} else {
		    RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view/tos_search.jsp");
			request.setAttribute("active", "home");
			request.setAttribute("title", "Home");
			rd.forward(request,response);
		} 
	}
}