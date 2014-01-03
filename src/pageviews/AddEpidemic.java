package pageviews;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;

import datajobs.DatabaseUtilities;
 
public class AddEpidemic extends HttpServlet {
 
	DatabaseUtilities dbtools = new DatabaseUtilities(); 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	String generateHTML = dbtools.getEpidemicsHeader();
        
        request.setAttribute("menuBar", generateHTML);    
        RequestDispatcher view = request.getRequestDispatcher("addEpidemicPage.jsp");
        view.forward(request, response);
        
    
       
    }
}
