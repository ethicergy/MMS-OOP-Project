package com.mms.UI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import com.mms.controllers.BookingController;
import com.mms.controllers.BookingSummary;
import com.mms.models.Movie;
import com.mms.models.Showtime;
import java.util.Arrays;
import java.util.List;
import com.mms.util.Logger;

public class BookingSummaryDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Booking Summary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(243, 232, 222));

        JLabel title = new JLabel("BOOKING SUMMARY", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(title, BorderLayout.NORTH);

        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new GridLayout(4, 1, 10, 10));
        bookingPanel.setBackground(new Color(196, 161, 121));
        bookingPanel.setPreferredSize(new Dimension(1000, 600));
        bookingPanel.setMaximumSize(new Dimension(600, 400));
        bookingPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.DARK_GRAY, 1),
            BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));

        Font font = new Font("Arial", Font.PLAIN, 26);

        try {
            Movie movie = new Movie("Inception", 120, "Sci-Fi", "English", "U/A", null);
            Showtime showtime = new Showtime(1, java.time.LocalDate.of(2025, 7, 25), java.time.LocalTime.of(13, 0), 1);
            List<String> seats = Arrays.asList("A4", "A5", "A6");
            double totalPrice = 360;
            BookingSummary summary = new BookingController().getBookingSummary(movie, showtime, seats, totalPrice);

            JLabel movieLabel = new JLabel("Movie: " + summary.movieTitle);
            JLabel showtimeLabel = new JLabel("Showtime: " + summary.showtimeDisplay);
            JLabel seatsLabel = new JLabel("Seats: " + summary.seatsDisplay);
            JLabel priceLabel = new JLabel("Total Price: " + summary.totalPriceDisplay);

            movieLabel.setFont(font);
            showtimeLabel.setFont(font);
            seatsLabel.setFont(font);
            priceLabel.setFont(font);

            bookingPanel.add(movieLabel);
            bookingPanel.add(showtimeLabel);
            bookingPanel.add(seatsLabel);
            bookingPanel.add(priceLabel);
        } catch (Exception ex) {
            Logger.log(ex);
            JLabel errorLabel = new JLabel("Error generating booking summary: " + ex.getMessage());
            errorLabel.setFont(font);
            bookingPanel.add(errorLabel);
        }

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(243, 232, 222));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 20, 20, 20);
        centerPanel.add(bookingPanel, gbc);
        frame.add(centerPanel, BorderLayout.CENTER);

        JButton confirmButton = new JButton("Confirm & Pay ₹360");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 16));
        confirmButton.setBackground(new Color(37, 50, 55));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setPreferredSize(new Dimension(200, 40));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(243, 232, 222));
        buttonPanel.add(confirmButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
