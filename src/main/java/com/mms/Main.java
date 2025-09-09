package com.mms;

import com.mms.db.DBManager;
import com.mms.dao.UserDAO;
import com.mms.dao.MovieDAO;
import com.mms.models.User;
import com.mms.models.Movie;
import com.mms.UI.LoginFrame_1;   
import javax.swing.SwingUtilities;



import java.util.List;

public class Main {
    public static void main(String[] args) {

        //   Basic DB connection test
        try (DBManager db = new DBManager()) {
            System.out.println("  DB connection works");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Depopulate the tables for clean testing
        DBManager.clearTables();

        //   Test UserDAO (user)
        UserDAO userDAO = new UserDAO();
        User newUser = new User("Protagonist", "tenet@gmail.com", "tenet", "user");
        boolean userCreated = userDAO.createUser(newUser);
        System.out.println(userCreated ? "  User created" : "  User creation failed");

        // Test UserDAO (admin)
        User adminUser = new User("Admin", "admin@gmail.com", "admin", "admin");
        boolean adminCreated = userDAO.createUser(adminUser);
        System.out.println(adminCreated ? "  Admin user created" : "  Admin user creation failed");

        //   Now test MovieDAO
        MovieDAO movieDAO = new MovieDAO();

        // 1. Create Movie
        Movie inception = new Movie("Inception", 148, "Sci-Fi", "English", "U/A", "Inception.jpg");
        boolean created = movieDAO.createMovie(inception);
        System.out.println(created ? "  Movie created" : "  Movie creation failed");

        // 2. Get Movie by ID (assuming ID = 1 for test)
        Movie fetched = movieDAO.getMoviebyId(1);
        if (fetched != null) {
            System.out.println("🎬 Fetched Movie: " + fetched);
        } else {
            System.out.println("  No movie found with ID 1");
        }

        // 3. Get All Movies
        List<Movie> movies = movieDAO.getAllMovies();
        System.out.println("📽️ All Movies:");
        for (Movie m : movies) {
            System.out.println(" - " + m);
        }

        // 4. Update Movie
        if (fetched != null) {
            fetched.setDuration(150);
            fetched.setGenre("Thriller");
            fetched.setPosterUrl("https://poster.com/inception_updated.jpg");

            boolean updated = movieDAO.updateMovie(fetched);
            System.out.println(updated ? "  Movie updated" : "  Movie update failed");
        }

        // 5. Search Movies (by title substring)
        List<Movie> searchResults = movieDAO.searchMovies("Incep", null, null, null);
        System.out.println("🔍 Search Results for 'Incep':");
        for (Movie m : searchResults) {
            System.out.println(" - " + m);
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame_1().setVisible(true);
        });
       }
}
