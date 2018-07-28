package com.queryrunner.db.processor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class QueryRunner {
	
	public static SelectQueryOutput executeSelectQueryWithParams(String query, ArrayList<String> queryParams, ArrayList<String> resultSetHeaderItems) {
		
		SelectQueryOutput result = null;
		
		
		
		try(Connection connection = JDBCConnector.getConnection()) {
			// Set prepared statement.
			PreparedStatement ps = connection.prepareStatement(query);
			if(queryParams != null) {
				for (int j = 0; j < queryParams.size(); j++) {
					ps.setObject((j + 1), queryParams.get(j));
				}
			}
			
		            
		 	// Execute SQL.
			ResultSet rs = ps.executeQuery();
			
			if(rs != null) {
				result = new SelectQueryOutput(resultSetHeaderItems);
			}
			
			
			while (rs.next()) {
				StringBuilder commaSeparatedRow = new StringBuilder();
				for(int k = 0; k < resultSetHeaderItems.size(); k++) {
					//commaSeparatedRow.append(rs.getString(resultSetHeaderItems.get(k)));this is version for getting value by column label
					commaSeparatedRow.append(rs.getString(k + 1));//getting value by column index
					if(k != (resultSetHeaderItems.size() - 1)) {
						commaSeparatedRow.append(",");
					}
				}
				
				result.addRow(commaSeparatedRow.toString());
			}


			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
