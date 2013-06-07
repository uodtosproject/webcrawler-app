<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="br.com.betohayasida.webcrawler.Store.Site,br.com.betohayasida.webcrawler.Store.Page,java.util.HashMap,java.util.Set,java.lang.reflect.Array,java.util.ArrayList,java.util.List" %>
<%@ include file="header.jsp" %>
            <%
           	List<Site> sites = (List<Site>) request.getAttribute("sites");
           	String title = (String) request.getAttribute("url");
           	title = title.replace("http://", "");
           	title = title.replace("https://", "");

            int i = 0;
            
            if(sites.size() > 0) {
            %>
						
	            <div class="row">
	                <div class="span12">
						<h1>Archive for <%= title %></h1>
						<p class="lead">https://twitter.com</p>
						<p><a href="<%= request.getContextPath()%>/archive/xml/<%= sites.get(0).getName() %>" class="btn btn-warning">XML</a></p>
						<p><a href="<%= request.getContextPath()%>/archive/json/<%= sites.get(0).getName() %>" class="btn btn-warning">JSON</a></p>
						<div class="accordion" id="accordion2">
			          	<% for(Site site : sites){ %>
			          		<div class="accordion-group">
								<div class="accordion-heading">
									<h4><a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapse<%=i%>">
										<%= site.getVisitedOnString() %>
									</a></h4>
								</div>
								<div id="collapse<%=i%>" class="accordion-body collapse<%if(i==0){%> in<%}%>">
									<div class="accordion-inner">
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
							</div>
							<% i++; %>
						<% } %>
						</div>
					</div>
				</div>
			
            <% } else { %>
            <div class="row">
            	<div class="span12">
					<div class="alert">
				    	<h1>Not found</h1>
				    	<p class="lead">No results were found for '<%= title %>'.</p>
				    </div>
            	</div>
            </div>
            <% } %>
<%@ include file="footer.jsp" %>