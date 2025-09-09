package com.mms.UI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BookingSummary {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Booking Summary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(243, 232, 222)); // Outer background

        // Title Label
        JLabel title = new JLabel("BOOKING SUMMARY", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(title, BorderLayout.NORTH);

        // Panel for booking details (inner box)
        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new GridLayout(4, 1, 10, 10));
        bookingPanel.setBackground(new Color(196, 161, 121)); // Inner box background
        bookingPanel.setPreferredSize(new Dimension(1000, 600)); // Set fixed size
        bookingPanel.setMaximumSize(new Dimension(600, 400));
        bookingPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.DARK_GRAY, 1), // Line border around the box
            BorderFactory.createEmptyBorder(20, 40, 20, 40) // Padding inside the box
        ));

        // Booking info labels
        JLabel movieLabel = new JLabel("Movie:............................................................ Inception");
        JLabel showtimeLabel = new JLabel("Showtime:...................... 1:00pm | Screen 1 | 25/7/25");
        JLabel seatsLabel = new JLabel("Seats:............................................................. A4, A5, A6");
        JLabel priceLabel = new JLabel("Total Price:............................................... ₹120 × 3 = ₹360");

        Font font = new Font("Arial", Font.PLAIN, 26);
        movieLabel.setFont(font);
        showtimeLabel.setFont(font);
        seatsLabel.setFont(font);
        priceLabel.setFont(font);

        bookingPanel.add(movieLabel);
        bookingPanel.add(showtimeLabel);
        bookingPanel.add(seatsLabel);
        bookingPanel.add(priceLabel);

        // Center panel with GridBagLayout for full centering
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(243, 232, 222)); // Match outer background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 20, 20, 20); // Optional padding

        centerPanel.add(bookingPanel, gbc);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Confirm button
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

        // Show frame
        frame.setLocationRelativeTo(null); // Center window
        frame.setVisible(true);
    }
}
