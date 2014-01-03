package datajobs;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LogUtilities {

	//  ***************************************************************************
	// 	***    Methods for accessing log/tree files of a specific Run 		 ***
	//  ***************************************************************************

	
	
	public static int checkCurrentStep(String epidemicID, String runID, int runLength) throws IOException
	{
		 String everything = "";
		 String f = "";
		 String logFilePath = epidemicID+"/"+runID+"/output.log";
		 File logFile = new File(logFilePath);
		 if (logFile.exists())
		 {
			 
			 f = tail(logFile);
			 /*
			 BufferedReader br = new BufferedReader(new FileReader(logFilePath));
			    try {
			        StringBuilder sb = new StringBuilder();
			        String line = br.readLine();
			        
			        while (line != null) {
			            sb.append(line);
			            sb.append('\n');
			            f = line;
			            line = br.readLine();
			           
			            
			            
			        }
			        
			       everything = sb.toString();
			    } finally {
			        br.close();
			    }
			    */
			    
			    //System.out.println(f);
			    String outputs[] = f.split("\t");
			    
			    //System.out.println(outputs[0]);
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
	public static String tail( File file ) {
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
	
	//Gets the CI of TMRCA and R0. //TODO may not work for runs that don't have TMRCA as a parameter! Need to generalise.
	public String getCI(String epidemicID, String runID) throws IOException
	{
		System.out.println("*** Converting log file to summary ***");
		
        String finalPath = "../"+epidemicID+"/"+runID+"/output.log";
        String outputPath = "../"+epidemicID+"/"+runID+"/summary.txt";
        String command = "./generateLogSummary.sh " + finalPath + " " + outputPath;
        //System.out.println(command);
        
      
		String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
        probuilder.directory(new File("scripts"));
        Process process = probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
        
        command = "./getCI.sh " + epidemicID+"/"+runID+"/summary.txt";
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
	public Map getESS(String epidemicID, String runID) throws IOException
	{
		
		System.out.println("*** Converting log file to ESS summary ***");
		String outputPath = epidemicID+"/"+runID+"/summary.txt";
		
		/* If need to remake the file...
        String finalPath = "../"+epidemicID+"/"+runID+"/output.log";
        
        String command = "./generateESSSummary.sh " + finalPath + " " + outputPath;
        //System.out.println(command);
        
      
		String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
        probuilder.directory(new File("scripts"));
        Process process = probuilder.start(); //Start the run. All outputs will appear in the subdirectory!

        
         BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
         String line=null;
         String tmp = "";
            while((line=input.readLine()) != null) {
            	System.out.println(line);
                //System.out.println(line);
                //output = output + "\n" + line;
                tmp = tmp + "\n" + line;
            }
           input.close();
		 */
		
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
					ESS.put(words[0], words[6]);
				}
			}
			
			return ESS;
	}

	//Gets MFCC from summary.txt file by calling TreeAnnotator. WARNING, takes a long time to run...
	public String getMFCC(String epidemicID, String runID) throws IOException
	{
		
		System.out.println("*** Getting MFCC ***");
		String outputPath = epidemicID+"/"+runID+"/MFCC.txt";
		
        String finalPath = epidemicID+"/"+runID+"/output.trees";
        
        String command = "./generateMFCC.sh " + finalPath + " " + outputPath;
        //System.out.println(command);
        
      
		String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
        Process process = probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
        System.out.println(command);
     
			return "";
	}

	public String getPhylo(String epidemicID, String runID, String fileType) throws IOException
	{
		
		System.out.println("*** Getting MFCC as " + fileType +" ***");
		
		String finalPath = epidemicID+"/"+runID+"/MFCC.txt";
		
        String outputPath = epidemicID+"/"+runID+"/phylogeny." +fileType;
        
        String command = "./generatePhylo.sh " + fileType.toUpperCase() + " " + finalPath + " " + outputPath;
        //System.out.println(command);
        
      
		String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
        Process process = probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
        System.out.println(command);
        
        return outputPath;
         
	}
}


