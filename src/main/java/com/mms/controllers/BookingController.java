package com.mms.controllers;

import com.mms.dao.BookingDAO;
import com.mms.models.Bookings;
import com.mms.models.User;
import com.mms.models.Showtime;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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

    /**
     * Generates a proper QR code with booking details using ZXing library
     * @param movie Movie object
     * @param showtime Showtime object
     * @param seats List of seat labels
     * @param totalPrice Total price
     * @return BufferedImage representing QR code with booking details
     */
    public BufferedImage generateBookingQRCode(com.mms.models.Movie movie, com.mms.models.Showtime showtime, java.util.List<String> seats, double totalPrice) {
        try {
            // Create booking details string for QR code content
            String bookingData = getBookingDetailsString(movie, showtime, seats, totalPrice);
            
            // Set QR code parameters
            int width = 200;
            int height = 200;
            
            // Create QR code writer
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            
            // Generate bit matrix
            BitMatrix bitMatrix = qrCodeWriter.encode(bookingData, BarcodeFormat.QR_CODE, width, height);
            
            // Convert bit matrix to BufferedImage
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
            
        } catch (WriterException e) {
            System.err.println("Error generating QR code: " + e.getMessage());
            e.printStackTrace();
            
            // Return a simple error image if QR generation fails
            BufferedImage errorImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = errorImage.createGraphics();
            g2d.setColor(java.awt.Color.WHITE);
            g2d.fillRect(0, 0, 200, 200);
            g2d.setColor(java.awt.Color.BLACK);
            g2d.drawString("QR Code", 70, 95);
            g2d.drawString("Error", 80, 110);
            g2d.dispose();
            return errorImage;
        }
    }

    /**
     * Get booking details as a string for QR code content
     * @param movie Movie object
     * @param showtime Showtime object
     * @param seats List of seat labels
     * @param totalPrice Total price
     * @return String representation of booking details
     */
    public String getBookingDetailsString(com.mms.models.Movie movie, com.mms.models.Showtime showtime, java.util.List<String> seats, double totalPrice) {
        // Create a structured format for QR code that can be easily parsed
        StringBuilder bookingInfo = new StringBuilder();
        bookingInfo.append("=== MOVIE BOOKING TICKET ===\n");
        bookingInfo.append("Movie: ").append(movie.getTitle()).append("\n");
        bookingInfo.append("Date: ").append(showtime.getDate().toString()).append("\n");
        bookingInfo.append("Time: ").append(showtime.getTime().toString()).append("\n");
        bookingInfo.append("Screen: ").append(showtime.getScreenNumber()).append("\n");
        bookingInfo.append("Seats: ").append(String.join(", ", seats)).append("\n");
        bookingInfo.append("Total: ₹").append(String.format("%.0f", totalPrice)).append("\n");
        bookingInfo.append("Booking ID: ").append(generateBookingId(movie, showtime, seats)).append("\n");
        bookingInfo.append("Generated: ").append(java.time.LocalDateTime.now().toString()).append("\n");
        bookingInfo.append("=== THANK YOU ===");
        
        return bookingInfo.toString();
    }
    
    /**
     * Generate a unique booking ID based on booking details
     * @param movie Movie object
     * @param showtime Showtime object
     * @param seats List of seat labels
     * @return Unique booking ID
     */
    private String generateBookingId(com.mms.models.Movie movie, com.mms.models.Showtime showtime, java.util.List<String> seats) {
        // Create a simple booking ID using hash of booking details
        String data = movie.getTitle() + showtime.getShowtimeId() + seats.toString() + System.currentTimeMillis();
        return "BK" + Math.abs(data.hashCode()) % 1000000;
    }
}