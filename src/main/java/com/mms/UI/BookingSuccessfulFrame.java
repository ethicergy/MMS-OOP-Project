package com.mms.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * BookingSuccessfulFrame — taller + narrower box, neatly aligned with tick mark.
 */

import com.mms.controllers.BookingController;
import com.mms.controllers.BookingSummary;
import com.mms.models.Movie;
import com.mms.models.Showtime;
import java.util.List;
import com.mms.util.Logger;

public class BookingSuccessfulFrame extends JFrame {
    // Theme colors
    private static final Color BG = new Color(234, 224, 213);
    private static final Color RECT = new Color(198, 172, 143);
    private static final Color BTN = new Color(34, 51, 59);
    private static final Color BORDER = Color.BLACK;

    public BookingSuccessfulFrame(Movie movie, Showtime showtime, List<String> seats, double totalPrice) {
        setTitle("Booking Successful");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        BookingSummary summary = null;
        try {
            summary = new BookingController().getBookingSummary(movie, showtime, seats, totalPrice);
        } catch (Exception ex) {
            Logger.log(ex);
            JOptionPane.showMessageDialog(this, "Error generating booking summary: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JLabel title = new JLabel("BOOKING SUCCESSFUL", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.weightx = 1;
        g.insets = new Insets(10, 0, 10, 0);

        try {
            ImageIcon tickIcon = new ImageIcon("booking_successful.png");
            Image scaled = tickIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel tick = new JLabel(new ImageIcon(scaled));
            g.gridy = 0;
            center.add(tick, g);
        } catch (Exception ex) {
            Logger.log(ex);
            // If image fails, just skip the tick mark
        }

        JPanel rectPanel = new JPanel(new GridBagLayout());
        rectPanel.setBackground(RECT);
        rectPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 2),
                new EmptyBorder(25, 40, 25, 40)
        ));
        rectPanel.setPreferredSize(new Dimension(700, 400));

        Font labelFont = new Font("SansSerif", Font.BOLD, 22);
        Font valueFont = new Font("SansSerif", Font.PLAIN, 22);
        Font totalFont = new Font("SansSerif", Font.BOLD, 24);

        GridBagConstraints r = new GridBagConstraints();
        r.gridx = 0;
        r.gridy = 0;
        r.weightx = 1;
        r.fill = GridBagConstraints.HORIZONTAL;
        r.insets = new Insets(14, 0, 14, 0);

        rectPanel.add(createRow("Movie:", summary.movieTitle, labelFont, valueFont), r);
        r.gridy++;
        rectPanel.add(createRow("Showtime:", summary.showtimeDisplay, labelFont, valueFont), r);
        r.gridy++;
        rectPanel.add(createRow("Seats:", summary.seatsDisplay, labelFont, valueFont), r);
        r.gridy++;
        rectPanel.add(createRow("Total Price:", summary.totalPriceDisplay, labelFont, totalFont), r);

        g.gridy = 1;
        center.add(rectPanel, g);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setBackground(BG);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        JButton downloadBtn = new JButton("Download Ticket (PDF)");
        downloadBtn.setBackground(BTN);
        downloadBtn.setForeground(Color.WHITE);
        downloadBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        downloadBtn.setFocusPainted(false);
        downloadBtn.setPreferredSize(new Dimension(280, 50));

        bottom.add(downloadBtn);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    // For legacy/test/demo
    public BookingSuccessfulFrame() {
        this(new Movie("Inception", 120, "Sci-Fi", "English", "U/A", null),
             new Showtime(1, java.time.LocalDate.of(2025, 7, 25), java.time.LocalTime.of(13, 0), 1),
             java.util.Arrays.asList("A4", "A5", "A6"),
             360);
    }

    private JPanel createRow(String leftText, String rightText, Font leftFont, Font rightFont) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0, 0, 0, 20);
        gc.anchor = GridBagConstraints.WEST;

        JLabel left = new JLabel(leftText);
        left.setFont(leftFont);
        gc.gridx = 0;
        row.add(left, gc);

        JLabel right = new JLabel(rightText);
        right.setFont(rightFont);
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.EAST;
        row.add(right, gc);

        row.setPreferredSize(new Dimension(600, 50)); // narrower + taller rows
        return row;
    }

    public static void main(String[] args) {
        new BookingSuccessfulFrame();
    }
}
