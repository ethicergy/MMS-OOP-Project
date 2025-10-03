package com.mms.controllers;

import com.mms.dao.SeatDAO;
import com.mms.dao.BookingDAO;
import com.mms.models.Seat;
import com.mms.models.Bookings;
import java.util.List;
import java.util.ArrayList;

public class SeatController {
    private SeatDAO seatDAO;
    private BookingDAO bookingDAO;
    
    public SeatController() {
        this.seatDAO = new SeatDAO();
        this.bookingDAO = new BookingDAO();
    }
    
    /**
     * Gets available seats for a specific showtime
     */
    public List<Seat> getAvailableSeats(int showtimeId) {
        List<Seat> allSeats = seatDAO.getSeatsByShowtime(showtimeId);
        List<Bookings> bookings = bookingDAO.getBookingsByShowtimeId(showtimeId);
        
        // Get booked seat IDs
        List<Integer> bookedSeatIds = new ArrayList<>();
        for (Bookings booking : bookings) {
            bookedSeatIds.add(booking.getSeatId());
        }
        
        // Filter out booked seats
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : allSeats) {
            if (!bookedSeatIds.contains(seat.getSeatId()) && "available".equals(seat.getStatus())) {
                availableSeats.add(seat);
            }
        }
        
        return availableSeats;
    }
    
    /**
     * Checks if seats are available for booking
     */
    public boolean areSeatsAvailable(List<Integer> seatIds, int showtimeId) {
        List<Seat> availableSeats = getAvailableSeats(showtimeId);
        List<Integer> availableSeatIds = new ArrayList<>();
        
        for (Seat seat : availableSeats) {
            availableSeatIds.add(seat.getSeatId());
        }
        
        return availableSeatIds.containsAll(seatIds);
    }
    
    /**
     * Gets seat by seat ID
     */
    public Seat getSeatById(int seatId) {
        return seatDAO.getSeatById(seatId);
    }
    
    /**
     * Creates a new seat
     */
    public Seat createSeat(int showtimeId, String seatRow, int seatCol, String status) {
        Seat seat = new Seat(showtimeId, seatRow, seatCol, status);
        seatDAO.createSeat(seat);
        return seat;
    }
    
    /**
     * Updates seat status
     */
    public void updateSeatStatus(int seatId, String status) {
        seatDAO.updateSeatStatus(seatId, status);
    }
    
    /**
     * Ensures seats exist for a showtime (creates them if they don't exist)
     */
    public void ensureSeatsForShowtime(int showtimeId) {
        seatDAO.ensureSeatsForShowtime(showtimeId);
    }
}