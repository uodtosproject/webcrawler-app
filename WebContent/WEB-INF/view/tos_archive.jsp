<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="br.com.betohayasida.webcrawler.Store.ToSStore, java.util.List, java.util.Set, java.lang.reflect.Array;" %>
<%@ include file="header.jsp" %>
			
			<h3>Archive for "<%= request.getAttribute("url") %>"</h3>
            <%
            List<ToSStore> results = (List<ToSStore>) request.getAttribute("results");
			if(results.size() > 0){
            	for(ToSStore tos : results){
            %>
					<% if(tos!=null) { %>
	            <div class="row">
	                <div class="span3">
						<h4>TOS Page's URL</h4>
						<p><a href="<%= tos.getUrl() %>" title="<%= tos.getUrl() %>"><%= tos.getUrl() %></a></p>
								
						<h4>Retrieved on</h4>
						<p><%= tos.getRetrievedOn() %></p>
						
						<p><a href="<%= request.getContextPath()%>/archive/xml/<%= tos.getFilename() %>/<%= tos.getRetrievedOnMili() %>" class="btn btn-warning">XML</a></p>
						<p><a href="<%= request.getContextPath()%>/archive/json/<%= tos.getFilename() %>/<%= tos.getRetrievedOnMili() %>" class="btn btn-warning">JSON</a></p>
	                </div>
	                <div class="span9">
		                <div class="well well-large">
					    	<div class="tosbox">
						    	<%= tos.getText() %>
					    	</div>
						</div>
	                </div>
	            </div>
		            <% } %>
            <%
            	}
			} else {
			%>
	            <div class="row">
	            	<div class="span12">
						<div class="alert">
					    	<h3>Not found</h3>
					    	<p>No archive was found for '<%= request.getAttribute("url") %>'.</p>
					    </div>
	            	</div>
	            </div>
			
			<%
			}
            %>
<%@ include file="footer.jsp" %>