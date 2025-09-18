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

    public static void clearAndResetTables() {
        try (DBManager db = new DBManager()) {
            Connection conn = db.getConnection();
            var rs = conn.createStatement().executeQuery(
                "SELECT tablename FROM pg_tables WHERE schemaname = 'public'"
            );
        while (rs.next()) {
            String table = rs.getString("tablename");
            // Clear table
            try {
            String truncateSql = "TRUNCATE TABLE " + table + " RESTART IDENTITY CASCADE";
            conn.createStatement().executeUpdate(truncateSql);
            System.out.println("Truncated table: " + table);
            } catch (SQLException ex) {
                System.out.println("Could not truncate table: " + table + " (" + ex.getMessage() + ")");
            }

            // Reset serial/identity columns (if any)
            // Try to find the primary key column and reset its sequence
            var pkRs = conn.createStatement().executeQuery(
                "SELECT a.attname, s.relname " +
                "FROM pg_class t " +
                "JOIN pg_attribute a ON a.attrelid = t.oid " +
                "JOIN pg_index i ON i.indrelid = t.oid AND a.attnum = ANY(i.indkey) " +
                "JOIN pg_class s ON s.oid = t.oid " +
                "WHERE t.relname = '" + table + "' AND i.indisprimary"
            );
            if (pkRs.next()) {
                String pk = pkRs.getString("attname");
                // Try to reset sequence (works for serial columns)
                String seqName = table + "_" + pk + "_seq";
                try {
                    String resetSql = "ALTER SEQUENCE " + seqName + " RESTART WITH 1";
                    conn.createStatement().executeUpdate(resetSql);
                    System.out.println("Reset sequence: " + seqName);
                } catch (SQLException seqEx) {
                    // Sequence may not exist, ignore
                }
            }
            pkRs.close();
        }
        rs.close();
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
