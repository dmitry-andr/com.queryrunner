package com.queryrunner.db.processor;

import java.sql.Connection;
import java.sql.DriverManager;

import com.queryrunner.db.tester.utils.AppParams;
import com.queryrunner.db.tester.utils.Utils;

public class JDBCConnector {
	
	
	public static Connection getConnection() {
		Connection conn = null;
		try
	    {
	        Class.forName(Utils.getDBConnProps().get(AppParams.DB_CONNECTION_DRIVER_CLASS));
	        conn = DriverManager.getConnection(
	        		Utils.getDBConnProps().get(AppParams.DB_CONNECTION_URL_KEY),
	        		Utils.getDBConnProps().get(AppParams.DB_CONNECTION_USER_KEY),
	        		Utils.getDBConnProps().get(AppParams.DB_CONNECTION_PASSWORD_KEY));
	    }catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}

}
