package com.queryrunner.db.tester.utils;

public class AppParams {
	public static final String DB_CONNECTION_CONFIG_FILE = "dbconnection.properties";
	public static final String DB_CONNECTION_DRIVER_CLASS = "driver_class";
	public static final String DB_CONNECTION_URL_KEY = "db_url";
	public static final String DB_CONNECTION_USER_KEY = "db_username";
	public static final String DB_CONNECTION_PASSWORD_KEY = "db_user_password";
	
	
	public static final String DB_JOBS_DIRECTORY = "db_jobs";
	public static final String DB_JOB_ID = "db_job_id";
	public static final String DB_JOB_QUERY = "db_job_query";
	public static final String DB_JOB_QUERY_PARAMS = "db_job_query_params";
	public static final String DB_JOB_OUTPUT_HEADER = "db_job_output_header";
	public static final String DB_JOB_CSV_OUTPUT_DIRECTORY = "csv_output";
	public static final String DB_JOB_EXECUTION_DATE_FORMAT = "yyyy_MM_dd_'at'_HH_mm_ss";
	
	
	public static final String QUERY_OUTPUT_ROW_VALUES_SEPARATOR = "%sep%";
	public static final String QUERY_OUTPUT_ROW_COMMA_SYMBOL_IN_TEXT_REPLACEMENT = "&commaSymbol&";
	
	
	
	public static final String EXECUTION_REPORT_CSV_FILE_NAME = "suite_execution_report.csv";
	public static final String EXECUTION_REPORT_CSV_FILE_COLUMNS = "No,JobID,SQL Prams Values(| separated),Execution Time(sec),Status,Result size(cols x rows), Error msgs(if any)";
	public static final String EXECUTION_REPORT_CSV_SQL_PRAMS_VALUES_SPERATOR = "|";
	
	
	
	
	
	
	
	
	

}
