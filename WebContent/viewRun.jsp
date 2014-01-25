<%@ include file="/base/head.html" %>
<%@ include file="/base/header.html" %>


<div id="navigation" class="sixteen columns clearfix">
        
	<div class="menu-header">		 
	<h2 class="element-invisible">Main menu</h2>
	<!--  ul id="main-menu-links" class="menu clearfix">
			
			<li class="menu-218 first active"><a href="index" class="active">Home</a></li>
			${menuBar }
			<li class="menu-315 last"><a href="/user" title="Login page for registered users">Login</a></li>
			
	</-->     
			
	</div>
            
</div><!-- /#navigation -->


<html>
<body>

<div id="content" class="sixteen columns clearfix">

 	<div id="main">
    <div class="region region-content">
    <div id="block-system-main" class="block block-system">

		<div class="content">
  	
  	 		
  	 		<h1>Run information</h1>
  	 		
  	 		<h2>Parameter Estimates</h2>
  	 		Run ID: ${runID} <br/>
  	 		Process ID: ${processID}<br/>
  	 		CI: ${CI}<br/>
  	 		
  	 		<hr />
  	 		<h2>Progress of run</h2>
  	 		
  	 		Time started: ${timeStarted }<br/>
  	 		Time running: ${hoursSince } hours<br/>
  	 		Percent done: ${percentDone}<br/>
  	 		Current step: ${currentStep }<br/>
  	 		Total chain length: ${chainLength }<br/>
  	 		
  	 		<hr />
  	 		<h2>Health of run</h2>
  	 		
 			Predictive potential: ${ predictivePot}<br/>
  	 		ESS: <br>${ESS }<br/>
  	 		Sum of all ESS: ${sumESS } <br />
  	 		Realised potential (log sumESS/spread): ${potRealised}<br />
  	 		
  	 		<br />
  	 		<br />
  	 		ESS per step: To be implemented<br />
  	 		Mixing: To be implemented <br />
  	 		Tracer view:<br/>
  	 		
  	 		<hr />
  	 		<h2>MFCC Phylogeny</h2>
  	 		
  	 		<img src='DownloadServlet?path=${phyloPath}'>
  	 		
  	 		<hr />
  	 		<h2>Downloads </h2>
  	 		<a href="DownloadServlet?path=${logPath}">Download log file</a> <br>
  	 		<a href="DownloadServlet?path=${treePath}">Download tree file</a> <br>
  	 		<a href="DownloadServlet?path=${consolePath}">Download console output</a> <br>
  	 		<a href="DownloadServlet?path=${errorPath}">Download error output</a> <br>
  	 		<a href="DownloadServlet?path=${logSummary}">Download summary file</a> <br>
  	 		<a href="DownloadServlet?path=${MFCCPath}">Download MFCC file</a> <br>
  	 		<hr />
  	 		
  	 		<h2>Controls</h2>
  	 		<a href='stopRun?processID=${processID}'><button type='button'>Stop run</button></a>
  	 		<a href='deleteRun?runID=${runID}'><button type='button'>Delete run</button></a>
  	 
    	                     
    	</div>
    	
    	<hr />
    	
    </div>
    </div>
    </div>
        

<div class="clear"></div>
<div class="clear"></div>

<%@ include file="/base/footer.html" %>

 </div><!-- /#content -->
 
</body>

</html>


