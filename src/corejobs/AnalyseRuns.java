package corejobs;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import datajobs.DatabaseUtilities;
import datajobs.LogUtilities;

public class AnalyseRuns {
	
	//  ***************************************************************************
	// 	***      			Tools to get best estimate						    ***
	//  ***************************************************************************
	
	//For now, get all runs, and return the info from the run with the highest potential.
	public static String getBestEstimate(String epidemicID) throws SQLException
	{
		System.out.println("*** Getting best estimate for epidemic ***");
		ResultSet rs = DatabaseUtilities.getAllRuns(epidemicID);
		String runID = "";
		
		while(rs.next())
		{
			runID = rs.getString("id");
		}
		
		System.out.println("Fittest run is " +runID);
		
		return runID;
	}
	
	//A real attempt to get best estimate
	public static String getBestEstimateReal(String epidemicID) throws SQLException, IOException
	{
		//TODO find more elegant way to ignore runs that summary files can't be generated from.
		
		System.err.println("*** Getting real best estimate for epidemic ***");
		ResultSet rs = DatabaseUtilities.getAllRuns(epidemicID);
		String runID = "";
		double bestRealised = 0;
		String bestRun = "";
		String allInfo = "";
		
		while(rs.next())
		{
			runID = rs.getString("id");
			
			//1.) Generates summary file for this run //TODO make faster?
			//LogUtilities.createSummaryFile(epidemicID, runID);
			
			//1.)Gets sum of ESS
			 Map ESS = LogUtilities.getESS(epidemicID, runID);
			 String ESSout = "";
			 
			 Iterator it = ESS.entrySet().iterator();
			 double sumESS =0;
			 while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			      	ESSout += pairs.getKey() + " = " + pairs.getValue() + "<br>";
			      	
			      	if ((pairs.getValue()+"").equalsIgnoreCase("?")) //This param doesn't have an ESS yet
			      	{
			      		System.err.println("Can't get ESS for parameter " + pairs.getValue()+"");
			      	}
			      	else
			      	{
			      		sumESS+=  Double.parseDouble(pairs.getValue()+"");
			      	}
			      	
			        it.remove(); // avoids a ConcurrentModificationException
			    }
			 
			 //Now retrieve the potential of each run 
			 ResultSet rt = DatabaseUtilities.getRun(epidemicID, runID);
			 
			 String pot = "";
			 while (rt.next())
			 {
				 pot = rt.getString("predictivepot");
			 }
			 
			 double realisedPotential = Math.log((sumESS))/Float.parseFloat(pot);
			 allInfo += "RunID: " + runID + "\tTotEss: " + sumESS + "\t:" + realisedPotential + "\n";
			 
			 if (realisedPotential > bestRealised)
			 {
				 bestRealised = realisedPotential;
				 bestRun = runID;
			 }
			    
		}
		
		//System.out.println("Fittest run is " +runID);
		
		//return runID + ": " + bestRealised;
		return allInfo+"\n\nBEST: " + bestRealised + " ID: " + bestRun;
	}

}
