package corejobs;


import java.io.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.naming.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import datajobs.LogUtilities;

import java.util.Timer;
import java.util.TimerTask;
/**
* Cron Job.
*
* @author Daynight
* @version 1.0 02/22/2011
*/
public class CronJob {
private final Timer timer = new Timer();
private int minutes = 15;

/**
* Web Call method.
*
*/
		public void getJobInfo() 
		{
		try {
				//Properties from web.xml enviornment file
				Context initCtx = new InitialContext();
				Context envCtx = (Context)initCtx.lookup("java:comp/env");
				String ctxminutes = (String) envCtx.lookup("Minutes");
				this.minutes = Integer.parseInt(ctxminutes);
				startScheduler();
			}
			catch (Exception e) {
			}
		}

/**
* start schedular.
*
*/
	private void startScheduler() 
	{
		
		
			timer.schedule(new TimerTask()
			
			{
				
				
			public void run(){
				try {
					try {
						scheduleParse();
//						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//						Date date = new Date();
//						ServletContext c = getServletContext();
//						c.setAttribute("sumsUpdated", dateFormat.format(date));
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//timer.cancel();
			}
			
			private void scheduleParse() throws IOException, SQLException 
			{
				//write your business logic here
				
				System.err.println("--- UPDATING SUMMARY FILES! --- ");
				LogUtilities.generateAllSummaryFiles();
				
				
				
			}
			
			
		
		}, 0, this.minutes * 60 * 1000);
		
	}
}