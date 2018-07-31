package com.queryrunner.db.tester.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.queryrunner.db.processor.DBQueryOutput;
import com.queryrunner.db.tester.DataTest;

public class Utils {
	
	private static Map<String, String> dbConnectionProperties = null;
	
	public static Map<String, String> getDBConnProps(){
		if(dbConnectionProperties != null) {
			return dbConnectionProperties;
		}else {
			dbConnectionProperties = readDBConnectionProperties();
			return dbConnectionProperties;
		}
	}
	
	public static Map<String, String> readDBConnectionProperties() {
		
		Map<String, String> properties = new HashMap<String, String>();
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(AppParams.DB_CONNECTION_CONFIG_FILE);

			// load a properties file
			prop.load(input);

			properties.put(AppParams.DB_CONNECTION_URL_KEY, prop.getProperty(AppParams.DB_CONNECTION_URL_KEY));
			properties.put(AppParams.DB_CONNECTION_DRIVER_CLASS, prop.getProperty(AppParams.DB_CONNECTION_DRIVER_CLASS));
			properties.put(AppParams.DB_CONNECTION_USER_KEY, prop.getProperty(AppParams.DB_CONNECTION_USER_KEY));
			properties.put(AppParams.DB_CONNECTION_PASSWORD_KEY, prop.getProperty(AppParams.DB_CONNECTION_PASSWORD_KEY));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return properties;
	}
	
	public static String[] filesInFolder(String folderNameRelativeToAppRoot) {
		String[] filesInTest = null;
		
		// create a file that is really a directory
	    File testsDirectory = new File(folderNameRelativeToAppRoot);

	    // get a listing of all files in the directory
	    String[] filesInDir = testsDirectory.list();

	    // sort the list of files (optional)
	    // Arrays.sort(filesInDir);

	    if(filesInDir != null) {
	    	filesInTest = new String[filesInDir.length];
	    	for ( int i=0; i<filesInDir.length; i++ )
		    {
		      System.out.println( "Filename added : " + filesInDir[i] );
		      filesInTest[i] = filesInDir[i];
		    }
	    	
	    }else {
	    	System.out.println( "[ERROR!!!] : \"" + folderNameRelativeToAppRoot +"\" directory is empty or doesn't exsist!!!!!");
	    }

		return filesInTest;
	}
	
	//jobId is filename in db_jobs directory
	public static DataTest readDBJob(String jobFielename) {
		
		DataTest job = null;
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(AppParams.DB_JOBS_DIRECTORY + "/" + jobFielename);

			// load a properties file
			prop.load(input);
			
			String jobId = prop.getProperty(AppParams.DB_JOB_ID);
			String jobQuery = prop.getProperty(AppParams.DB_JOB_QUERY);
			String jobConfigurationQueryParams = prop.getProperty(AppParams.DB_JOB_QUERY_PARAMS);
			ArrayList<String> jobQueryParams = null;
			if((jobConfigurationQueryParams != null) && (jobConfigurationQueryParams.length() > 0)) {
				jobQueryParams = new ArrayList<String>();
				String[] queryParams = jobConfigurationQueryParams.split(",");
				for(String queryParam : queryParams) {
					jobQueryParams.add(queryParam.trim());
				}
			}
			
			String jobConfigurationOutputHeader = prop.getProperty(AppParams.DB_JOB_OUTPUT_HEADER);
			ArrayList<String> jobHeaderItems = null;
			if((jobConfigurationOutputHeader != null) && (jobConfigurationOutputHeader.length() > 0)) {
				jobHeaderItems = new ArrayList<String>();
				String[] headerItems = jobConfigurationOutputHeader.split(",");
				for(String headerItem : headerItems) {
					jobHeaderItems.add(headerItem.trim());
				}
			}
			
			job = new DataTest(jobId, jobQuery, jobQueryParams, jobHeaderItems);
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return job;
	}
	
	public static int writeOutputToCSV(DBQueryOutput output) {
		
		//Checking if output directory exists
		File file = new File(AppParams.DB_JOB_CSV_OUTPUT_DIRECTORY);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println(AppParams.DB_JOB_CSV_OUTPUT_DIRECTORY + " - directory is created!");
            } else {
                System.out.println("[ERROR !!!] : Failed to create directory! - " + AppParams.DB_JOB_CSV_OUTPUT_DIRECTORY);
                return -1;
            }
        }
        
        PrintWriter pw;
		try {
			pw = new PrintWriter(new File(AppParams.DB_JOB_CSV_OUTPUT_DIRECTORY + "/" + output.getJobId() + ".csv"));
	        StringBuilder sb = new StringBuilder();
	        //CSV header
	        for(int k = 0; k < output.getHeaderItems().size(); k++) {
	        	sb.append(output.getHeaderItems().get(k));
	        	if(k < (output.getHeaderItems().size() - 1)) {
	        		sb.append(',');
	        	}
	        }
	        sb.append('\n');
	        
	        //Values
	        for(int i = 0; i < output.getRows().size(); i++) {
	        	String[] rowItems = output.getRows().get(i).split(AppParams.QUERY_OUTPUT_ROW_VALUES_SEPARATOR);
	        	for(int j = 0; j < rowItems.length; j++) {
	        		rowItems[j] = rowItems[j].replace(",", AppParams.QUERY_OUTPUT_ROW_COMMA_SYMBOL_IN_TEXT_REPLACEMENT);//replacing possible commas in text to ensure CSV file consistency
	        		sb.append(rowItems[j]);
	        		
	        		if(j < (rowItems.length - 1)) {
		        		sb.append(',');
	        		}
	        	}
	        	
	        	if(i < (output.getRows().size() - 1)) {
	        		sb.append('\n');
	        	}
	        	
	        }
	        
	        pw.write(sb.toString());
	        
	        pw.close();        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
		
		return 1;
	}
}
