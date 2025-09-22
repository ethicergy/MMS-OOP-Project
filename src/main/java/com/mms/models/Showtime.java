package com.mms.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Showtime {
	private int showtimeId;
	private int movieId;
	private LocalDate date;
	private LocalTime time;
	private int screenNumber;
	
	public Showtime() {}
	
	public Showtime(int showtimeId, int movieId, LocalDate date, LocalTime time, int screenNumber) {
		this.showtimeId = showtimeId;
		this.movieId = movieId;
		this.date = date;
		this.time = time;
		this.screenNumber = screenNumber;
		
		
	}
	public Showtime( int movieId, LocalDate date, LocalTime time, int screenNumber) {
		
		this.movieId = movieId;
		this.date = date;
		this.time = time;
		this.screenNumber = screenNumber;
		
		
	}
	 public int getShowtimeId() { return showtimeId; }
	    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

	    public int getMovieId() { return movieId; }
	    public void setMovieId(int movieId) { this.movieId = movieId; }

	    public LocalDate getDate() { return date; }
	    public void setDate(LocalDate date) { this.date = date; }

	    public LocalTime getTime() { return time; }
	    public void setTime(LocalTime time) { this.time = time; }

	    public int getScreenNumber() { return screenNumber; }
	    public void setScreenNumber(int screenNumber) { this.screenNumber = screenNumber; }

	    @Override
	    public String toString() {
	        return String.format("Showtime{id=%d, movieId=%d, date=%s, time=%s, screen=%d}", 
	                showtimeId, movieId, date, time, screenNumber);
	    }
}
