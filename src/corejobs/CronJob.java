package corejobs;


import java.io.*;
import java.util.*;

import javax.naming.*;

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
public void getJobInfo() {
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
	private void startScheduler() {
		
		
			timer.schedule(new TimerTask(){
				
				
			public void run(){
				try {
					scheduleParse();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//timer.cancel();
			}
			
			private void scheduleParse() throws IOException 
			{
			//write your business logic here
				
				Runtime rt = Runtime.getRuntime();
			    Process pr = rt.exec("touch omgitrun.txt");
			}
			
			
		
		}, 0, this.minutes * 60 * 1000);
		
	}
}