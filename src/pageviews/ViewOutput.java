package pageviews;

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import corejobs.*;

/** Simple servlet for testing. Makes use of a helper class. */

@WebServlet("/test-with-utils")
public class ViewOutput extends HttpServlet 
{
  @Override
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
    
	  
	  
	  response.setContentType("text/html");  // Set content type of the response so that jQuery knows what it can expect.
      //response.setCharacterEncoding("UTF-8");
      
    PrintWriter out = response.getWriter();
    String title = "Test Servlet with Utilities";
    
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
