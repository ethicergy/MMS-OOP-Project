package com.mms.controllers;

import com.mms.dao.BookingDAO;
import com.mms.models.Bookings;
import com.mms.models.User;
import com.mms.models.Showtime;
import java.util.List;
import java.util.ArrayList;

public class BookingController {
    private BookingDAO bookingDAO;
    
    public BookingController() {
        this.bookingDAO = new BookingDAO();
    }
    
    /**
     * Validates booking data
     */
    public boolean validateBookingData(User user, Showtime showtime, List<String> selectedSeats) {
        return user != null && showtime != null && selectedSeats != null && !selectedSeats.isEmpty();
    }
    
    /**
     * Creates a new booking for a single seat
     */
    public Bookings createBooking(User user, Showtime showtime, int seatId, double totalPrice) {
        Bookings booking = new Bookings(
            user.getUserId(),
            showtime.getShowtimeId(),
            seatId,
            java.math.BigDecimal.valueOf(totalPrice)
        );
        
        bookingDAO.createBooking(booking);
        return booking;
    }
    
    /**
     * Creates multiple bookings for multiple seats
     */
    public List<Bookings> createBookings(User user, Showtime showtime, List<Integer> seatIds, double pricePerSeat) {
        List<Bookings> bookings = new ArrayList<>();
        
        for (Integer seatId : seatIds) {
            Bookings booking = new Bookings(
                user.getUserId(),
                showtime.getShowtimeId(),
                seatId,
                java.math.BigDecimal.valueOf(pricePerSeat)
            );
            bookingDAO.createBooking(booking);
            bookings.add(booking);
        }
        
        return bookings;
    }
    
    /**
     * Gets all bookings
     */
    public List<Bookings> getAllBookings() {
        return bookingDAO.getAllBookings();
    }
    
    /**
     * Gets bookings by user ID
     */
    public List<Bookings> getBookingsByUserId(int userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }
    
    /**
     * Gets bookings by showtime ID
     */
    public List<Bookings> getBookingsByShowtimeId(int showtimeId) {
        return bookingDAO.getBookingsByShowtimeId(showtimeId);
    }
    
    /**
     * Cancels a booking
     */
    public void cancelBooking(int bookingId) {
        bookingDAO.deleteBooking(bookingId);
    }
    
    /**
     * Calculates total price for selected seats
     */
    public double calculateTotalPrice(List<String> selectedSeats, double pricePerSeat) {
        return selectedSeats.size() * pricePerSeat;
    }
    
    /**
     * Creates bookings for multiple seat labels
     */
    public boolean createBookingsForSeatLabels(int showtimeId, List<String> seatLabels, int userId, java.math.BigDecimal pricePerSeat) {
        return bookingDAO.createBookingsForSeatLabels(showtimeId, seatLabels, userId, pricePerSeat);
    }
    
    /**
     * Gets booked seat labels for a showtime
     */
    public List<String> getBookedSeatLabels(int showtimeId) {
        return bookingDAO.getBookedSeatLabels(showtimeId);
    }
}