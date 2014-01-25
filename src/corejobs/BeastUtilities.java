package corejobs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.*;

import datajobs.DatabaseUtilities;
import datajobs.LogUtilities;
import datajobs.Run;


/*
 *  A class with utilities for interfacing with BEAST and it's data.
 *  Includes starting, stopping, analyzing BEAST runs.
 *  
 *  */

public class BeastUtilities {
	
	public static boolean newBeastRun(String epidemicID, String runID, float chainLength, String predictivePot)
	{
		//So for every epidemic, store the run information under a folder named as the epidemicID, 
		//and a subfolder for each run named with runID
		
		try
		{

			File epDir = new File(epidemicID);
			if (!epDir.exists()) { epDir.mkdir();} //Create new directory if it doesn't exist.
			File runDir = new File(epidemicID+"/"+runID);
			runDir.mkdir();
		
		
			//Now start a beast run with the options below. This will be passed to BeastRunThread which starts the run
			//from the appropriate sub directory (where output files will be genearted), retrieves the PID, 
			//and inserts the run info into the table.
			
			String message = "java -jar ../../beastSource/lib/beast.jar -seed 1234 -overwrite " + "output.xml";
			String pathToRunFrom = epidemicID+"/" + runID;
			Runnable r = new BeastRunThread(message, pathToRunFrom, epidemicID, runID, chainLength, predictivePot);
			new Thread(r).start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("WARNING - THERE WAS AN ERROR WHEN TRYING TO START A RUN");
			return false;

		}
		
		return true;

		
	}
	
	//  ***************************************************************************
	// 	***            Tools to work with different file types				    ***
	//  ***************************************************************************
	
	//Uses BeastConverts to convert an uploaded nexus file as well as params to an xml file
	public static boolean nexusToBeastFile(String nexusFilePath, float chainLength, int logEvery, String runID, String outputPath) throws IOException
	{
	
		System.out.println("*** Making beast file from nexus *** ");
        runID = "output";
        String finalPath = outputPath+runID+".xml";
        
        //TODO handle large numbers!
        String command = "./generateBeastXML.sh " + (int)chainLength + " " + logEvery + " " + nexusFilePath + " " + finalPath;
       
        //TODO got to get this working -> summary breaks for some reason.
        command = "./generateBeastXMLE.sh " + (int)chainLength + " " + logEvery + " " + nexusFilePath + " " + finalPath;
        
        System.err.println(command);
        
		String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
        Process process = probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
        
		return true;
	}
	
	public static boolean nexusToFasta(String nexusFilePath, String outputPath) throws IOException
	{
			System.out.println("*** Converting nexus to fasta ***");
	        String finalPath = outputPath+"sequences.fasta";
	        String command = "./generateFasta.sh " + nexusFilePath + " " + finalPath;
	        //System.out.println(command);
	        
			String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
	        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
	        Process process = probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
	        
			return true;
	}
	
	//TODO NOT DONE, FIX.
	public static boolean fastaToNexus(String fastaFilePath, String outputPath) throws IOException
	{
		String finalPath = outputPath+"sequences.fasta";
        String command = "./generateFasta.sh " + fastaFilePath + " " + finalPath;
        System.out.println(command);
        
		String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
        Process process = probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
        
		return true;
	}
			
	//  ***************************************************************************
	// 	***      			Tools to start and stop run						    ***
	//  ***************************************************************************
	
	//Starts a BEAST run
	public static String runBEAST()
		{
			String output = "";
			
			
			boolean runHere = true;
			
			if (runHere)
			{
				
				try {
		            Runtime rt = Runtime.getRuntime();
		            Process pr = rt.exec(" pwd");
		           
		            //pr = rt.exec("touch aids");
		            pr = rt.exec("java -jar beastSource/lib/beast.jar -seed 1234 beastSource/lib/output.xml");
		           
		            //TODO ERROR STREAM pr.getErrorStream();
		            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		
		            String line=null;
		            
		            BufferedWriter out = new BufferedWriter(new FileWriter("MCMCoutput.txt"));

		
		            while((line=input.readLine()) != null) {
		                //System.out.println(line);
		                output = output + "\n" + line;
		                out.write(line);
		            }
		
		         
		            int exitVal = pr.waitFor();
		            System.out.println("Exited with error code " );
		            System.out.println(output);
		            out.close();
		        } catch(Exception e) {
		            System.out.println(e.toString());
		            e.printStackTrace();
		        }
			}
			else
			{
				//.(new HelloThread()).start();
				
			}
			
			return output;
		}
	
	//Stops a beast run.
	public static void stopRun(String processID) throws IOException, SQLException
	{
		System.out.println("*** Stoppung run with ID " + processID + " ***");
		

        System.out.println("Process ID for " + processID + " = " + processID);
        String command = "./killrun.sh " + processID;
   
		String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
        probuilder.directory(new File("scripts"));
        Process process = probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
       		
	}
 
	
	
}

