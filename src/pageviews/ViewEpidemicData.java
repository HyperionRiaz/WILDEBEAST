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


public class ViewEpidemicData extends HttpServlet {
	
	/*
	 * A class for generating the private MCMC view for an epidemic.
	 * 
	 */
	
			DatabaseUtilities dbtools = new DatabaseUtilities();
		
			public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
			{
				ServletContext c = getServletContext();
				//String epidemicID = request.getParameter("epidemicID");
				String epidemicID = (String) c.getAttribute("epidemicID");
				
  				ResultSet rs = null;
				try 
				{
					rs = (ResultSet) dbtools.getAllRuns(epidemicID);
					
				} catch (SQLException e) 
				{
					
					System.out.println("There are no runs to display.");
					e.printStackTrace();
				}

				String outputMCMCRuns = "The current sequences that have been uploaded for this epidemic are as follows:";
				try 
				{
					while (rs.next())
					{
						String id = rs.getString("id");
						String processID = rs.getString("processid");
						String timeStarted = rs.getString("timestarted");
						int chainlength = rs.getInt("chainlength");
						String row = id + "            " + processID + "            " + timeStarted + "             " + chainlength + "<br>";
						//System.out.println(rs.getString("id"));
						
						outputMCMCRuns = outputMCMCRuns + row;
					}
				} catch (Exception e) {
					
					
				}
				
				String generateHTML = dbtools.getEpidemicsHeader();
				request.setAttribute("menuBar", generateHTML); //generate the top header dynamically
				request.setAttribute("epidemicID", epidemicID);
				request.setAttribute("notification", request.getParameter("message"));			        
			    RequestDispatcher view = request.getRequestDispatcher("manageEpidemicData.jsp");
				request.setAttribute("sequenceData", outputMCMCRuns); 
				view.forward(request, response);
  
		        
			}
			
			public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
			{
					doGet(request, response);
			}
	}

