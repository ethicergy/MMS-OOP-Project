package com.mms.controllers;

import com.mms.dao.MovieDAO;
import com.mms.dao.ShowtimeDAO;
import com.mms.models.Movie;
import com.mms.models.Showtime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class ShowtimeController {
	private final MovieDAO movieDAO;
	private final ShowtimeDAO showtimeDAO;

	public ShowtimeController() {
		this.movieDAO = new MovieDAO();
		this.showtimeDAO = new ShowtimeDAO();
	}

	public List<Movie> getAllMovies() {
		return movieDAO.getAllMovies();
	}

	public boolean validateShowtimeFields(Movie selectedMovie, LocalDate startDate, LocalDate endDate, int numShows, List<String> hours, List<String> minutes, List<String> amPm) {
		if (selectedMovie == null || startDate == null || endDate == null || numShows < 1) return false;
		if (hours.size() != numShows || minutes.size() != numShows || amPm.size() != numShows) return false;
		for (int i = 0; i < numShows; i++) {
			if (hours.get(i).isEmpty() || minutes.get(i).isEmpty() || amPm.get(i) == null) return false;
		}
		return true;
	}

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
}
