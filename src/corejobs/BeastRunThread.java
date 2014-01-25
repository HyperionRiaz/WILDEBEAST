package corejobs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class BeastRunThread implements Runnable {
	String command = "";
	String runFrom = "";
	datajobs.DatabaseUtilities dbtools = new datajobs.DatabaseUtilities();
	String epidemicID = "";
	String runID = "";
	String predictivePot = "";
	float chainLength = 0;
	
	public BeastRunThread(String command, String pathToRunFrom, String epidemicID, String runID, float chainLength, String predictivePot)
	{
		this.command = command;
		this.runFrom = pathToRunFrom;
		this.epidemicID = epidemicID;
		this.runID = runID;		
		this.chainLength = (int)chainLength; //TODO handle large numbers
		this.predictivePot = predictivePot;
		
		//this.c = "java -jar beastSource/lib/beast.jar -seed 1234 -overwrite beastSource/lib/input.xml";
	}
	
    public void run() {
               
        try {
	
        	File f = new File(runFrom+"/output.xml");
        	while(f.exists() == false) { 
  		
        		//System.out.println("File not found");
        		try {
            	    Thread.sleep(500); //need short pause TODO replace this with check file exists
            	} catch(InterruptedException ex) {
            	    Thread.currentThread().interrupt();
            	}

        	}
        
        	String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
            ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
            probuilder.directory(new File(runFrom)); //Change the working directory to the working directory of the currnetEpidemic and runID
            Process process = probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
            
            //Code for getting processID!
            try 
            {
            	Class clazz = Class.forName("java.lang.UNIXProcess");          	
    			Field pidField = clazz.getDeclaredField("pid");
    			pidField.setAccessible(true);
    			Object processID = pidField.get(process);    			
    			//System.err.println("Run started for command " + command + " with pid = " + processID);
				dbtools.addToRunToTable(epidemicID, runID, processID.toString(), chainLength, predictivePot); //If successful record in database

    			
	    	} catch (Throwable e) 
	    	{
	    			System.out.println("WARNING, ERROR WHEN TRYING TO START PROCESS FOR NEW RUN");
	    			e.printStackTrace();
	    	}
            
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader inputErrors = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            BufferedWriter out = new BufferedWriter(new FileWriter(runFrom+"/consoleOutput.txt"));
            BufferedWriter outError = new BufferedWriter(new FileWriter(runFrom+"/consoleOutputErrors.txt"));
            String line=null;

            while((line=input.readLine()) != null) {
                //System.out.println(line);
                //output = output + "\n" + line;
                out.write(line);
            }
            
            line = null;
            
            while((line=inputErrors.readLine()) !=null)
            {
            	outError.write(line);
            }
        
            //System.out.println("Done reading from console output");
            input.close();
            out.close();
            outError.close();
            inputErrors.close();
            
        } 
        catch(Exception e) 
        {
        	System.out.println("EXCEPTION IN BEASTRUNTHREAD");
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    
   

   
}
