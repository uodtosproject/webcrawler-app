<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="br.com.betohayasida.webcrawler.Store.ToSStore, java.util.List, java.lang.reflect.Array;" %>
<%@ include file="header.jsp" %>

			<div class="hero-unit">
                <h1>TOS Multiple Search</h1>
          		<p class="lead">Search for Terms of Use of many social networks.</p><p>Type the URLs separated by spaces.</p>
				<form action="<%= request.getContextPath()%>/msearch/" method="POST">
			   		<textarea tabindex="1" autofocus="autofocus" rows="3" class="input-xlarge" name="urls" id="urls" placeholder="e.g. https://www.facebook.com  https://www.twitter.com"></textarea><br />
			    	<button class="btn" type="submit">Go!</button>
			    </form>
            </div>
			<div class="row">
				<div class="span6">
				    <h2>Common services</h2>
				    <div class="btn-group">
					    <button class="btn services btn-inverse" value="https://www.facebook.com">Facebook</button>
					    <button class="btn services btn-inverse" value="https://www.twitter.com">Twitter</button>
					    <button class="btn services btn-inverse" value="http://plus.google.com">Google+</button>
					    <button class="btn services btn-inverse" value="http://www.linkedin.com">Linkedin</button>
				    </div>
				</div>
				<div class="span6">
				    <h2>Last services searched</h2>
				    <div class="row">
					    <% List<ToSStore> last = (List<ToSStore>)request.getAttribute("last"); int i = 0; %>
					    <div class="span3">
					    <% for(ToSStore tos : last) { i++;%>
						<p><button class="btn services btn-inverse btn-xlarge" value="<%= tos.getOriginUrl() %>"><%= tos.getOriginUrl() %></button></p>
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