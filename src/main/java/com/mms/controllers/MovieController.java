
package com.mms.controllers;

import com.mms.dao.MovieDAO;
import com.mms.dao.ShowtimeDAO;
import com.mms.models.Movie;
import com.mms.models.Showtime;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;

public class MovieController extends BaseController {
    /**
     * Returns a map of Movie to its list of showtimes for a given date.
     * Only movies with at least one showtime on that date are included.
     */
    public Map<Movie, List<Showtime>> getMoviesWithShowtimesForDisplay(LocalDate date) {
        List<Showtime> showtimes = showtimeDAO.getShowtimesByDate(date);
        Map<Integer, List<Showtime>> movieIdToShowtimes = new HashMap<>();
        for (Showtime showtime : showtimes) {
            movieIdToShowtimes.computeIfAbsent(showtime.getMovieId(), k -> new ArrayList<>()).add(showtime);
        }
        Map<Movie, List<Showtime>> result = new LinkedHashMap<>();
        for (Integer movieId : movieIdToShowtimes.keySet()) {
            Movie movie = movieDAO.getMoviebyId(movieId);
            if (movie != null) {
                result.put(movie, movieIdToShowtimes.get(movieId));
            }
        }
        return result;
    }
    private MovieDAO movieDAO;
    private ShowtimeDAO showtimeDAO;

    public MovieController() {
        this.movieDAO = new MovieDAO();
        this.showtimeDAO = new ShowtimeDAO();
    }

    /**
     * Validates movie input fields
     */
    public boolean validateMovie(String title, String duration, String genre, String language, String certificate) {
        try {
            validateNotEmpty(title, "Title");
            validateNotEmpty(duration, "Duration");
            validateNotEmpty(genre, "Genre");
            validateNotEmpty(language, "Language");
            validateNotEmpty(certificate, "Certificate");
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Validates and converts duration string to integer
     */
    public int validateDuration(String duration) throws NumberFormatException {
        int dur = Integer.parseInt(duration);
        if (dur <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        return dur;
    }

    /**
     * Handles poster image upload
     */
    public String uploadPosterImage(File selectedFile) throws IOException {
        validateNotNull(selectedFile, "Selected file");
        // Create images directory if it doesn't exist
        File imagesDir = new File("./images");
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }
        String fileName = selectedFile.getName();
        File dest = new File("./images/" + fileName);
        Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return "images/" + fileName;
    }

    /**
     * Creates a new movie
     */
    public Movie createMovie(String title, int duration, String genre, 
                           String language, String certificate, String posterUrl) {
        Movie newMovie = new Movie(title, duration, genre, language, certificate, posterUrl);
        movieDAO.createMovie(newMovie);
        return newMovie;
    }

    /**
     * Updates an existing movie
     */
    public Movie updateMovie(Movie movie) {
        validateNotNull(movie, "Movie");
        movieDAO.updateMovie(movie);
        return movie;
    }

    /**
     * Gets all movies
     */
    public List<Movie> getAllMovies() {
        return movieDAO.getAllMovies();
    }

    /**
     * Gets a movie by ID
     */
    public Movie getMovieById(int movieId) {
        if (!isValidId(movieId)) {
            throw new IllegalArgumentException("Invalid movie ID");
        }
        return movieDAO.getMoviebyId(movieId);
    }

    /**
     * Gets movies with showtimes for a specific date
     */
    public Set<Integer> getMoviesWithShowtimesForDate(LocalDate date) {
        List<Showtime> showtimes = showtimeDAO.getShowtimesByDate(date);
        Set<Integer> movieIds = new HashSet<>();
        for (Showtime showtime : showtimes) {
            movieIds.add(showtime.getMovieId());
        }
        return movieIds;
    }

    /**
     * Gets showtimes for a specific date
     */
    public List<Showtime> getShowtimesForDate(LocalDate date) {
        return showtimeDAO.getShowtimesByDate(date);
    }

    /**
     * Deletes a movie (with business rule validation)
     */
    public void deleteMovie(int movieId) throws MovieOperationException {
        // Check if movie has showtimes before deletion
        if (hasShowtimes(movieId)) {
            throw new MovieOperationException("Cannot delete movie with existing showtimes");
        }
        movieDAO.deleteMovie(movieId);
    }

    /**
     * Checks if a movie has associated showtimes
     */
    private boolean hasShowtimes(int movieId) {
        // This would need to be implemented in ShowtimeDAO if not already present
        // For now, we'll assume it returns true if there are any showtimes for this movie
        List<Showtime> showtimes = showtimeDAO.getShowtimesByMovieId(movieId);
        return showtimes != null && !showtimes.isEmpty();
    }

    /**
     * Custom exception for movie operations
     */
    public static class MovieOperationException extends Exception {
        public MovieOperationException(String message) {
            super(message);
        }
    }
}
