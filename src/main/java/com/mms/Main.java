package com.mms;

import com.mms.db.DBManager;
import com.mms.dao.UserDAO;
import com.mms.dao.MovieDAO;
import com.mms.models.User;
import com.mms.models.Movie;
import com.mms.UI.LoginFrame_1;   
import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("MMS (Movie Management System) - Initialization");
        System.out.println("=".repeat(80));

        // 1. Clean the entire database (truncate all tables)
        cleanDatabase();
        
        // 2. Create admin user
        createAdminUser();
        
        // 3. Create regular user
        createRegularUser();
        
        // 4. Create one movie
        createMovie();

        // 5. Launch the UI
        System.out.println("\nLaunching UI...");
        SwingUtilities.invokeLater(() -> {
            new LoginFrame_1().setVisible(true);
        });
    }
    
    private static void cleanDatabase() {
        System.out.println("\n🧹 Cleaning all tables...");
        try (DBManager db = new DBManager();
            Connection conn = db.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("TRUNCATE TABLE seats, showtimes, movies, users RESTART IDENTITY CASCADE");
            System.out.println("All tables cleaned.");
        } catch (Exception e) {
            System.out.println("Failed to clean tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createAdminUser() {
        System.out.println("\nCreating admin user...");
        UserDAO userDAO = new UserDAO();
        User adminUser = new User("Admin", "admin@mms.com", "admin", "admin");
        boolean created = userDAO.createUser(adminUser);
        System.out.println(created ? "Admin user created successfully" : "Failed to create admin user");
    }
    
    private static void createRegularUser() {
        System.out.println("\nCreating regular user...");
        UserDAO userDAO = new UserDAO();
        User regularUser = new User("Protagonist", "tenet@gmail.com", "tenet", "user");
        boolean created = userDAO.createUser(regularUser);
        System.out.println(created ? "Regular user created successfully" : "Failed to create regular user");
    }
    
    private static void createMovie() {
        System.out.println("\nAdding movie...");
        MovieDAO movieDAO = new MovieDAO();
        Movie inception = new Movie("Inception", 148, "Sci-Fi", "English", "U/A", "inception.jpg");
        boolean created = movieDAO.createMovie(inception);
        System.out.println(created ? "Movie 'Inception' created successfully" : "Failed to create movie");
    }
}