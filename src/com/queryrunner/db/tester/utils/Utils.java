package com.queryrunner.db.tester.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.queryrunner.db.processor.DBQueryOutput;
import com.queryrunner.db.tester.DataTest;

public class Utils {
	
	private static String timestampForOutputArtifacts = null;
	
	private static Map<String, String> dbConnectionProperties = null;
	
	private static String getTimestamp() {
		if(timestampForOutputArtifacts != null) {
			return timestampForOutputArtifacts;
		}else {
			timestampForOutputArtifacts = (new SimpleDateFormat(AppParams.DB_JOB_EXECUTION_DATE_FORMAT).format(new Date()));
			return timestampForOutputArtifacts;
		}
	}
	
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
		String outputDirectory = AppParams.DB_JOB_CSV_OUTPUT_DIRECTORY  + "\\" + getTimestamp() + "_csv_files";
		File file = new File(outputDirectory);
        if (!file.exists()) {
            if (file.mkdirs()) {
                System.out.println(outputDirectory + " - directory is created!");
            } else {
                System.out.println("[ERROR !!!] : Failed to create directory! - " + outputDirectory);
                return -1;
            }
        }
        
        PrintWriter csvFileWriter;
		try {
			csvFileWriter = new PrintWriter(new File(outputDirectory + "/" + output.getJobId() + ".csv"));
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
	        
	        csvFileWriter.write(sb.toString());
	        
	        csvFileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
		return 1;
	}
	
	public static int updateSuiteExecutionCSVReport(DataTest job) {
		int status = 1;
		String fileNameWithPath = AppParams.DB_JOB_CSV_OUTPUT_DIRECTORY  + "\\" + getTimestamp() + "_" + AppParams.EXECUTION_REPORT_CSV_FILE_NAME;
		File csvExecutionReport = new File(fileNameWithPath);
		  if(csvExecutionReport.exists()){
			  try {
				FileWriter pw = new FileWriter(fileNameWithPath, true);
				StringBuilder sb = new StringBuilder();
				
				//add row with data
				sb.append(buildExecutionReportDataRow(job));
				
				pw.write(sb.toString());
		        
				pw.close();				
			} catch (IOException e) {
				status = -1;
				e.printStackTrace();
			}
		  }else{
			  try {
				PrintWriter csvWriter = new PrintWriter(new File(fileNameWithPath));
				StringBuilder sb = new StringBuilder();
				//adding header
				String[] columns = AppParams.EXECUTION_REPORT_CSV_FILE_COLUMNS.split(",");
				for(int k = 0; k < columns.length; k++) {
					sb.append(columns[k]);
					if(k < (columns.length - 1)) {
						sb.append(",");
					}
				}
				//add row with data
				sb.append(buildExecutionReportDataRow(job));
				
				csvWriter.write(sb.toString());
		        
				csvWriter.close();				
			} catch (FileNotFoundException e) {
				status = -2;
				e.printStackTrace();
			}
		  }
		
		return status;
	}
	
	private static String buildExecutionReportDataRow(DataTest job) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("");//"No" counter not implemented
		sb.append(",");
		sb.append(job.getId());
		sb.append(",");
		
		//SQL prams
		if(job.getQueryParams() != null) {
			for(int i = 0; i < job.getQueryParams().size(); i++) {
				sb.append(job.getQueryParams().get(i));
				if(i < (job.getQueryParams().size() - 1)) {
					sb.append(AppParams.EXECUTION_REPORT_CSV_SQL_PRAMS_VALUES_SPERATOR);
				}
			}
		}else {
			//adding empty cell as no SQL parameters defined
			sb.append("");
		}
		sb.append(",");
		
		sb.append(job.getExecutionTimeInSec());
		sb.append(",");
		sb.append(job.getStatus());
		sb.append(",");
		//result output dimensions in format "columns x rows"
		sb.append(job.getHeaderKeys().size());
		sb.append(" x ");
		sb.append(job.getRowsReturnedCount());
		sb.append(",");
		
		sb.append(job.getErrorShortText().replace(",", AppParams.QUERY_OUTPUT_ROW_COMMA_SYMBOL_IN_TEXT_REPLACEMENT));
		
		return sb.toString();
	}
	
}
