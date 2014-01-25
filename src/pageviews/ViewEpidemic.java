package pageviews;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.ResultSet;

import corejobs.AnalyseRuns;
import corejobs.BeastUtilities;
import datajobs.DatabaseUtilities;
import datajobs.LogUtilities;


public class ViewEpidemic extends HttpServlet {
	
		DatabaseUtilities dbtools = new DatabaseUtilities();
		
		/*
		 * A class for generating the public facing summary for an epidemic.
		 * Provides error checking and generates front end summary.
		 * 
		 */
		
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
		{
			
			String epidemicID = request.getParameter("epidemicID"); //Get the requested epidemic extracting from ?
			boolean exists = dbtools.checkEpidemicExists(epidemicID);
			String generateHTML = dbtools.getEpidemicsHeader();
			request.setAttribute("menuBar", generateHTML); //generate the top header dynamically
			
			if (exists)
			{
					request.setAttribute("epidemicID", epidemicID);
			        RequestDispatcher view = request.getRequestDispatcher("viewEpidemicSummary.jsp");  
					request.setAttribute("bodyContent", dbtools.getEpidemicSummary(epidemicID)); 
					ServletContext c = getServletContext(); //Set ServletContext to indicate we're dealing with this epidemic
					c.setAttribute("epidemicID", epidemicID);
					
					// ========= Get best estimate to display on summary =========
					String data = "";
					String estimates = "";
					String estimatesOut = "";
					String runID = "";
					
					try
					{
						data = dbtools.prepareDisplayRuns(epidemicID); //makes an array structure for d3, which is stored here then passed to page.
					}
					catch(Exception e)
					{
						System.out.println("No runs to display");
					}
					
					if (data.equalsIgnoreCase("]"))
					{
						estimates = "No runs for this epidemic yet.";
					}
					else
					{
						
						try {
							runID = AnalyseRuns.getBestEstimate(epidemicID); //TODO extend
							
							//estimates = LogUtilities.getCI(epidemicID, runID);
							Map CIlowToHigh = LogUtilities.getCIValues(epidemicID, runID); 
							Map estimatesP = LogUtilities.getPosteriorMeanEstimates(epidemicID, runID);
							   
							    Iterator it = estimatesP.entrySet().iterator();
							    while (it.hasNext()) {
							        Map.Entry pairs = (Map.Entry)it.next();
							      	estimatesOut += "<b>"+pairs.getKey()+"</b>" + ": " + pairs.getValue() + " (" + CIlowToHigh.get(pairs.getKey())+")<br>";
							      	
							        it.remove(); // avoids a ConcurrentModificationException
							    }
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						request.setAttribute("runID", "<b>Estimated by run with ID " + runID +"</b></br>");
						
					}

					request.setAttribute("phylogeny", "Not implemented yet.");
					request.setAttribute("estimates", estimatesOut);
				    view.forward(request, response);
				        

			}
			else //Redirect to home page
			{
				 	request.setAttribute("notification", "Epidemic with ID '" + epidemicID + "' does not exist."); 
			        RequestDispatcher view = request.getRequestDispatcher("index.jsp");
			        view = request.getRequestDispatcher("index.jsp");
			        view.forward(request, response);
			}
     
	      
	        
		}
		
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
		{
				doGet(request, response);
		}
	}

