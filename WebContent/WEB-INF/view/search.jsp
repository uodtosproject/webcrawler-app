<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="br.com.betohayasida.webcrawler.Store.Site,java.util.List,java.lang.reflect.Array;" %>
<%@ include file="header.jsp" %>
			<div class="hero-unit">
                <h1>Crawler</h1>
          		<p class="lead">Search for Terms of Use of social networks.</p><p>Type the complete URL.</p>
				<form class="form-inline" action="<%=request.getContextPath()%>/search/" method="POST">
				    <div class="input-append">
				    	<input tabindex="1" autofocus="autofocus" class="input-xlarge" name="url" id="url" type="text" placeholder="e.g. https://www.facebook.com">
				    	<button class="btn" type="submit">Go!</button>
				    </div>
			    </form>
			    <p>Or use the <a href="<%=request.getContextPath()%>/msearch">multiple search</a>.</p>
            </div>
			<div class="row">
				<div class="span6">
				    <h2>Common services</h2>
				    <div class="btn-group">
					    <button class="btn service btn-inverse" value="https://www.facebook.com">Facebook</button>
					    <button class="btn service btn-inverse" value="https://www.twitter.com">Twitter</button>
					    <button class="btn service btn-inverse" value="https://plus.google.com">Google+</button>
					    <button class="btn service btn-inverse" value="http://www.linkedin.com">Linkedin</button>
				    </div>
				</div>
				<div class="span6">
				    <h2>Last services searched</h2>
				    <div class="row">
					    <div class="span3">
					    <%
					    	List<Site> last = (List<Site>)request.getAttribute("last"); int i = 0;
					    %>
					    <%
					    	for(Site site : last) { i++;
					    %>
						<p><button class="btn service btn-inverse btn-xlarge" value="<%= site.getUrl() %>"><%= site.getUrl() %></button></p>
					    	<% if(i==5) { %>
					    </div>
					    <div class="span3">
					    	<% } %>
					    <% } %>
					    </div>
				    </div>
				</div>
			</div>

<%@ include file="footer.jsp" %>