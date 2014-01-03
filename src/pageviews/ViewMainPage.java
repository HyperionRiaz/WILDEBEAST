package pageviews;

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import corejobs.*;

/** Simple servlet for testing. Makes use of a helper class. */

public class ViewMainPage extends HttpServlet 
{
  @Override
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
 
	  
	  
	Runtime rt = Runtime.getRuntime();
    Process pr = rt.exec(" cd beastSource");
    pr = rt.exec("open beastSource/TreeAnnotator.app");
      
      
	response.setContentType("text/html");  // Set content type of the response so that jQuery knows what it can expect.
     
    PrintWriter out = response.getWriter();
    
    String runOutput = "";
    
    BufferedReader br = new BufferedReader(new FileReader("exampleRun.log"));
    try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }
        runOutput = sb.toString();
    } finally {
        br.close();
    }
    
    
	out.write(runOutput);  
    
    
   
  }
}
