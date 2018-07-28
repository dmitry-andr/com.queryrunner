package com.queryrunner.db.processor;

import java.util.ArrayList;

public class SelectQueryOutput {
	private ArrayList<String> headerItems;
	private ArrayList<String> rows;//rows are comma separated values
	
	public SelectQueryOutput(ArrayList<String> headerItems) {
		super();
		this.headerItems = headerItems;
		rows = new ArrayList<String>();
	}
	
	
	
	public void printOutputToConsole(int limit) {
		System.out.println("\n>>>Select query output");
		System.out.println("Output records number : " + this.rows.size());
		System.out.println("Displaying first : " + limit);
		System.out.println("*********************************************");
		int rowsCounter = 1;
		for (String headerItem: this.getHeaderItems()) {
		    System.out.print("   " + headerItem + "   | ");
		}
		System.out.println();
		for (String row: this.getRows()) {
		    System.out.println(rowsCounter + " # " + row);
		    rowsCounter++;
		    if(rowsCounter > limit) {
		    	break;
		    }
		}
		
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
	
}
