package com.queryrunner.db.tester.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
	
	public static String[] filesInDBJobsSuite() {
		String[] filesInTest = null;
		
		// create a file that is really a directory
	    File testsDirectory = new File(AppParams.DB_JOBS_DIRECTORY);

	    // get a listing of all files in the directory
	    String[] filesInDir = testsDirectory.list();

	    // sort the list of files (optional)
	    // Arrays.sort(filesInDir);

	    if(filesInDir != null) {
	    	filesInTest = new String[filesInDir.length];
	    	for ( int i=0; i<filesInDir.length; i++ )
		    {
		      System.out.println( "Test scenario added : " + filesInDir[i] );
		      filesInTest[i] = filesInDir[i];
		    }
	    	
	    }else {
	    	System.out.println( "[ERROR] : \"" + AppParams.DB_JOBS_DIRECTORY +"\" directory is empty or doesn't exsist!!!!!");
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
}