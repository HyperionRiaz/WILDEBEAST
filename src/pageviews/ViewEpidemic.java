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
			//ServletContext c = getServletContext();
			//String epidemicID = (String) c.getAttribute("epidemicID");
			
			boolean exists = dbtools.checkEpidemicExists(epidemicID);
			
			//Generate header for next page [More efficent way?]
			String generateHTML = dbtools.getEpidemicsHeader();
			request.setAttribute("menuBar", generateHTML); //generate the top header dynamically
			
			if (exists)
			{
					request.setAttribute("epidemicID", epidemicID);
			        RequestDispatcher view = request.getRequestDispatcher("viewEpidemicSummary.jsp");
					request.setAttribute("bodyContent", dbtools.getEpidemicSummary(epidemicID)); 
					ServletContext c = getServletContext(); //Set ServletContext to indicate we're dealing with this epidemic
					c.setAttribute("epidemicID", epidemicID);
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

