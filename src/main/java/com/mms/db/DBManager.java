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

    public static void clearTables() {
        String[] tables = {"users", "movies"};
        try (DBManager db = new DBManager()) {
            Connection conn = db.getConnection();
            for (String table : tables) {
                String sql = "DELETE FROM " + table;
                conn.createStatement().executeUpdate(sql);
                System.out.println("Cleared table: " + table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
