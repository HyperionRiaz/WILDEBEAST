package datajobs;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 

public class AddEpidemicDatabase extends HttpServlet {
	
	 DatabaseUtilities dbtools = new DatabaseUtilities();

	/*
	 * 	A class for managing the addition of a new epidemic to the webserver. Creates database for it plus required tables.
	 *  Interfaces with form to do error checking.
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String epidemicID = request.getParameterValues("epidemicID")[0];
		String backgroundInfo = request.getParameterValues("backgroundInfo")[0];
		String priority = request.getParameterValues("priority")[0];
		
		boolean added = dbtools.addToEpidemicDatabase(epidemicID, backgroundInfo, priority); //Adds to epidemics database
		
		//Generate its own database and 3 tables.
		if (added)
		{
			try {
				dbtools.createEpidemicDatabaseAndTables(epidemicID);
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println("Error generating tables for this epidemic");
			}
		}
		
		String message = added ? "Your new epidemic has been added." : "Error adding new epidemic. Please try again";
		request.setAttribute("notification", message);
        request.setAttribute("menuBar", dbtools.getEpidemicsHeader()); //generate the top header dynamically
        RequestDispatcher view = request.getRequestDispatcher("index.jsp");
        view.forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
}