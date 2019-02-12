package com.revature.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;



public class ConnectionUtil {
	
	private static Connection instance = null;
	//logger
	
	private ConnectionUtil() {}
	
	public static Connection getConnection() {
		if (ConnectionUtil.instance != null) {
			return instance;
		}
		
		InputStream in = null;
		
		try {
			Properties props = new Properties();
			in = new FileInputStream("C:\\Users\\Storm\\Documents\\Revature\\bank\\project-zero-jshoberg\\JDBCBank\\JDBCBank\\src\\main\\resources\\connection.properties");
			props.load(in);
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = null;	
			
			String endpoint = props.getProperty("jdbc.url");
			String username = props.getProperty("jdbc.username");
			String password = props.getProperty("jdbc.password");
			
			con = DriverManager.getConnection(endpoint, username, password);
			instance = con;
			return instance;
		} catch (Exception e) {
			//error
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				
			}
		}
		return null;
	}
}
