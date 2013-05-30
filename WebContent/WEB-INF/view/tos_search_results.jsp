<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="br.com.betohayasida.webcrawler.Store.ToSStore" %>
<% ToSStore tos = (ToSStore) request.getAttribute("tos"); %>
<%@ include file="header.jsp" %>
            
            <%
            	String title = (String) request.getAttribute("url");
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
            		<p>No results were found.</p>
            	</div>
            </div>
            <% } %>

<%@ include file="footer.jsp" %>