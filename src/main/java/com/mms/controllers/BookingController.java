package com.mms.controllers;

import com.mms.dao.BookingDAO;
import com.mms.models.Bookings;
import com.mms.models.User;
import com.mms.models.Showtime;
import java.util.List;
import java.util.ArrayList;
import com.mms.controllers.BookingSummary;

public class BookingController {
    private BookingDAO bookingDAO;
    public BookingController() {
        this.bookingDAO = new BookingDAO();
    }
    /**
     * Formats the booking summary for display in the success frame.
     * @param movie Movie object
     * @param showtime Showtime object
     * @param seats List of seat labels (e.g., ["A4", "A5"])
     * @param totalPrice total price as int or double
     * @return BookingSummary with formatted fields
     */
    public BookingSummary getBookingSummary(com.mms.models.Movie movie, com.mms.models.Showtime showtime, java.util.List<String> seats, double totalPrice) {
        String movieTitle = movie != null ? movie.getTitle() : "";
        String showtimeDisplay = showtime != null ? String.format("%s  |  Screen %d  |  %s",
                showtime.getTime().toString(), showtime.getScreenNumber(),
                showtime.getDate().format(java.time.format.DateTimeFormatter.ofPattern("d/M/yy"))) : "";
        String seatsDisplay = seats != null ? String.join(", ", seats) : "";
        String totalPriceDisplay = String.format("\u20B9%.0f", totalPrice);
        return new BookingSummary(movieTitle, showtimeDisplay, seatsDisplay, totalPriceDisplay);
    }

    public boolean validateBookingData(User user, Showtime showtime, List<String> selectedSeats) {
        return user != null && showtime != null && selectedSeats != null && !selectedSeats.isEmpty();
    }

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

    public List<Bookings> getAllBookings() {
        return bookingDAO.getAllBookings();
    }

    public List<Bookings> getBookingsByUserId(int userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }

    public List<Bookings> getBookingsByShowtimeId(int showtimeId) {
        return bookingDAO.getBookingsByShowtimeId(showtimeId);
    }

    public void cancelBooking(int bookingId) {
        bookingDAO.deleteBooking(bookingId);
    }

    public double calculateTotalPrice(List<String> selectedSeats, double pricePerSeat) {
        return selectedSeats.size() * pricePerSeat;
    }

    public boolean createBookingsForSeatLabels(int showtimeId, List<String> seatLabels, int userId, java.math.BigDecimal pricePerSeat) {
        return bookingDAO.createBookingsForSeatLabels(showtimeId, seatLabels, userId, pricePerSeat);
    }

    public List<String> getBookedSeatLabels(int showtimeId) {
        return bookingDAO.getBookedSeatLabels(showtimeId);
    }
}