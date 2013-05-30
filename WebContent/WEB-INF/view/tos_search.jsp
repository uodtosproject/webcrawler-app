<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="br.com.betohayasida.webcrawler.Store.ToSStore, java.util.List, java.lang.reflect.Array;" %>
<%@ include file="header.jsp" %>
			<div class="hero-unit">
                <h1>TOS Search</h1>
          		<p class="lead">Search for Terms of Use of social networks. Type the complete URL.</p>
				<form class="form-inline" action="<%= request.getContextPath()%>/search/" method="POST">
				    <div class="input-append">
				    	<input tabindex="1" autofocus="autofocus" class="input-xlarge" name="url" id="url" type="text" placeholder="e.g. https://www.facebook.com">
				    	<button class="btn" type="submit">Go!</button>
				    </div>
			    </form>
			    <p>Or use our <a href="<%= request.getContextPath()%>/msearch">multiple search</a></p>
            </div>
			<div class="row">
				<div class="span6">
				    <h2>Common services</h2>
				    <div class="btn-group">
					    <button class="btn service btn-inverse" value="https://www.facebook.com">Facebook</button>
					    <button class="btn service btn-inverse" value="https://www.twitter.com">Twitter</button>
					    <button class="btn service btn-inverse" value="http://www.linkedin.com">Linkedin</button>
				    </div>
				</div>
				<div class="span6">
				    <h2>Last services searched</h2>
				    <% List<ToSStore> last = (List<ToSStore>)request.getAttribute("last"); %>
				    <% for(ToSStore tos : last) { %>
					<p><button class="btn service btn-inverse btn-xlarge" value="<%= tos.getOriginUrl() %>"><%= tos.getOriginUrl() %></button></p>
				    <% } %>
				</div>
			</div>

<%@ include file="footer.jsp" %>