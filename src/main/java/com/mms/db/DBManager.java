package com.mms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager implements AutoCloseable {
private final Connection conn;
public DBManager () throws SQLException{
 String url = "jdbc:postgresql://localhost:5432/moviedb";
 String user = "movieuser";
 String password = "moviepass";
 
 conn = DriverManager.getConnection(url, user,password);
 System.out.println("Connected to Postgre");
 }
 public Connection getConnection() {
 	return conn;
 }
 @Override
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("🔌 Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
