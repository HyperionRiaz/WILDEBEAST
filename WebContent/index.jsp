
<%@ include file="/base/head.html" %>
<head>
<!--  THE DISPLAY SCRIPT FOR MAIN PHYLOG -->
<script>
            $(document).ready(function() {                        // When the HTML DOM is ready loading, then execute the following function...
                $('#viewMain').click(function() {               // Locate HTML DOM element with ID "somebutton" and assign the following function to its "click" event...
                    $.get('viewmain', function(responseText) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
                        $('#mainDisplay').text(responseText);         // Locate HTML DOM element with ID "somediv" and set its text content with the response text.
                    });
                });
            });
        </script>
        

</head>

<body class="html front not-logged-in no-sidebars page-node page-node- page-node-1 node-type-page" >
  <div id="skip-link">
    <a href="#main-content" class="element-invisible element-focusable">Skip to main content</a>
  </div>
    <div id="wrap">
    <div class="container">
        
        <div class="resize"></div>
        
        <!-- #header -->
        <div id="header" class="sixteen columns clearfix">
            <div class="inner">
    
                    <a href="/" title="Home" rel="home" id="logo">
                    <img src="http://epidemic.bio.ed.ac.uk/sites/default/files/logo_small.png" alt="Home" />
                  </a>
                                
                   <div id="name-and-slogan">
                
                     <div id="site-name">
                  		<a href="/" title="Home" rel="home">epidemic</a>
                    </div>
                                        
                                        <div id="site-slogan">
                    Molecular Epidemiology and Evolution of Viral Pathogens                    </div>
                                    
                </div>
                            </div>
        </div><!-- /#header -->
        
        <!-- #navigation -->
        <div id="navigation" class="sixteen columns clearfix">
        
			<div class="menu-header">
			 
			<h2 class="element-invisible">Main menu</h2>
			<ul id="main-menu-links" class="menu clearfix">
			
				<li class="menu-218 first active"><a href="index" class="active">Home</a></li>
				<!--  li class="menu-435"><a href="/coronaviruses" title="Background and genetic analysis of the novel coronavirus circulating in the Arabian Peninsula">Coronaviruses</a></li>
				<li class="menu-436"><a href="/influenza" title="A hub for research on influenza viruses including human and animal epidemics">Influenza</a></li>-->
				${menuBar}
				<li class="menu-315 last"><a href="/user" title="Login page for registered users">Login</a></li>
			
			</ul>                        
			
			</div>
            
        </div><!-- /#navigation -->
        
          <div id="content" class="sixteen columns clearfix">
          <div id="main">
            
                                
               <div class="tabs">
                           </div>
                                
                                
                                
    <div class="region region-content">
    <div id="block-system-main" class="block block-system">

    
  	<div class="content">
    	<div id="node-1" class="node node-page node-promoted clearfix" about="/home" typeof="foaf:Document">
		<div class="content clearfix">
    	<div class="field field-name-body field-type-text-with-summary field-label-hidden">
    
    	<div class="field-items">
    
    	<div class="field-item even" property="content:encoded"> 
    	
		    <p>This is a webserver that attempts to automatically monitor new disease epidemics using tools such as BEAST.</p>
			<p>All content is publically viewable but initiating monitoring of new epidemics, editing current BEAST runs etc is restricted to account holders. </p>
			<hr />
			
			<h1>${notification }</h1>
			
			<h2>Current best knowledge of epidemic of interest: MERS-COV</h2>
			
			<div id='mainDisplay'>
			
				<h3>Background: The MERS-CoV epidemic has resulted in 89 infections and 43 fatalities since June 2012. </h3>
				<h3>Best estimate of the Growth rate is: 1.42 </h3>
				<h3>Best estimate of the TMRCA is: January 3rd 2011 (95% Confidence interval Feb 28 - March 22)</h3>
				<h3>Best estimate of phylogeny of 8 MERS-CoV sequences below:</h3>
				<hr>
				<br>	
				<img width='360' height='369' src='currentPhylo.gif'>
				
				
			
			</div>
			
			<button id="viewMain">Click here to view the main display</button>
			
			<br>
			<br>
			<br>
			
			<h1>Pages</h1>			
			<ul>
			<li> <a href='AddEpidemic'>Add Epidemic: </a>Add a new epidemic here, and start a BEAST analysis for it.</li>
			<li> <a href='viewEpidemic?epidemicID=H1N12009'>View Epidemic: </a>View summaries and options on an existing epidemic. </li>
			
			</ul>
			
			<br>
			<h1>Testing</h1>
			<ul>
				<li>To start a new run, <a href="runBEAST.jsp">click here.</a></li>
				
				<li>To view data from an existing run, <a href="viewTrees.jsp">click here.</a></li>
				
				<li>See info from existing <a href="generic">run.</a></li>
			</ul>
			
			
		  
		</div>
	 	</div>
		</div>
	  	</div>
	    </div>
		</div>
	  	</div>                       
        </div>
        
     </div><!-- /#content -->
                 
        <div class="clear"></div>
        <div class="clear"></div>
        
     <%@ include file="base/footer.html" %>  
</div> <!-- /#wrap -->
  </body>
</html>
