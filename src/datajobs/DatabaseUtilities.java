package datajobs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.text.DateFormat;


public class DatabaseUtilities 
{
	//  ***************************************************************************
	// 	***    Prepared statements for database access					        ***
	//  ***************************************************************************
	
	static String insertTableSQL = "INSERT INTO Epidemics"
			+ "(epidemicID, addDate, backgroundInfo, priority) VALUES"
			+ "(?,?,?,?)";	
	
	static String viewEpidemicsSQL = "SELECT * from Epidemics order by priority desc;";
	
	static String getEpidemicBackground = "SELECT backgroundInfo from Epidemics WHERE epidemicID = ?";
	
	static String checkEpidemicExists = "SELECT * from Epidemics WHERE epidemicID = ?";
	
	static String insertRunsTableSQL = "INSERT INTO Runs"
			+ "(id, processID, timestarted, chainlength, predictivepot) VALUES"
			+ "(?,?,?,?, ?)";	
	
	static String insertSequencesTableSQL = "INSERT INTO Sequences"
			+ "(id, seqdata, subdate, coldate, founddate, loc, descrip) VALUES"
			+ "(?,?,?,?, ?, ?, ?)";	
	
	static String checkRunExists = "SELECT * from Runs WHERE id = ?";
	
	static String checkSequenceExists = "SELECT * from Sequences WHERE id = ?";
	
	String viewEpidemicRuns = "Select * from ?.Runs;";
	
	String viewEpidemicSequences = "Select * from ?.Sequences;";
	
	//  ***************************************************************************
	// 	***    Methods for accessing Epidemic (database of all Epidemics)       ***
	//  ***************************************************************************

	//General method to connect to a database and return a Connection object
	public static Connection connectToDatabase(String databasename, String USER, String PASS)
	{
	  
	    String DB_URL="jdbc:mysql://localhost/" + databasename;
	    Connection conn = null;
	  	try{
	           // Register JDBC driver
	           Class.forName("com.mysql.jdbc.Driver");
	           // Open a connection
	           conn = DriverManager.getConnection(DB_URL, USER, PASS);
	         
	  	}
	  	catch(Exception e)
	  	{
	  		System.out.println("CONNECTION ERROR: " );
	  		e.printStackTrace();
	  	}
	  	
	  	return conn;
	      
	}
		
