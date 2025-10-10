package com.mms.controllers;

import com.mms.dao.MovieDAO;
import com.mms.dao.ShowtimeDAO;
import com.mms.models.Movie;
import com.mms.models.Showtime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class ShowtimeController extends BaseController {
	private final MovieDAO movieDAO;
	private final ShowtimeDAO showtimeDAO;

	public ShowtimeController() {
		this.movieDAO = new MovieDAO();
		this.showtimeDAO = new ShowtimeDAO();
	}

	public List<Movie> getAllMovies() {
		return movieDAO.getAllMovies();
	}
	/**
	 * Validates showtime input fields
	 * @param selectedMovie The selected movie object.
	 * @param startDate The start date for the showtimes.
	 * @param endDate The end date for the showtimes.
	 * @param numShows The number of shows to be created.
	 * @param hours List of hour strings for each show.
	 * @param minutes List of minute strings for each show.
	 * @param amPm List of AM/PM strings for each show.
	 * @return true if all fields are valid, false otherwise.
	 */
	public boolean validateShowtimeFields(Movie selectedMovie, LocalDate startDate, LocalDate endDate, int numShows, List<String> hours, List<String> minutes, List<String> amPm) {
		if (selectedMovie == null || startDate == null || endDate == null || numShows < 1) return false;
		if (hours.size() != numShows || minutes.size() != numShows || amPm.size() != numShows) return false;
		for (int i = 0; i < numShows; i++) {
			if (hours.get(i).isEmpty() || minutes.get(i).isEmpty() || amPm.get(i) == null) return false;
		}
		return true;
	}
	/**
	 * Parses show times from input lists and returns a list of LocalTime objects.
	 * @param numShows The number of shows.
	 * @param hours List of hour strings for each show.
	 * @param minutes List of minute strings for each show.
	 * @param amPm List of AM/PM strings for each show.
	 * @return A list of LocalTime objects representing the show times.
	 * @throws IllegalArgumentException if any time format is invalid.
	 */
	public List<LocalTime> parseShowTimes(int numShows, List<String> hours, List<String> minutes, List<String> amPm) throws IllegalArgumentException {
		List<LocalTime> showTimes = new ArrayList<>();
		for (int i = 0; i < numShows; i++) {
			int hrs = Integer.parseInt(hours.get(i));
			int mins = Integer.parseInt(minutes.get(i));
			String ampm = amPm.get(i);
			if (hrs < 1 || hrs > 12 || mins < 0 || mins > 59) {
				throw new IllegalArgumentException("Invalid time format for show " + (i+1));
			}
			if (ampm.equals("PM") && hrs != 12) hrs += 12;
			if (ampm.equals("AM") && hrs == 12) hrs = 0;
			showTimes.add(LocalTime.of(hrs, mins));
		}
		return showTimes;
	}
	/**
	 * Creates showtimes for a movie over a date range and list of times.
	 * @param movie The movie for which showtimes are being created.
	 * @param startDate The start date for the showtimes.
	 * @param endDate The end date for the showtimes.
	 * @param screenNumber The screen number for the showtimes.
	 * @param showTimes The list of showtimes to be created.
	 * @return The number of showtimes successfully created.
	 */
	public int createShowtimes(Movie movie, LocalDate startDate, LocalDate endDate, int screenNumber, List<LocalTime> showTimes) {
		int count = 0;
		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			for (LocalTime showTime : showTimes) {
				Showtime newShowtime = new Showtime(movie.getMovieId(), date, showTime, screenNumber);
				boolean success = showtimeDAO.createShowtime(newShowtime);
				if (success) count++;
			}
		}
		return count;
	}
	/**
	 * Returns all showtimes for a given movie ID.
	 * @param movieId The ID of the movie.
	 * @return List of showtimes for the movie, empty list if none found.
	 */
	public List<Showtime> getShowtimesForMovie(int movieId) {
		return showtimeDAO.getShowtimesByMovieId(movieId);
	}

	/**
	 * Deletes a showtime by its ID. Returns true if successful, false otherwise.
	 * @param showtimeId The ID of the showtime to delete.
	 * @return true if deletion was successful, false otherwise.
	 */
	public boolean deleteShowtime(int showtimeId) {
		return showtimeDAO.deleteShowtime(showtimeId);
	}
}
