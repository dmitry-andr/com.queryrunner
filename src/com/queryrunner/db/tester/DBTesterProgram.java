package com.queryrunner.db.tester;

import com.queryrunner.db.processor.QueryRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import com.queryrunner.db.processor.DBQueryOutput;
import com.queryrunner.db.tester.utils.AppParams;
import com.queryrunner.db.tester.utils.Utils;

public class DBTesterProgram {

	public static void main(String[] args) {

		System.out.println("Data extraction utility launched - " + AppParams.APP_VERSION);
		System.out.println("********************************************************\n");
		
		
		DBQueryOutput dqQueryRunOutput = null;
				
		
		System.out.println("Reading DB jobs");
		DataTest[] jobs = null;
		
		String[] jobFilesInSuite = Utils.filesInFolder(AppParams.DB_JOBS_DIRECTORY);
				
		if(jobFilesInSuite.length >= 1) {
			jobs = new DataTest[jobFilesInSuite.length];
			System.out.println("Number of files in suite : " + jobFilesInSuite.length);
			for(int k = 0; k < jobFilesInSuite.length; k++) {
				System.out.println(jobFilesInSuite[k]);
				jobs[k] = Utils.readDBJob(jobFilesInSuite[k]);
				
				int countDynamicParamsInQuery = Utils.countSubstring("?", jobs[k].getQuery());
				if((jobs[k].getQueryParams() != null) && (countDynamicParamsInQuery != jobs[k].getQueryParams().size())) {
					System.out.println("[WARNING !!!] : Number of paramaters in Query and in Prams list doesn't match in Job -" + jobs[k].getId());
					//Ask user if to proceed
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
						System.out.print("Are you sure to proceed? (yes/no) : ");
		                String answer = br.readLine();
		                if ("no".equalsIgnoreCase(answer)) {
		                    System.out.println("Run terminated - Fix configurations and run again :)");
		                    System.exit(0);
		                }
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}else {
			System.out.println("[ERROR] : NO files in suite!!!!!");
		}
		
		System.out.println("Starting DB jobs execution");
		for(int j = 0; j < jobs.length; j++) {
			System.out.println(">>>START Job#" + (j +1) + " (of " + jobFilesInSuite.length + ")");
			System.out.println("Job Settings");
			System.out.println(jobs[j].toString());
			System.out.println("Job Run");
			
			dqQueryRunOutput = QueryRunner.executeQueryWithParams(jobs[j]);
			if(dqQueryRunOutput !=  null) {
				dqQueryRunOutput.printOutputToConsole(10);
				jobs[j].setRowsReturnedCount(dqQueryRunOutput.getRows().size());
				System.out.println("Writing Job Data output to CSV, Status : " + Utils.writeOutputToCSV(dqQueryRunOutput) + "\n");
			}
			
			System.out.println("Job executed on :" + (new SimpleDateFormat(AppParams.DB_JOB_EXECUTION_DATE_FORMAT).format(jobs[j].getExecutionDate())) + 
					" ; Execution time in Sec : " + jobs[j].getExecutionTimeInSec() + "\n");			
			System.out.println("Job execution status wirting to CSV, Status : " + Utils.updateSuiteExecutionCSVReport(jobs[j]) + "\n");
			
			
			System.out.println(">>>END Job#" + (j +1) + "\n");
		}
		
		
		
		System.out.println("***Completed***");
	}
	
	

}
