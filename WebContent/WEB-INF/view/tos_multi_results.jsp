<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="br.com.betohayasida.webcrawler.Store.ToSStore, java.util.HashMap, java.util.Set, java.lang.reflect.Array;" %>
<%@ include file="header.jsp" %>
            <%
            HashMap<String, ToSStore> results = (HashMap<String, ToSStore>) request.getAttribute("results");
            String[] urls = (String[]) results.keySet().toArray(new String[0]);
            int i = 0;
            int j = urls.length;
            for(i = 0; i < j; i++){
            	String url = urls[i];
            %>
            
	            <%
	            	ToSStore tos = (ToSStore) results.get(url);
	            	String title = url;
	            	title = title.replace("http://", "");
	            	title = title.replace("https://", "");
	            %>
				<h3>Search for "<%= title %>"</h3>
				<% if(tos!=null) { %>
	            <div class="row">
	                <div class="span3">
						<h4>TOS Page's URL</h4>
						<p><a href="<%= tos.getUrl() %>" title="<%= tos.getUrl() %>"><%= tos.getUrl() %></a></p>
							
						<h4>URL</h4>
						<p><a href="<%= tos.getOriginUrl() %>" title="<%= tos.getOriginUrl() %>"><%= tos.getOriginUrl() %></a></p>
							
						<h4>Retrieved on</h4>
						<p><%= tos.getRetrievedOn() %></p>
						<p><a href="<%= request.getContextPath()%>/tos/<%= tos.getFilename() %>" class="btn btn-danger">Individual Page</a></p>
					<p><a href="<%= request.getContextPath()%>/archive/<%= tos.getFilename() %>" class="btn btn-info">Archive</a></p>
						<p><a href="<%= request.getContextPath()%>/xml/<%= tos.getFilename() %>" class="btn btn-warning">XML</a></p>
						<p><a href="<%= request.getContextPath()%>/json/<%= tos.getFilename() %>" class="btn btn-warning">JSON</a></p>
	                </div>
	                <div class="span9">
		                <div class="well well-large">
					    	<div class="tosbox">
						    	<%= tos.getText() %>
					    	</div>
						</div>
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