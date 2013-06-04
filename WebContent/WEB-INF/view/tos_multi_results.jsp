<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="br.com.betohayasida.webcrawler.Store.TOS, br.com.betohayasida.webcrawler.Store.Page,java.util.HashMap,java.util.Set,java.lang.reflect.Array, java.util.ArrayList, java.util.List" %>
<%@ include file="header.jsp" %>
            <%
            	HashMap<String, TOS> results = (HashMap<String, TOS>) request.getAttribute("results");
                        String[] urls = (String[]) results.keySet().toArray(new String[0]);
                        int i = 0;
                        int j = urls.length;
                        for(i = 0; i < j; i++){
                        	String url = urls[i];
            %>
            
	            <%
            	            	TOS tos = (TOS) results.get(url);
            	            	            	String title = url;
            	            	            	title = title.replace("http://", "");
            	            	            	title = title.replace("https://", "");
            	            %>
				<h3><%= title %></h3>
				<% if(tos!=null) { %>
	            <div class="row">
	                <div class="span3">
						<h4>URL</h4>
						<p><a href="<%= tos.getUrl() %>" title="<%= tos.getUrl() %>"><%= tos.getUrl() %></a></p>
									
						<h4>Retrieved on</h4>
						<p><%= tos.getVisitedOn() %></p>
						
						<p><a href="<%= request.getContextPath()%>/tos/<%= tos.getName() %>" class="btn btn-danger">Individual Page</a></p>
						<p><a href="<%= request.getContextPath()%>/archive/<%= tos.getName() %>" class="btn btn-info">Archive</a></p>
						<p><a href="<%= request.getContextPath()%>/xml/<%= tos.getName() %>" class="btn btn-warning">XML</a></p>
						<p><a href="<%= request.getContextPath()%>/json/<%= tos.getName() %>" class="btn btn-warning">JSON</a></p>
	                </div>
	                <div class="span9">
	                	<% List<Page> pages = tos.getPages(); %>
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