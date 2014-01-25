
<%@ include file="/base/head.html" %>

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
    	
    	<h1> Summary for epidemic ${epidemicID} </h1>
    	
    	This is a public facing summary of the current status of an epidemic.
    	<br>
    	<br>
    	
 		<h2>Background Information on Epidemic</h2>
  			 
		 ${bodyContent }
	
		<br>
		<br>
		
		<h2>Best Parameter Estimates</h2>
		${runID}
	
		${estimates }
		
		<br>
		<h3>Phylogeny</h3>
		${phylogeny}

		<br>
		<br>
		
		
		</div>
		<a href='estimateBest'><button id="somebutton">Get best estimate</button></a>
		
		<hr>
		
		<h2>Manage analysis and data for this epidemic</h2>
		<a href='viewEpidemicRuns'><button id="somebutton">Manage MCMCs</button></a>
		<a href='viewEpidemicRuns '><button id="somebutton">todo Manage Data</button></a>
		<a href='viewEpidemicRuns '><button id="somebutton">todo Mange Recommendations</button></a>
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
