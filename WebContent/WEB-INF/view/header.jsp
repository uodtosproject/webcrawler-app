<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>TOSCrawler - <%= request.getAttribute("title") %></title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/resources/css/bootstrap.min.css">
        <style>
            body {
                padding-top: 60px;
                padding-bottom: 40px;
            }
        </style>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/resources/css/bootstrap-responsive.min.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/resources/css/main.css">

        <script src="<%= request.getContextPath()%>/resources/js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>
    </head>
    <body>
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <a class="brand" href="<%= request.getContextPath()%>/search">TOSCrawler</a>
                    <div class="nav-collapse collapse">
                    	<%
                    		String about = "";
                    		String contact = "";
                    		String home = "";
                    		String msearch = "";
                    		String active = (String) request.getAttribute("active");
                    		if(active != null){
                    			if(active.equals("about")){
                    				about = " class=\"active\"";
                    			} else if(active.equals("contact")){
                    				contact = " class=\"active\"";
                    			} else if(active.equals("home")){
                    				home = " class=\"active\"";
                    			} else if(active.equals("msearch")){
                    				msearch = " class=\"active\"";
                    			}
                    		}
                    	%>
                        <ul class="nav">
                            <li<%= home %>><a href="<%= request.getContextPath()%>/search">Home</a></li>
                            <li<%= msearch %>><a href="<%= request.getContextPath()%>/msearch/">Multisearch</a></li>
                            <li<%= about %>><a href="<%= request.getContextPath()%>/about">About</a></li>
                            <li<%= contact %>><a href="<%= request.getContextPath()%>/contact">Contact</a></li>
                        </ul>
                    </div>
		            <form class="navbar-form pull-right" action="<%= request.getContextPath()%>/search/" method="POST">
					    <div class="input-append">
					    	<input class="input-xlarge" name="url" id="url_head" type="text" placeholder="URL (e.g. https://www.facebook.com)">
					    	<button class="btn" type="submit">Go!</button>
					    </div>
				    </form>
                </div>
            </div>
        </div>

        <div class="container">
			<% 
			String alertText = (String) request.getAttribute("alertText");
			String alertTitle = (String) request.getAttribute("alertTitle");
			if(alertText != null && alertTitle != null){
			%>
			<div class="alert">
		    	<button type="button" class="close" data-dismiss="alert">&times;</button>
		    	<h3><%= alertTitle %></h3>
		    	<p><%= alertText %></p>
		    </div>
			<% } %>