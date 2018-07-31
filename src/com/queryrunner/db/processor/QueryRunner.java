package com.queryrunner.db.processor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.queryrunner.db.tester.DataTest;
import com.queryrunner.db.tester.utils.AppParams;

public class QueryRunner {
	
	public static DBQueryOutput executeQueryWithParams(DataTest dbJob) {
		
		DBQueryOutput result = null;
		
		dbJob.setExecutionDate(new Date());
		long startTimeInMillis = System.currentTimeMillis();
				
		Connection connection = JDBCConnector.getConnection();
		try{
			// Set prepared statement.
			PreparedStatement ps = connection.prepareStatement(dbJob.getQuery());
			if(dbJob.getQueryParams() != null) {
				for (int j = 0; j < dbJob.getQueryParams().size(); j++) {
					ps.setObject((j + 1), dbJob.getQueryParams().get(j));
				}
			}
					            
		 	// Execute SQL.
			ResultSet rs = ps.executeQuery();
			
			if(rs != null) {
				result = new DBQueryOutput(dbJob.getId(), dbJob.getHeaderKeys());
			}
						
			while (rs.next()) {
				StringBuilder stringSeparatedRow = new StringBuilder();
				for(int k = 0; k < dbJob.getHeaderKeys().size(); k++) {
					//commaSeparatedRow.append(rs.getString(resultSetHeaderItems.get(k)));this is version for getting value by column label
					stringSeparatedRow.append(rs.getString(k + 1));//getting value by column index
					if(k != (dbJob.getHeaderKeys().size() - 1)) {
						stringSeparatedRow.append(AppParams.QUERY_OUTPUT_ROW_VALUES_SEPARATOR);
					}
				}
				
				result.addRow(stringSeparatedRow.toString());
			}			
		}catch (Exception e) {
			dbJob.setStatus(-2);
			e.printStackTrace();
		}finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					dbJob.setStatus(-3);
					e.printStackTrace();
				}
			}
		}
		
		long endTimeInMillis = System.currentTimeMillis();
		dbJob.setExecutionTimeInSec((endTimeInMillis - startTimeInMillis)/1000);
		dbJob.setStatus(1);
		return result;
	}
}
