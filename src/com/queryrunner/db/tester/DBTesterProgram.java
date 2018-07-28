package com.queryrunner.db.tester;

import java.util.ArrayList;
import java.util.Arrays;
import com.queryrunner.db.processor.QueryRunner;
import com.queryrunner.db.processor.SelectQueryOutput;
import com.queryrunner.db.tester.utils.Utils;

public class DBTesterProgram {

	public static void main(String[] args) {

		System.out.println("Main program launched");
		
		
		SelectQueryOutput selectOutput = null;
		
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
		
		String[] jobFilesInSuite = Utils.filesInDBJobsSuite();
				
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
			System.out.println("Job Settings\n");
			System.out.println(jobs[j].toString());
			System.out.println("Job Run\n");
			
			
			
			selectOutput = QueryRunner.executeSelectQueryWithParams(jobs[j].getQuery(), jobs[j].getQueryParams(), jobs[j].getHeaderKeys());
			selectOutput.printOutputToConsole(10);
			
			
			
			
			System.out.println(">>>END Job#" + (j +1) + "\n");
		}
		
		
		
		
		System.out.println("***Completed***");
	}

}
