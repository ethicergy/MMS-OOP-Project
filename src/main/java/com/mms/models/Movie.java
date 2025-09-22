package com.mms.models;

import java.time.LocalDateTime;

public class Movie {
	
	private int movieId;
	private String title;
	private int duration;
	private String genre;
	private String language;
	private String certificate;
	private String posterUrl;
	private LocalDateTime createdAt;
	
	public Movie() {}
	
	public Movie(int movieId, String title, int duration, String genre, String language, String certificate, String posterUrl, LocalDateTime createdAt) {
		this.movieId = movieId;
		this.title = title;
		this.duration = duration;
		this.genre = genre;
		this.language = language;
		this.certificate = certificate;
		this.posterUrl = posterUrl;
		this.createdAt = createdAt;
		
	}
	
	public Movie(int movieId, String title, int duration, String genre, String language, String certificate, String posterUrl) {
		this.movieId = movieId;
		this.title = title;
		this.duration = duration;
		this.genre = genre;
		this.language = language;
		this.certificate = certificate;
		this.posterUrl = posterUrl;		
	}
	public Movie(String title, int duration, String genre, String language, String certificate, String posterUrl) {//This one needed when we need to create it, createdAt instance will be auto-assigned do not manage it
		
		this.title = title;
		this.duration = duration;
		this.genre = genre;
		this.language = language;
		this.certificate = certificate;
		this.posterUrl = posterUrl;
		this.createdAt = LocalDateTime.now();
		
		
	}
	public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCertificate() { return certificate; }
    public void setCertificate(String certificate) { this.certificate = certificate; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return String.format("Movie{id=%d, title='%s', duration=%d min, genre='%s', language='%s', certificate='%s'}", 
                movieId, title, duration, genre, language, certificate);
    }
}
