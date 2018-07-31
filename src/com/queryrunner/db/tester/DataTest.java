package com.queryrunner.db.tester;

import java.util.ArrayList;
import java.util.Date;

public class DataTest {
	private String id;
	private String query;
	private ArrayList<String> queryParams;
	ArrayList<String> headerKeys;//used for describing column names in DB and to understand result set size
	
	private Date executionDate;
	private long executionTimeInSec;
	private int status = -1;//negative by default, changed to 1 in case of success or according error code
	
	
	
	public DataTest(String id, String query, ArrayList<String> queryParams, ArrayList<String> headerKeys) {
		super();
		this.id = id;
		this.query = query;
		this.queryParams = queryParams;
		this.headerKeys = headerKeys;
	}
	
	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("ID : " + this.id + "\n");
		output.append("Query : " + this.query + "\n");
		if(this.queryParams != null){
			for(String param : this.queryParams){
				output.append("---param : " + param + "\n");
			}
		}else {
			output.append("Query params - empty\n");
		}
		
		if(this.headerKeys != null){
			output.append("Header ->  ");
			for(String headerKey : this.headerKeys){
				output.append(headerKey + "   |   ");
			}
		}else {
			output.append("[ERROR] : No header - Check config file for this JOB !!!!!");
		}
		
				
		return output.toString();
	}


	public Date getExecutionDate() {
		return executionDate;
	}


	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}


	public long getExecutionTimeInSec() {
		return executionTimeInSec;
	}


	public void setExecutionTimeInSec(long executionTimeInSec) {
		this.executionTimeInSec = executionTimeInSec;
	}


	public String getId() {
		return id;
	}


	public String getQuery() {
		return query;
	}


	public ArrayList<String> getQueryParams() {
		return queryParams;
	}


	public ArrayList<String> getHeaderKeys() {
		return headerKeys;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
}
