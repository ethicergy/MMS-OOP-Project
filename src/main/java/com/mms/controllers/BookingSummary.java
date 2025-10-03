package com.mms.controllers;

public class BookingSummary {
    public final String movieTitle;
    public final String showtimeDisplay;
    public final String seatsDisplay;
    public final String totalPriceDisplay;

    public BookingSummary(String movieTitle, String showtimeDisplay, String seatsDisplay, String totalPriceDisplay) {
        this.movieTitle = movieTitle;
        this.showtimeDisplay = showtimeDisplay;
        this.seatsDisplay = seatsDisplay;
        this.totalPriceDisplay = totalPriceDisplay;
    }
}
