<%@ include file="/base/head.html" %>
<%@ include file="/base/header.html" %>


<div id="navigation" class="sixteen columns clearfix">
        
	<div class="menu-header">		 
	<h2 class="element-invisible">Main menu</h2>
	<ul id="main-menu-links" class="menu clearfix">
			
			<li class="menu-218 first active"><a href="index" class="active">Home</a></li>
			${menuBar }
			<li class="menu-315 last"><a href="/user" title="Login page for registered users">Login</a></li>
			
	</ul>     
			
	</div>
            
</div><!-- /#navigation -->


<html>
<body>

<div id="content" class="sixteen columns clearfix">

 	<div id="main">
    <div class="region region-content">
    <div id="block-system-main" class="block block-system">

		<div class="content">
  	
  	 		
  	 		
  	 		
  	 		<br>
  	 		<br>
  	 		
  	 		TO BE DONE..........
  	 		<form name="input" action="AddEpidemicDatabase" method="POST">
			Parameter 1: <input type="text" name="epidemicID">
			<br>Parameter 2: <input type="textbox" name="backgroundInfo">
			<br>Priority: <input type="textbox" name="priority">
			<br><input type="submit" value="Submit">
			</form>
    	                     
    	</div>
    	
    </div>
    </div>
    </div>
        

<div class="clear"></div>
<div class="clear"></div>

<%@ include file="/base/footer.html" %>

 </div><!-- /#content -->
 
</body>

</html>


