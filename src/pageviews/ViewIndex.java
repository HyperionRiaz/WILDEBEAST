package pageviews;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datajobs.DatabaseUtilities;

public class ViewIndex extends HttpServlet {
 
	DatabaseUtilities dbtools = new DatabaseUtilities();
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     
       String generateHTML = dbtools.getEpidemicsHeader();
        //System.out.println(generateHTML);
        //System.out.println("PRoblems");
        request.setAttribute("notification", "Welcome"); 
        request.setAttribute("menuBar", generateHTML); //generate the top header dynamically
        //System.out.println("Hello?");
        RequestDispatcher view = request.getRequestDispatcher("index.jsp");
        view.forward(request, response);
 
       
    }
}

