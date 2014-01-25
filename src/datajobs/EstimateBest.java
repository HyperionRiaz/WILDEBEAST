package datajobs;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import corejobs.AnalyseRuns;
import corejobs.BeastUtilities;

public class EstimateBest extends HttpServlet
{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		ServletContext c = getServletContext(); 
		String epidemicID = "" + c.getAttribute("epidemicID");
		
		try
		{
			
			String totESS = AnalyseRuns.getBestEstimateReal(epidemicID);
			JOptionPane.showMessageDialog(null, totESS);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("Could not get sumESS!");
		}
		
		//Redirect
		response.sendRedirect("viewEpidemic?epidemicID="+epidemicID);     
        //RequestDispatcher view = request.getRequestDispatcher("viewEpidemicSummary.jsp");
		//view.forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

}