	//Method for inserting into epidemic database //TODO FIX DATE
	public boolean addToEpidemicDatabase(String epidemicID, String backgroundInfo, String priority)
	{
		boolean completed = true;
		
		try
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String now = dateFormat.format(date);
			Connection c = connectToDatabase("epidemics", "riaz", "Mordor");						
			PreparedStatement preparedStatement = (PreparedStatement) c.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, epidemicID);	
			preparedStatement.setString(2,  now);
			preparedStatement.setString(3, backgroundInfo);
			preparedStatement.setInt(4, Integer.parseInt(priority));
			preparedStatement .executeUpdate();
			preparedStatement.close();
	        c.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			completed = false;
		}
		
		return completed;
	}
	
	//Method for getting an epidemic summary
	public String getEpidemicSummary(String epidemicID)
	{
		String background = "";
		try
		{
			Connection c = connectToDatabase("epidemics", "riaz", "Mordor");						
					
			PreparedStatement preparedStatement = (PreparedStatement) c.prepareStatement(getEpidemicBackground);
			preparedStatement.setString(1, epidemicID);
			ResultSet rs = preparedStatement.executeQuery();

			
			while (rs.next())
			{
				background = rs.getString("backgroundInfo");
	        
			}
			
			preparedStatement.close();
	        c.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
	
		}
		
		return background;
		
	}
	
	//Method for checking an epidemic exists
	public boolean checkEpidemicExists(String epidemicID)
	{
		boolean exists = false;
		
		try
		{
			Connection c = connectToDatabase("epidemics", "riaz", "Mordor");						
					
			PreparedStatement preparedStatement = (PreparedStatement) c.prepareStatement(checkEpidemicExists);
			preparedStatement.setString(1, epidemicID);
			ResultSet rs = preparedStatement.executeQuery();
			
			exists = rs.next();
		}
		catch (Exception e)
		{
			e.printStackTrace();
	
		}
		
		return exists;
		
	}
	
	//Searches epidemics database and filters by priority to show on top bar
	public String getEpidemicsHeader()
	{
		//System.out.println("Getting epidemic headers");
		String html = "";
		
		try
		{
			Connection c = connectToDatabase("epidemics", "riaz", "Mordor");						
					
			PreparedStatement preparedStatement = (PreparedStatement) c.prepareStatement(viewEpidemicsSQL);
			ResultSet rs = preparedStatement.executeQuery();
			
			int menuCount = 435;
			
			while (rs.next())
			{
				String id  = rs.getString("epidemicID");
	            //Date date = rs.getDate("addDate");
	            String backgroundInfo = rs.getString("backgroundInfo");
	            int getPrio = rs.getInt("priority");
	            html = html + "<li class='menu-'"+menuCount+"'><a href='viewEpidemic?epidemicID=" +id+ "' title='" + backgroundInfo +"'>" + id +"</a></li>";
	            menuCount+=1;
	     
			}
			
			preparedStatement.close();
	        c.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
	
		}
		
		return html; 
		
	}

	
	//  **************************************************************************
	// 	*** Methods for dealing with Database/tables of a specific Epidemic    ***
	//  **************************************************************************
	
	// == Methods for setup == 
	
	//Method for creating the new epidemic database (eg one just for H1N12009) with its 3 subtables
	public void createEpidemicDatabaseAndTables(String dbname) throws SQLException
		{
			Connection Conn = DriverManager.getConnection
					("jdbc:mysql://localhost/", "riaz", "Mordor"); 
					Statement s=Conn.createStatement();
					int Result=s.executeUpdate("CREATE DATABASE " + dbname);
					System.out.println("Result of creating DB is: " + Result); 
					
			Conn.close();
			
			//Create Sequences table
			Connection conn = connectToDatabase(dbname, "riaz", "Mordor");
			Statement stmt = conn.createStatement();	      
		    String sql = "create table Sequences(id varchar(256) not null, seqdata text not null, subdate date, coldate date, founddate date, loc text, descrip text, primary key (id));";
		    stmt.executeUpdate(sql);
		    stmt.close();
		    conn.close();
		      
		    //Create Runs table
		    Connection conn2 = connectToDatabase(dbname, "riaz", "Mordor");
			Statement stmt2 = conn2.createStatement();
			String sql2 = "create table Runs( id varchar(256) not null, processid int not null, timestarted datetime not null, chainlength int not null, predictivepot text, primary key(id) );";
			stmt2.executeUpdate(sql2);
			stmt2.close();
			conn2.close();
		     
			//Create Sequence_Runs table
			Connection conn3 = connectToDatabase(dbname, "riaz", "Mordor");
			Statement stmt3 = conn3.createStatement();
			String sql3 = "create table Sequences_Runs( sequence_id varchar(256) not null, run_id varchar(256) not null, primary key (sequence_id, run_id), foreign key (sequence_id) references Sequences(id) on update cascade, foreign key (run_id) references Runs(id) on update cascade);";
			stmt3.executeUpdate(sql3);
			stmt3.close();
			conn3.close();
			
			
		}
		
	// == Methods for Runs ==
	
	public ResultSet getAllRuns(String epidemicID) throws SQLException
	{
		ResultSet rs = null;
		try
		{
			
			Connection conn  = connectToDatabase(epidemicID, "riaz", "Mordor");
			PreparedStatement preparedStatement = (PreparedStatement) conn.prepareStatement("Select * from " + epidemicID +".Runs order by cast(predictivepot as unsigned) asc;");
	
	        rs = preparedStatement.executeQuery();
	        
	       
		}
		catch(Exception e)
		{	
		
			System.out.println("Epidemic with ID " + epidemicID + " not found in database.");
			e.printStackTrace();
		}
	
       
	
        return rs;
       
	}
	
	public boolean isRunUnique(String epidemicID, String runID)
	{
		boolean exists = false;
		
		try
		{
			Connection c = connectToDatabase(epidemicID, "riaz", "Mordor");						
					
			PreparedStatement preparedStatement = (PreparedStatement) c.prepareStatement(checkRunExists);
			preparedStatement.setString(1, runID);
			ResultSet rs = preparedStatement.executeQuery();
			
			exists = rs.next();
		}
		catch (Exception e)
		{
			e.printStackTrace();
	
		}
		
		return !(exists);
	}
	
	//Adds a new MCMC run to the Runs table
	public boolean addToRunToTable(String epidemicID, String runID, String processID, float chainlength, String predictivePotential)
	{
		boolean completed = true;
		
		try
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String now = dateFormat.format(date);
			
			Connection c = connectToDatabase(epidemicID, "riaz", "Mordor");						
			PreparedStatement preparedStatement = (PreparedStatement) c.prepareStatement(insertRunsTableSQL);
			preparedStatement.setString(1, runID);	
			preparedStatement.setString(2,  processID);
			preparedStatement.setString(3,  now);
			preparedStatement.setFloat(4, chainlength);
			preparedStatement.setString(5, predictivePotential);
			preparedStatement .executeUpdate();
			preparedStatement.close();
			
		}
		catch (Exception e)
		{
			completed = false;
			System.out.println("New run NOT added to table.");
			e.printStackTrace();
		}
		
		return completed;
		
	}

	//Method that displays all from a table in a popup
	public void getAllFromTable(Connection conn, String tableA) throws SQLException
	{
		Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from " + tableA + ";");
        
        JTable table = new JTable(buildTableModel(rs));
        JOptionPane.showMessageDialog(null, new JScrollPane(table));
	}
	
	//Helper method to view content of a result set as a popup
	public static DefaultTableModel buildTableModel(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
	
	//Method for checking an run exists
	public boolean checkRunExists(String epidemicID, String runID)
	{
		boolean exists = false;
		
		try
		{
			Connection c = connectToDatabase(epidemicID, "riaz", "Mordor");						
					
			PreparedStatement preparedStatement = (PreparedStatement) c.prepareStatement(checkRunExists);
			preparedStatement.setString(1, runID);
			ResultSet rs = preparedStatement.executeQuery();
			
			exists = rs.next();
		}
		catch (Exception e)
		{
			e.printStackTrace();
	
		}
		
		return exists;
			
	}
	
	//  **************************************************************************
	// 	*** Methods for dealing with uploaded data (sequences, data for run)   ***
	//  **************************************************************************
	
	public static boolean addToSequenceTable(String epidemicID, String seqid, String seqdata, Date subdate, Date coldate, Date founddate, String loc, String descrip)
	{
		boolean completed = true;
		
		try
		{
			Connection c = connectToDatabase(epidemicID, "riaz", "Mordor");						
			PreparedStatement preparedStatement = (PreparedStatement) c.prepareStatement(insertSequencesTableSQL);
		
			preparedStatement.setString(1,  seqid);
			preparedStatement.setString(2,  seqdata);
			preparedStatement.setString(3,  "2013-11-11 12:12:22");
			preparedStatement.setString(4,  "2013-11-11 12:12:22");
			preparedStatement.setString(5,  "2013-11-11 12:12:22");
			preparedStatement.setString(6,  "2013-11-11 12:12:22");
			preparedStatement.setString(7,  "2013");
			//preparedStatement.setInt(4, chainlength);
			//preparedStatement.setString(5, "");
			//preparedStatement.setString(6, "");
			preparedStatement .executeUpdate();
			preparedStatement.close();
			
		}
		catch (Exception e)
		{
			completed = false;
			System.out.println("New sequence NOT added to table.");
			e.printStackTrace();
		}
		
		return completed;
		
	}
	
	//Parse .sequences file, check if seq IDs already exist in DB, if not add them.
	public static boolean checkSeqExists(String epidemicID, String seqData)
	{
	
		boolean exists = false;
		
		try
		{
			Connection c = connectToDatabase(epidemicID, "riaz", "Mordor");						
					
			PreparedStatement preparedStatement = (PreparedStatement) c.prepareStatement(checkSequenceExists);
			preparedStatement.setString(1, seqData);
			ResultSet rs = preparedStatement.executeQuery();
			
			exists = rs.next();
		}
		catch (Exception e)
		{
			e.printStackTrace();
	
		}
		
		return (exists);
	}
	
	//Check for sequences that haven't been run yet.
	public static void processUploadedFile(String epidemicID, String filenamePath)
	{
		
		
		File f = new File(filenamePath+"/sequences.fasta");
    	while(f.exists() == false) { 
    		
    		try {
        	    Thread.sleep(500);
        	} catch(InterruptedException ex) {
        	    Thread.currentThread().interrupt();
        	}

    	}
    	
    	try{
    		Thread.sleep(1000); //Wait for entire file to fill.
    	}
    	catch (Exception e)
    	{
    		
    	}
    	
    	System.out.println("** Adding uploaded sequences to database ***");
    	
		BufferedReader br = null;
        try {
        	 
			String sCurrentLine;
			
			br = new BufferedReader(new FileReader(filenamePath+"/sequences.fasta"));

			
			Map<String, String> fastaData = new HashMap<String, String>();
			
			String thisID = "";
			int count = 0;
					
			while ((sCurrentLine = br.readLine()) != null) {

		
				if (sCurrentLine.substring(0,1).equalsIgnoreCase(">"))
				{
					
					thisID = sCurrentLine;
					count+=1;
				}
				else
				{
					fastaData.put(thisID.substring(1), sCurrentLine);
				}
				
			}
			
			Iterator iterator =fastaData.keySet().iterator();  
			int countDuplicates = 0;
			
			while (iterator.hasNext()) {  
			   String key = iterator.next().toString();  
			   String value = fastaData.get(key).toString();  
  
			   if(DatabaseUtilities.checkSeqExists(epidemicID, key))
				{
				    countDuplicates +=1;
					//System.out.println("Sequences, " + key + " is already in the database");
				}
				else
				{
					DatabaseUtilities.addToSequenceTable(epidemicID, key, value, new Date(), new Date(), new Date(), "unknownloc", "unknowndes");

				}
			   
			}
			
			System.err.println(countDuplicates +" sequences out of " + count +" sequences have already been used in previous runs.");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static String[] calculateImportance(String pathToFile) throws IOException
	{	 	
			System.out.println("**** Calculating importance of uploaded data **** ");
			File f = new File(pathToFile+"/sequences.fasta");
	    	while(f.exists() == false) { 
	    		
	    		//System.out.println("Sequences file not found");
	    		try {
	        	    Thread.sleep(500); //need short pause TODO replace this with check file exists
	        	} catch(InterruptedException ex) {
	        	    Thread.currentThread().interrupt();
	        	}

	    	}
	    	
	    	try{
	    		Thread.sleep(1000); //Wait for entire file to fill.
	    	}
	    	catch (Exception e)
	    	{
	    		
	    	}
	    		
		 	String command = "./getSpread.sh " + pathToFile+"sequences.fasta";
		 	//System.out.println("===== Trying to calculate spread with command ======");
	        //System.out.println(command);
	        
			String[] commandA =  command.split(" "); //First, take the command passed in and split it up for processBuilder
	        ProcessBuilder probuilder = new ProcessBuilder( commandA ); //Construct a processBuilder with all arguments
	        //probuilder.directory(new File(runFrom)); //Change the working directory to the working directory of the currnetEpidemic and runID
	        Process process = probuilder.start(); //Start the run. All outputs will appear in the subdirectory!
	        
	        //Read errors
	        BufferedReader input = new BufferedReader(new InputStreamReader(process.getErrorStream()));
           
	        String output ="";
            String line=null;

            while((line=input.readLine()) != null) {
                //System.out.println(line);
                //output = output + "\n" + line;
                output = output + "\n" + line;
            }
         
            //Read output of program
            BufferedReader inputA = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
	        String outputA ="";
            String lineA=null;
            String[] info = new String[6];
       
            while((lineA=inputA.readLine()) != null) {
            
                outputA = outputA + ";" + lineA;
            }
      
            info = outputA.split(";");
            //System.out.println("Output of python process is:\n"+ outputA); 
            return info;
		
	}
	
	//  **************************************************************************
	// 	*** Methods for displaying runs and updating info					   ***
	//  **************************************************************************
	
	//Creates a d3 list of current run data for an epidemic which is then parsed by d3
	public String prepareDisplayRuns(String epidemicID)
	{
		
		System.out.println("*** Preparing D3 array for table display ***");
		String data = "[";
		//[{date: "2013-08-10T00:00:00",dt: "Aug 10 2013",id: "runOne", pot: "1",step: "50"},
         //{date: "2013-08-10T00:00:00",dt: "Aug 10 2013",id: "runTwo", pot: "2",step: "50"}];
		

		ResultSet rs = null;
		try 
		{
			rs = (ResultSet) getAllRuns(epidemicID);
			
		} catch (SQLException e) 
		{
			
			System.err.println("There are no runs to display.");
			e.printStackTrace();
			return "";
			
		}

		
		try 
		{
			int count = 0;
			while (rs.next())
			{	
				count+=1;
				String id = rs.getString("id");
				String processID = rs.getString("processid");
				String timeStarted = rs.getString("timestarted");
				int chainlength = rs.getInt("chainlength");
				String predictivePot = rs.getString("predictivepot"); //TODO
				float currentStep = -1;
				try
				{
					currentStep = LogUtilities.checkCurrentStep(epidemicID, id, chainlength);
				}
				catch(Exception e)
				{
					System.err.println("Error getting current step from log file for " + id);
				}
				
				double percentDone = ((currentStep/chainlength)*100)/100;
				String chainStatus = "";
				if (currentStep >= chainlength)
				{
					percentDone = 100;
				}
	
				DecimalFormat df2 = new DecimalFormat("###.##");
				percentDone = ((currentStep/chainlength)*100);
				percentDone = Double.valueOf(df2.format(percentDone));
				
				// Date date = new SimpleDateFormat("E, dd MM HH").parse(timeStarted);
		
				data = data + "{date: \""+timeStarted+"\",dt: \""+count+"\",id: \""+id+"\",pot: \""+predictivePot+"\",step: \""+percentDone+"\"},";		
				
			}
			rs.close();		
			//System.out.println("OUTPUT for CSV IS: " + data);
			data = data.substring(0, data.length()-1);
			data = data + "]";		
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("Error when reading result set of database query for runs.");
			
		}
		return data;
	
		
	}
	
	public String returnTab()
	{
		return " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	}
	
	//Get info for a certain run
	public static ResultSet getRun(String epidemicID, String runID)
		{
			ResultSet rs = null;
			
			try
			{
				Connection c = connectToDatabase(epidemicID, "riaz", "Mordor");						
						
				PreparedStatement preparedStatement = (PreparedStatement) c.prepareStatement(checkRunExists);
				preparedStatement.setString(1, runID);
				rs = preparedStatement.executeQuery();
				
				//exists = rs.next();
			}
			catch (Exception e)
			{
				e.printStackTrace();
		
			}
			
			return rs;
				
		}
	
	//Get time since
	public static long getDateDiff(Date date1, TimeUnit timeUnit) 
	{
	
	    long diffInMillies = new Date().getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	//When entering MCMCs, compare allSeqs to uploaded seqs
	public boolean checkForNewData(String epidemicID)
	{
		System.out.println("**** Checking for new data for this epidemic **** ");
		File f = new File(epidemicID+"/newSeqs.fasta");
    	if (f.exists() == false) { 
    		
    		System.err.println("newSeqs.fasta missing from epidemic root. Can't check for new sequences.");
    		return false;
    	}
    	
    	BufferedReader br = null;
        try {
        	 
			String sCurrentLine;
			
			br = new BufferedReader(new FileReader(epidemicID+"/newSeqs.fasta"));

			
			Map<String, String> fastaData = new HashMap<String, String>();
			
			String thisID = "";
			int count = 0;
					
			while ((sCurrentLine = br.readLine()) != null) {

		
				if (sCurrentLine.substring(0,1).equalsIgnoreCase(">"))
				{
					
					thisID = sCurrentLine;
					count+=1;
				}
				else
				{
					fastaData.put(thisID.substring(1), sCurrentLine);
				}
				
			}
			
			Iterator iterator =fastaData.keySet().iterator();  
			int countDuplicates = 0;
			
			Map<String, String> newSeqs = new HashMap<String, String>();
			
			while (iterator.hasNext()) {  
			   String key = iterator.next().toString();  
			   String value = fastaData.get(key).toString();  
  
			   if(DatabaseUtilities.checkSeqExists(epidemicID, key))
				{
				    countDuplicates +=1;
					//System.out.println("Sequences, " + key + " is already in the database");
				}
			   else
			   {
				   newSeqs.put(key, value);
			   }
			   
			}
			
			//System.err.println(countDuplicates +" sequences out of " + count +" sequences have already been used in previous runs.");
			
			if (countDuplicates != count)
			{
				System.err.println("There are " + (count=countDuplicates) + " new sequences that have been retrived!");
				return true;
			}
			else
			{
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
    	
    	
    	
	}

	

}
