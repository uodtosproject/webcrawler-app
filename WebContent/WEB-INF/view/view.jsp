<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="br.com.betohayasida.webcrawler.Store.Site,br.com.betohayasida.webcrawler.Store.Page,java.util.ArrayList,java.util.List" %>
<%
	Site site = (Site) request.getAttribute("site");
%>
<%@ include file="header.jsp" %>
            
            <%
            	String title = (String) request.getAttribute("url");
            	title = title.replace("http://", "");
            	title = title.replace("https://", "");
            %>
			<h3><%= title %></h3>
			<% if(site!=null) { %>
            <div class="row">
	                <div class="span3">
						<h4>URL</h4>
						<p><a href="<%= site.getUrl() %>" title="<%= site.getUrl() %>"><%= site.getUrl() %></a></p>
									
						<h4>Retrieved on</h4>
						<p><%= site.getVisitedOnString() %></p>
						
						<p><a href="<%= request.getContextPath()%>/archive/<%= site.getName() %>" class="btn btn-info">Archive</a></p>
						<p><a href="<%= request.getContextPath()%>/xml/<%= site.getName() %>" class="btn btn-warning">XML</a></p>
						<p><a href="<%= request.getContextPath()%>/json/<%= site.getName() %>" class="btn btn-warning">JSON</a></p>
	                </div>
	                <div class="span9">
	                	<% List<Page> pages = site.getPages(); %>
	                	<% for(Page item : pages) { %>
		                <div class="well well-large">
		                	<h5><%= item.getTitle() %></h5>
				    		<p>URL: <a href="<%= item.getUrl() %>"><%= item.getUrl() %></a></p>
				    		<p>Retrieved on <%= item.getRetrievedOn() %></p>
				    		
				    		<hr />
				    		
					    	<div class="tosbox">
						    	<%= item.getText() %>
					    	</div>
						</div>
						<% } %>
	                </div>
	            </div>
            <% } else { %>
            <div class="row">
            	<div class="span12">
            		<p>No results were found.</p>
            	</div>
            </div>
            <% } %>

<%@ include file="footer.jsp" %>