package datajobs;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogUtilities {

	//  ***************************************************************************
	// 	***   Globally generating summary files from log files for all runs     ***
	//  ***************************************************************************
	
	public static void generateAllSummaryFiles() throws SQLException, IOException
	{
		ArrayList<String> epidemicIDs = DatabaseUtilities.getAllEpidemicIDs();
		
		for (int i = 0; i < epidemicIDs.size(); i++) //Loop through all epidemics
		{
			String epidemicID  = epidemicIDs.get(i);
			ResultSet rs = DatabaseUtilities.getAllRuns(epidemicID);
			
			while (rs.next()) //Loop through all runs
			{
				String runID = rs.getString("id");
				
				createSummaryFile(epidemicID, runID);
			}
			
			rs.close();
			
		}
	}
	
	//  ***************************************************************************
	// 	***    Methods for accessing log/tree files of a specific Run 		 ***
	//  ***************************************************************************

	public static int checkCurrentStep(String epidemicID, String runID, int runLength) throws IOException
	{
		 String f = "";
		 String logFilePath = epidemicID+"/"+runID+"/output.log";
		 File logFile = new File(logFilePath);
		 if (logFile.exists())
		 {
			 f = tail(logFile);
			 String outputs[] = f.split("\t");
			 int currentRunStep = Integer.parseInt(outputs[0]);
			 return currentRunStep;
			    
		 }
		 else
		 {
			 System.err.print("The log file for run " + runID + " could not be found.");
			 return -1;
		 }
	
	}

	//Credit :http://stackoverflow.com/questions/686231/quickly-read-the-last-line-of-a-text-file
	public static String tail(File file ) {
	    RandomAccessFile fileHandler = null;
	    try {
	        fileHandler = new RandomAccessFile( file, "r" );
	        long fileLength = fileHandler.length() - 1;
	        StringBuilder sb = new StringBuilder();

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	            if( readByte == 0xA ) {
	                if( filePointer == fileLength ) {
	                    continue;
	                } else {
	                    break;
	                }
	            } else if( readByte == 0xD ) {
	                if( filePointer == fileLength - 1 ) {
	                    continue;
	                } else {
	                    break;
	                }
	            }

	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        return lastLine;
	    } catch( java.io.FileNotFoundException e ) {
	        e.printStackTrace();
	        return null;
	    } catch( java.io.IOException e ) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        if (fileHandler != null )
	            try {
	                fileHandler.close();
	            } catch (IOException e) {
	                /* ignore */
	            }
	    }
	}
	
	//Generate a summary file from log file
	public static boolean createSummaryFile(String epidemicID, String runID) throws IOException
	{
	
      	File f2 = new File(epidemicID+"/"+runID+"/summary.txt");
      	if (f2.exists())
      	{
      		//System.out.println("Old file DELETED");
      		//f2.delete();
      		
      	}
		
        String finalPath = "../"+epidemicID+"/"+runID+"/output.log";
        String outputPath = "../"+epidemicID+"/"+runID+"/summary.txt";
        String command = "./generateLogSummary.sh " + finalPath + " " + outputPath;
        
		String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
        probuilder.directory(new File("scripts"));
        Process p = probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
        
        
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        while ( (line = br.readLine()) != null) { //Check if log file is there!
        	if (line.equalsIgnoreCase("File not found!") || line.equalsIgnoreCase("File found."))
        	{
  
        		break;
        	}
        
        }

        if (line.equalsIgnoreCase("File not found!"))
        {
        	System.err.println("WARNING: Failed to generate a summary file for " + runID);
        	return false;
        }
        else
        {
        	
        	
//        	try {
//           	   Thread.sleep(10000); //need short pause TODO IF THIS IS 1000 PROGRAMS MESSES UP!!!
//             } catch(InterruptedException ex) {
//           	    Thread.currentThread().interrupt();
//           	}
        	
        	//Wait until file is there for sure.
          	File f = new File(epidemicID+"/"+runID+"/summary.txt");
          	
          	
            while(f.exists() == false) { 
          		
            	
              		System.out.println("File not found");
              		try {
                  	   Thread.sleep(500); //need short pause TODO IF THIS IS 1000 PROGRAMS MESSES UP!!!
                    } catch(InterruptedException ex) {
                  	    Thread.currentThread().interrupt();
                  	}

              	}
            
            System.out.println(runID + " : " + tail(f));
        
            return true;
              		
        }
     	
	}
	
	
	//  ***************************************************************************
	// 	***    Methods for  getting specific info from a log file 		 ***
	//  ***************************************************************************
	
	//Gets the CI of TMRCA and R0. //TODO may not work for runs that don't have TMRCA as a parameter! Need to generalise.
	public static String getCI(String epidemicID, String runID) throws IOException
	{
		//TODO MAKE THIS MORE ELEGANT...
		
        String command = "./getCI.sh " + epidemicID+"/"+runID+"/summary.txt";
        //System.out.println(command);
        String[] commandB = command.split(" ");
        ProcessBuilder probuilderB = new ProcessBuilder( commandB ); //Construct a processBuilder with all arguments
   
        Process processB = probuilderB.start(); //Start the run. All outputs will appear in the subdirectory!
        
         BufferedReader input = new BufferedReader(new InputStreamReader(processB.getInputStream()));
        
         String line=null;
         String tmp = "";
            while((line=input.readLine()) != null) {
            	System.out.println(line);
                //System.out.println(line);
                //output = output + "\n" + line;
                tmp = tmp + "\n" + line;
            }
           input.close();
           
          return tmp;
	}
	
	//Get ESS for different parameters
	public static Map<String, String> getESS(String epidemicID, String runID) throws IOException
	{
		//createSummaryFile(epidemicID, runID);
		
		//System.out.println("*** Converting log file to ESS summary ***");
		String outputPath = epidemicID+"/"+runID+"/summary.txt";

		String sCurrentLine;
            BufferedReader br = new BufferedReader(new FileReader(outputPath));			
			Map<String, String> ESS = new HashMap<String, String>();
			while ((sCurrentLine = br.readLine()) != null) {
				String[] words = sCurrentLine.split("\t");
				
				if (words[0].equalsIgnoreCase("statistic") || words[0].equalsIgnoreCase("burnIn") || words.length < 6)
				{
					
					continue;
				}
				else
				{ 
					//System.out.println(words[0] + " " + words[6]);
					ESS.put(words[0], words[6]);
				}
			}
			br.close();
			return ESS;
	}

	//Get posterior mean estimates for different parameters
	public static Map<String, String> getPosteriorMeanEstimates(String epidemicID, String runID) throws IOException
	{
			//createSummaryFile(epidemicID, runID);
			
			System.out.println("*** Converting log file to Posterior Mean summary ***");
			String outputPath = epidemicID+"/"+runID+"/summary.txt";

	           	String sCurrentLine;
	            BufferedReader br = new BufferedReader(new FileReader(outputPath));			
				Map<String, String> ESS = new HashMap<String, String>();
				while ((sCurrentLine = br.readLine()) != null) {
					String[] words = sCurrentLine.split("\t");
					
					if (words[0].equalsIgnoreCase("statistic") || words[0].equalsIgnoreCase("burnIn") || words.length < 6)
					{
						continue;
					}
					else
					{
						ESS.put(words[0], words[1]); //2nd item on each row
					}
				}
				br.close();
				return ESS;
		
	}
	
	//Get CI low to high estimates for different parameters
	public static Map<String, String> getCIValues(String epidemicID, String runID) throws IOException
	{
				//createSummaryFile(epidemicID, runID);
				System.out.println("*** Converting log file to CI low and high summary ***");
				String outputPath = epidemicID+"/"+runID+"/summary.txt";
				
				try
				{
		           	String sCurrentLine;
		            BufferedReader br = new BufferedReader(new FileReader(outputPath));			
					Map<String, String> ESS = new HashMap<String, String>();
					while ((sCurrentLine = br.readLine()) != null) {
						String[] words = sCurrentLine.split("\t");
						
						if (words[0].equalsIgnoreCase("statistic") || words[0].equalsIgnoreCase("burnIn") || words.length < 6)
						{
							continue;
						}
						else
						{
							ESS.put(words[0], words[7] + " to " + words[8]); //2nd item on each row
						}
					}
					
					br.close();
				
					return ESS;
				}
				catch (Exception e)
				{
					System.err.println("Could not create summary for " + runID);
				}
			return new HashMap<String, String>();
	}
	
	//Gets MFCC from summary.txt file by calling TreeAnnotator. WARNING, takes a long time to run...
	public String getMFCC(String epidemicID, String runID) throws IOException
	{	
		System.out.println("*** Getting MFCC ***");
		String outputPath = epidemicID+"/"+runID+"/MFCC.txt";
        String finalPath = epidemicID+"/"+runID+"/output.trees";
        String command = "./generateMFCC.sh " + finalPath + " " + outputPath;
		String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
        probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
        return "";
	}

	public String getPhylo(String epidemicID, String runID, String fileType) throws IOException
	{
		
		System.out.println("*** Getting MFCC as " + fileType +" ***");
		String finalPath = epidemicID+"/"+runID+"/MFCC.txt";
		String outputPath = epidemicID+"/"+runID+"/phylogeny." +fileType;
        String command = "./generatePhylo.sh " + fileType.toUpperCase() + " " + finalPath + " " + outputPath;
		String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
        probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
        //System.out.println(command);
        return outputPath;
         
	}

	private static boolean isCompletelyWritten(File file) {
	    RandomAccessFile stream = null;
	    try {
	        stream = new RandomAccessFile(file, "rw");
	        return true;
	    } catch (Exception e) {
	        //log.info("Skipping file " + file.getName() + " for this iteration due it's not completely written");
	    } finally {
	        if (stream != null) {
	            try {
	                stream.close();
	            } catch (IOException e) {
	                //log.error("Exception during closing file " + file.getName());
	            }
	        }
	    }
	    return false;
	}
	
}


