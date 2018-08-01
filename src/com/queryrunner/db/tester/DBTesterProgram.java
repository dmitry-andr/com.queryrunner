package com.queryrunner.db.tester;

import com.queryrunner.db.processor.QueryRunner;

import java.text.SimpleDateFormat;

import com.queryrunner.db.processor.DBQueryOutput;
import com.queryrunner.db.tester.utils.AppParams;
import com.queryrunner.db.tester.utils.Utils;

public class DBTesterProgram {

	public static void main(String[] args) {

		System.out.println("Main program launched");
		
		
		
		DBQueryOutput selectOutput = null;
		
/*
		String queryWithParam = "SELECT * FROM sakila.city WHERE sakila.city.city = ?";
		ArrayList<String> queryParams = new ArrayList<String>(Arrays.asList("Aden"));
		ArrayList<String> headerKeys = new ArrayList<String>(Arrays.asList("city_id", "city", "country_id", "last_update"));
		selectOutput = QueryRunner.executeSelectQueryWithParams(queryWithParam, queryParams, headerKeys);
		selectOutput.printOutputToConsole(10);
*/
		
		System.out.println("********************************************************\n");
		System.out.println("Reading DB jobs");
		DataTest[] jobs = null;
		
		String[] jobFilesInSuite = Utils.filesInFolder(AppParams.DB_JOBS_DIRECTORY);
				
		if(jobFilesInSuite.length >= 1) {
			jobs = new DataTest[jobFilesInSuite.length];
			System.out.println("Number of files in suite : " + jobFilesInSuite.length);
			for(int k = 0; k < jobFilesInSuite.length; k++) {
				System.out.println(jobFilesInSuite[k]);
				jobs[k] = Utils.readDBJob(jobFilesInSuite[k]);
			}
			
		}else {
			System.out.println("[ERROR] : NO files in suite!!!!!");
		}
		
		System.out.println("Starting DB jobs execution");
		for(int j = 0; j < jobs.length; j++) {
			System.out.println(">>>START Job#" + (j +1));
			System.out.println("Job Settings");
			System.out.println(jobs[j].toString());
			System.out.println("Job Run");
			
			
			
			selectOutput = QueryRunner.executeQueryWithParams(jobs[j]);			
			selectOutput.printOutputToConsole(10);

			System.out.println("Job executed on :" + (new SimpleDateFormat(AppParams.DB_JOB_EXECUTION_DATE_FORMAT).format(jobs[j].getExecutionDate())) + 
					" ; Execution time in Sec : " + jobs[j].getExecutionTimeInSec() + "\n");
			System.out.println("Writing Job Data output to CSV, Status : " + Utils.writeOutputToCSV(selectOutput) + "\n");
			System.out.println("Job execution status wirting to CSV, Status : " + Utils.updateSuiteExecutionCSVReport(jobs[j]) + "\n");
			
			
			
			
			System.out.println(">>>END Job#" + (j +1) + "\n");
		}
		
		
		
		
		System.out.println("***Completed***");
	}

}
