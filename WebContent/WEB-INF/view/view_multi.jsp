<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="br.com.betohayasida.webcrawler.Store.Site,br.com.betohayasida.webcrawler.Store.Page,java.util.HashMap,java.util.Set,java.lang.reflect.Array,java.util.ArrayList,java.util.List" %>
<%@ include file="header.jsp" %>
            <%
            	HashMap<String, Site> results = (HashMap<String, Site>) request.getAttribute("results");
                String[] urls = (String[]) results.keySet().toArray(new String[0]);
                int i = 0;
                int j = urls.length;
                for(i = 0; i < j; i++){
                	String url = urls[i];
            %>
            
	            <%
   	            	Site site = (Site) results.get(url);
   	            	String title = url;
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
						
						<p><a href="<%= request.getContextPath()%>/site/<%= site.getName() %>" class="btn btn-danger">Individual Page</a></p>
						<p><a href="<%= request.getContextPath()%>/archive/<%= site.getName() %>" class="btn btn-info">Archive</a></p>
						<p><a href="<%= request.getContextPath()%>/xml/<%= site.getName() %>" class="btn btn-warning">XML</a></p>
						<p><a href="<%= request.getContextPath()%>/json/<%= site.getName() %>" class="btn btn-warning">JSON</a></p>
	                </div>
	                <div class="span9">
	                	<% List<Page> pages = site.getPages(); %>
	                	<% for(Page item : pages) { %>
		                <div class="well well-large">
				    		<p><a href="<%= item.getUrl() %>"><%= item.getUrl() %></a></p>
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
						<div class="alert">
					    	<h3>Not found</h3>
					    	<p>No results were found for '<%= title %>'.</p>
					    </div>
	            	</div>
	            </div>
	            <% } %>
            	<% if(i != j-1){ %>
            	<hr />
            	<% } %>
            <%
            }
            %>
<%@ include file="footer.jsp" %>