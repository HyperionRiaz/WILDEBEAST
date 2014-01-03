package datajobs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Run 
{
	String runID;
	int processID;
	String timeStarted;
	int chainlength;
	String predictivePot;
	float currentStep;
	double percentDone;
	String path;
	long hoursSince;
	
	Date dateStart;
	
	
	Map<String, String> displayData = new HashMap<String, String>();
	
	public Run(String epidemicID, String runID) throws SQLException
	{
		ResultSet rs = DatabaseUtilities.getRun(epidemicID, runID);
		while (rs.next())
		{

			this.path = epidemicID+"/"+runID+"/";
			this.runID = runID;
			this.processID = rs.getInt("processid");
			this.timeStarted = rs.getString("timestarted");
			this.chainlength = rs.getInt("chainlength");
			this.predictivePot = rs.getString("predictivepot"); 
			this.currentStep = -1;
			
			
			try
			{
				this.currentStep = LogUtilities.checkCurrentStep(epidemicID, runID, chainlength);
			}
			catch(Exception e)
			{
				System.err.println("Error getting current step from log file for " + runID);
			}
			
			DecimalFormat df2 = new DecimalFormat("###.##");
			this.percentDone = ((currentStep/chainlength)*100);
			percentDone = Double.valueOf(df2.format(percentDone));
	
			if (currentStep >= chainlength)
			{
				percentDone = 100;
			}
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
			try {
				this.dateStart = dateFormat.parse(timeStarted);
				this.hoursSince = DatabaseUtilities.getDateDiff(dateStart, TimeUnit.HOURS);

				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			fillDisplay();
		}
		
	}
	
	public double getPredPot()
	{
		return Double.parseDouble(this.predictivePot);
	}
	
	public int getProcessID()
	{
		return processID;
	}
	
	public void fillDisplay()
	{
		displayData.put("path", path);
		displayData.put("processID", processID+"");
		displayData.put("timeStarted", timeStarted+"");
		displayData.put("chainLength", chainlength+"");
		displayData.put("predictivePot", predictivePot+"");
		displayData.put("currentStep", currentStep+"");
		displayData.put("percentDone", percentDone+"");
		displayData.put("hoursSince", hoursSince+"");
	}
	
	public Map getDisplay()
	{
		System.out.println("Getting display");
		return displayData;
	}

}
