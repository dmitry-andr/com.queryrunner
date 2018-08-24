package com.queryrunner.db.processor;

import java.util.ArrayList;

import com.queryrunner.db.tester.utils.AppParams;

public class DBQueryOutput {
	private String jobId;
	private ArrayList<String> headerItems;
	private ArrayList<String> rows;//rows are strings with values separated by symbol defined by App constant QUERY_OUTPUT_ROW_VALUES_SEPARATOR
	
	public DBQueryOutput(String jobId, ArrayList<String> headerItems) {
		super();
		this.jobId = jobId;
		this.headerItems = headerItems;
		rows = new ArrayList<String>();
	}
	
	@SuppressWarnings("unused")
	private DBQueryOutput() {
		super();
	}
	
	
	public void printOutputToConsole(int limit) {
		System.out.println("\n>>>DB query output");
		System.out.println("Output records number : " + this.rows.size());
		System.out.println("Displaying first : " + limit);
		System.out.println("*********************************************");
		int rowsCounter = 1;
		for (String headerItem: this.getHeaderItems()) {
		    System.out.print("   " + headerItem + "   | ");
		}
		System.out.println();
		for (String row: this.getRows()) {
			String[] rowItems = row.split(AppParams.QUERY_OUTPUT_ROW_VALUES_SEPARATOR);
		    System.out.print(rowsCounter + " # ");
		    for(String rowItem : rowItems) {
		    	System.out.print(rowItem + " | ");
		    }
		    System.out.println("");
		    rowsCounter++;
		    if(rowsCounter > limit) {
		    	break;
		    }
		}
		System.out.println("*********************************************");
	}
	
	public void addRow(String commaSepratedRowValues) {
		this.rows.add(commaSepratedRowValues);		
	}

	public ArrayList<String> getHeaderItems() {
		return headerItems;
	}
	public ArrayList<String> getRows() {
		return rows;
	}

	public String getJobId() {
		return jobId;
	}
}
