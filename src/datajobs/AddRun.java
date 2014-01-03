package datajobs;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.*;

import corejobs.BeastUtilities;



public class AddRun extends HttpServlet {
	
	DatabaseUtilities dbtools = new DatabaseUtilities();

	/*
	 * 	A class for managing the addition of a new run for a new epidemic.
	 *  Is a data job because it inserts a new run into Runs table (through Database utilites)
	 *  Also starts a new core job, interfacing with corejobs through Beast utilities)
	 *  
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//Extract parameters for new run
		ServletContext c = getServletContext(); //TODO: Error checking if null
		
		String epidemicID = "" + c.getAttribute("epidemicID");
		boolean inputFine = true;
		String runID ="";
		float chainLength = 0;
		InputStream filecontent = null;
		String upname = "";
		
		  try {
		        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		        for (FileItem item : items) {
		            if (item.isFormField()) {
		                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
		                String fieldname = item.getFieldName();
		                String fieldvalue = item.getString();
		                
		                if (fieldname.equalsIgnoreCase("runID"))
		                {
		                	runID = fieldvalue;
		                }
		                else if (fieldname.equalsIgnoreCase("chainLength"))
		                {
		                	chainLength = Integer.parseInt(fieldvalue);
		                }
		                
		                //System.out.println(fieldname + fieldvalue);
		      
		            } else {
		                // Process form file field (input type="file").
		                String fieldname = item.getFieldName();
		                String filename = FilenameUtils.getName(item.getName());
		                upname = filename;
		                filecontent = item.getInputStream();
		                //System.out.println(filename);
	          
		            }
		        }
		    } catch (FileUploadException e) {
		    	inputFine = false; 
		        throw new ServletException("Cannot parse multipart request.", e);
		        
		    }
		  
		  if (dbtools.isRunUnique(epidemicID, runID) && inputFine) //If this runID doesn't already exist.
		  {

				File epDir = new File(epidemicID);
				if (!epDir.exists()) { epDir.mkdir();} //Create new directory if it doesn't exist.
				File runDir = new File(epidemicID+"/"+runID);
				runDir.mkdir();
				
				String fileType = upname.substring(upname.indexOf(".")+1, upname.indexOf(".")+4);
				//System.out.println(fileType);
				
				if (fileType.equalsIgnoreCase("nex")== true)
				{
					//TODO uploading nex file with sequence name that are not normal breaks this!
					upname = "output.nex"; //renames uploaded files..
					
					//Copy input nexus file to run directory
					System.out.println("*** Copying your uploaded file ***");
					OutputStream outputStream = new FileOutputStream(new File(epidemicID+"/"+runID+"/"+upname));
	
		    		int read = 0;
		    		byte[] bytes = new byte[1024];
		     
		    		while ((read = filecontent.read(bytes)) != -1) {
		    			outputStream.write(bytes, 0, read);
		    		}
		    		
		    		outputStream.close();
		    		    		
		    		//Add uploaded sequences to database, or check if they exist...
		    		String directoryForRun = epidemicID+"/"+runID+"/";
		    		String pathToNexus = epidemicID+"/"+runID+"/"+upname;
		    			    		
		    		//Nexus -> Fasta
		    		BeastUtilities.nexusToFasta(pathToNexus, directoryForRun);
		    		
		    		//Convert uploaded nexus file to beast file to start run (and creates .fasta file).
		    		BeastUtilities.nexusToBeastFile(pathToNexus, chainLength, 1000, runID, directoryForRun);
		    		
		    		//Analyse sequences.fasta file.
		    		String[] info = DatabaseUtilities.calculateImportance(directoryForRun);
		    		
		     		//TODO fix this parsing!
		    		DatabaseUtilities.processUploadedFile(epidemicID, directoryForRun);
		    		
		    		//Display important information on sequences, ie predictive power.
		    		String message = "Your uploaded sequences have the following parameters:\n";
		    		String predPot ="";
		    		for (int i = 0; i < info.length; i++)
		    		{
		    			
		    			if (info[i].length() > 4)
		    			{
		    		
		    				if (info[i].substring(0,3).equalsIgnoreCase("Spr"))
			    			{
			    				predPot = info[i].substring(info[i].indexOf(':')+2); //TODO get real measure
			    			}
		    			}

		    			message = message + info[i]+"\n";
		    		}
		    		message = message + "Are you sure you want to continue?";
		    		
		    		//Ask if user want to continue. 
		    		int option = JOptionPane.showConfirmDialog (null, message);

		    		if (option == JOptionPane.NO_OPTION || option == JOptionPane.CANCEL_OPTION) 
		    		{
		    			String me = "ERROR: Run cancelled."; 
				        
						response.sendRedirect("viewEpidemicRuns?message=" + me); //Now refresh the page.
		    		}
		    		else
		    		{
		    			//Try start this new run and will add it to table!!! //TODO dont add to table if it doesnt start, delete files also.
						boolean runStarted = BeastUtilities.newBeastRun(epidemicID, runID , chainLength, predPot);
						
						
						if (runStarted)
						{
							//A 2 sec delay before redirecting!
							response.setHeader("Refresh", "2; viewEpidemicRuns");									
						}
						else	//If fail redirect with error message
						{
							request.setAttribute("notification", "Could not start a new run for " + epidemicID); 
		
						}
		    		}
		
					
				}
				else
				{
					
					String message = "ERROR: You didn't upload a nexus file."; 
			        
					response.sendRedirect("viewEpidemicRuns?message=" + message); //Now refresh the page.
				}
			}
			else
			{

				String message = "ERROR: There was an error with your input. Check you have a unique ID/entered a number for chain length"; 
		        
				response.sendRedirect("viewEpidemicRuns?message=" + message); //Now refresh the page.
			}
      
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
	
	

}
