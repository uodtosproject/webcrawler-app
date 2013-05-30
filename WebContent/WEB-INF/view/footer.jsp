		<hr>

            <footer>
                <p>&copy; University of Dundee, 2013</p>
            </footer>

        </div> <!-- /container -->
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
      
        <script src="<%= request.getContextPath()%>/resources/js/vendor/bootstrap.min.js"></script>

        <script src="<%= request.getContextPath()%>/resources/js/main.js"></script>
		
            
        <script type="text/javascript">
		$(document).ready(function () {
			$(".services").click(function () {
				var textAreaValue = $("textarea#urls").text();
				var service = $(this).attr("value");
				if(textAreaValue == "") {
					$("textarea#urls").text(service);
				} else {
					$("textarea#urls").text(textAreaValue + "\n" + service);
				}
			});
			$(".service").click(function () {
				$("input#url").val($(this).attr("value"));
			});
		});
		</script>
        <script>
            var _gaq=[['_setAccount','UA-XXXXX-X'],['_trackPageview']];
            (function(d,t){var g=d.createElement(t),s=d.getElementsByTagName(t)[0];
            g.src=('https:'==location.protocol?'//ssl':'//www')+'.google-analytics.com/ga.js';
            s.parentNode.insertBefore(g,s)}(document,'script'));
        </script>
    </body>
</html>
