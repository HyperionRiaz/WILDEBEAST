package corejobs;


	import java.io.*;
	import java.util.*;
	import javax.servlet.*;
	import javax.servlet.http.*;
/**
* Parser Schedular to run every one hour to parse the file.
*
* @author Daynight
* @version 1.0 02/23/2011
*/

public class ParseSchedular extends HttpServlet {
/**
* init method - when tomcat server starts the servlet init.
*
* @param config- ServletConfig 
*/
public void init(ServletConfig config) throws ServletException {
super.init(config);
try {
CronJob pp = new CronJob ();
pp.getJobInfo();
}
catch (Exception e) {
}
}

/**
* Get method.
*
* @param request - HttpServletRequest
* @param response - HttpServletResponse
*/
public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
doPost(request, response);
}

/**
* Post method.
*
* @param request - HttpServletRequest
* @param response - HttpServletResponse
*/
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
}
}