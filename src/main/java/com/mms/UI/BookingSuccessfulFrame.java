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
        java.awt.image.BufferedImage qrImage = null;
        BookingController bookingController = new BookingController();
        try {
            summary = bookingController.getBookingSummary(movie, showtime, seats, totalPrice);
            qrImage = bookingController.generateBookingQRCode(movie, showtime, seats, totalPrice);
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

        // Main container for booking details and QR code
        JPanel mainDetailsPanel = new JPanel(new BorderLayout(20, 0));
        mainDetailsPanel.setOpaque(false);
        mainDetailsPanel.setPreferredSize(new Dimension(900, 400));

        // Left panel: Booking details
        JPanel rectPanel = new JPanel(new GridBagLayout());
        rectPanel.setBackground(RECT);
        rectPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 2),
                new EmptyBorder(25, 40, 25, 40)
        ));
        rectPanel.setPreferredSize(new Dimension(500, 400));

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

        // Right panel: QR Code
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setOpaque(false);
        qrPanel.setPreferredSize(new Dimension(280, 400));
        
        JLabel qrTitle = new JLabel("<html><center>Scan QR Code<br><small>for Ticket Details</small></center></html>", SwingConstants.CENTER);
        qrTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        qrTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel qrLabel = new JLabel();
        if (qrImage != null) {
            // Scale the QR code image to fit better
            Image scaledQR = qrImage.getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            qrLabel.setIcon(new ImageIcon(scaledQR));
            qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Add click functionality to show QR code details
            qrLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            qrLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    String details = bookingController.getBookingDetailsString(movie, showtime, seats, totalPrice);
                    JOptionPane.showMessageDialog(BookingSuccessfulFrame.this, 
                        "<html><pre>" + details.replace("\n", "<br>") + "</pre></html>", 
                        "QR Code Content", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            });
        } else {
            qrLabel.setText("<html><center>QR Code<br>Not Available</center></html>");
            qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        qrLabel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Add a small instruction label
        JLabel instructionLabel = new JLabel("<html><center><small>Click QR code to view details</small></center></html>", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        instructionLabel.setForeground(new Color(100, 100, 100));
        
        qrPanel.add(qrTitle, BorderLayout.NORTH);
        qrPanel.add(qrLabel, BorderLayout.CENTER);
        qrPanel.add(instructionLabel, BorderLayout.SOUTH);

        // Add both panels to main container
        mainDetailsPanel.add(rectPanel, BorderLayout.WEST);
        mainDetailsPanel.add(qrPanel, BorderLayout.EAST);

        g.gridy = 1;
        center.add(mainDetailsPanel, g);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setBackground(BG);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        JButton downloadBtn = new JButton("Download Ticket (PDF)");
        downloadBtn.setBackground(BTN);
        downloadBtn.setForeground(Color.WHITE);
        downloadBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        downloadBtn.setFocusPainted(false);
        downloadBtn.setPreferredSize(new Dimension(280, 50));
        downloadBtn.addActionListener(e -> {
            // Show booking details as formatted text (can be enhanced to actual PDF generation)
            String details = bookingController.getBookingDetailsString(movie, showtime, seats, totalPrice);
            
            // Create a dialog with better formatting
            JDialog detailsDialog = new JDialog(this, "Ticket Details", true);
            detailsDialog.setSize(400, 350);
            detailsDialog.setLocationRelativeTo(this);
            
            JTextArea textArea = new JTextArea(details);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setBackground(new Color(248, 249, 250));
            textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            
            JButton closeBtn = new JButton("Close");
            closeBtn.addActionListener(evt -> detailsDialog.dispose());
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(closeBtn);
            
            detailsDialog.add(scrollPane, BorderLayout.CENTER);
            detailsDialog.add(buttonPanel, BorderLayout.SOUTH);
            detailsDialog.setVisible(true);
        });

        JButton returnBtn = new JButton("Return to Movies");
        returnBtn.setBackground(new Color(108, 117, 125));
        returnBtn.setForeground(Color.WHITE);
        returnBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        returnBtn.setFocusPainted(false);
        returnBtn.setPreferredSize(new Dimension(200, 50));
        returnBtn.addActionListener(e -> {
            this.dispose();
            new MovieSelection_3().setVisible(true);
        });

        bottom.add(downloadBtn);
        bottom.add(returnBtn);
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
