package pageviews;

import java.io.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datajobs.DatabaseUtilities;
import datajobs.LogUtilities;
import datajobs.Run;



public class QueryRun extends HttpServlet {
	
	DatabaseUtilities dbtools = new DatabaseUtilities();
	LogUtilities logtools = new LogUtilities();

	/*
	 * 	A class for managing the addition of a new run for a new epidemic.
	 *  Is a data job because it inserts a new run into Runs table (through Database utilites)
	 *  Also starts a new core job, interfacing with corejobs through Beast utilities)
	 *  
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//Extract parameters for new run
		ServletContext c = getServletContext(); //TODO: Error checking if null
		
		String runID = request.getParameter("runID");
		//System.out.println("GOT THIS RUNID " + runID);
		String epidemicID = "" + c.getAttribute("epidemicID");
		String path ="";
		
		
		  if (dbtools.checkRunExists(epidemicID, runID)) //If this runID doesn't already exist.
		  {
			    //ResultSet rs = null;
			  	
			  double predPot = 0;
			  
			    try {
					Run currentRun = new Run(epidemicID,runID);
					Map display = currentRun.getDisplay();
					path = display.get("path")+"";
					predPot = currentRun.getPredPot();
					
					Iterator it = display.entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry pairs = (Map.Entry)it.next();
				        System.out.println(pairs.getKey() + " = " + pairs.getValue());
				        it.remove(); // avoids a ConcurrentModificationException
				        request.setAttribute(pairs.getKey().toString(), pairs.getValue()+"");
				    }
					
				} catch (SQLException e) 
				{
					e.printStackTrace();
				}
			  
			    // == GET CI == 
			    String CI = logtools.getCI(epidemicID, runID);
			    
			    // == GET ESS ==
			    Map ESS = logtools.getESS(epidemicID, runID);
			    String ESSout = "";
			    double sumESS =0;
			    Iterator it = ESS.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			      	ESSout += pairs.getKey() + " = " + pairs.getValue() + "<br>";
			      	sumESS+=  Double.parseDouble(pairs.getValue()+"");
			        it.remove(); // avoids a ConcurrentModificationException
			    }
			    
			    // == GET MFCC ==
			    logtools.getMFCC(epidemicID, runID);

		        // == Get Phylogeny ==
			    String phyloPath = logtools.getPhylo(epidemicID, runID, "gif");
			    
			    
		        // == Render view ==
			    //TODO move some of these to Run object!
		        
		        request.setAttribute("logPath", path+"output.log");
		        request.setAttribute("treePath", path+"output.trees");
		        request.setAttribute("MFCCPath", path+"MFCC.txt");
		        request.setAttribute("logSummary", path+"summary.txt");
		        request.setAttribute("consolePath", path+"consoleOutput.txt");
		        request.setAttribute("errorPath", path+"consoleOutputErrors.txt");
		        request.setAttribute("runID", runID);    
		        request.setAttribute("CI", CI);
		        request.setAttribute("ESS", ESSout);
		        request.setAttribute("sumESS", sumESS);
		        
		       
		        double potRealised = Math.log(sumESS)/predPot;
		        System.err.println("REALISED POTENTIAL: " + potRealised);
		        
		        request.setAttribute("potRealised", potRealised);
		        
		        
		        request.setAttribute("phyloPath", phyloPath);
		        
		        RequestDispatcher view = request.getRequestDispatcher("viewRun.jsp");
				view.forward(request, response);
					
		  }
		 else
		 {
					
					String message = "ERROR: Cannot find a run with that ID."; 
			        
					response.sendRedirect("viewEpidemicRuns?message=" + message); //Now refresh the page.
			}
		
      
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
	

}
