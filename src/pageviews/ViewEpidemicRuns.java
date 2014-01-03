package pageviews;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.ResultSet;

import datajobs.DatabaseUtilities;
import datajobs.LogUtilities;


public class ViewEpidemicRuns extends HttpServlet {
	
	/*
	 * A class for generating the private MCMC view for an epidemic.
	 * 
	 */
	
			DatabaseUtilities dbtools = new DatabaseUtilities();
			
			//todo, prepare CSV then render.
		
			public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
			{
				ServletContext c = getServletContext();
				//String epidemicID = request.getParameter("epidemicID");
				String epidemicID = (String) c.getAttribute("epidemicID");
				if (epidemicID == null)
				{
					System.err.println("EpidemicID not set. Defaulting to Alien2.");
					epidemicID = "Alien2";
				}
				
				
				// ========= Check for new sequences!!! =====================
				boolean newData = dbtools.checkForNewData(epidemicID);
				if (newData)
				{
					request.setAttribute("newData", "There are new sequences since your last visit");
				}
				else
				{
					request.setAttribute("newData", "");
				}
				
				// ========= Get runs display table ==========================
				String data = "";
				try
				{
					data = dbtools.prepareDisplayRuns(epidemicID); //makes an array structure for d3, which is stored here then passed to page.
				}
				catch(Exception e)
				{
					System.out.println("No runs to display");
				}
				
				
				
				String generateHTML = dbtools.getEpidemicsHeader();
				request.setAttribute("menuBar", generateHTML); //generate the top header dynamically
				request.setAttribute("epidemicID", epidemicID);
				String message = request.getParameter("message");

				if (data.equalsIgnoreCase("]"))
				{
					message = "No runs for this epidemic yet.";
				}
				
				request.setAttribute("notification", message);
			        
			    RequestDispatcher view = request.getRequestDispatcher("manageEpidemicRuns.jsp");
			    request.setAttribute("data", data);
				view.forward(request, response);
				             
		        
			}
			
			public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
			{
					doGet(request, response);
			}
	}

