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
        final java.awt.image.BufferedImage[] qrImageHolder = new java.awt.image.BufferedImage[1];
        BookingController bookingController = new BookingController();
        try {
            summary = bookingController.getBookingSummary(movie, showtime, seats, totalPrice);
            qrImageHolder[0] = bookingController.generateBookingQRCode(movie, showtime, seats, totalPrice);
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

        // Main container for booking details and QR code using GridBagLayout for better control
        JPanel mainDetailsPanel = new JPanel(new GridBagLayout());
        mainDetailsPanel.setOpaque(false);
        mainDetailsPanel.setPreferredSize(new Dimension(1000, 420));

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(0, 10, 0, 10);
        mainGbc.anchor = GridBagConstraints.CENTER;
        mainGbc.fill = GridBagConstraints.BOTH;

        // Left panel: Booking details
        JPanel rectPanel = new JPanel(new GridBagLayout());
        rectPanel.setBackground(RECT);
        rectPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 2),
                new EmptyBorder(25, 40, 25, 40)
        ));
        rectPanel.setPreferredSize(new Dimension(650, 400));
        rectPanel.setMinimumSize(new Dimension(650, 400));

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
        qrPanel.setBackground(BG);
        qrPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 2),
            new EmptyBorder(20, 20, 20, 20)
        ));
        qrPanel.setPreferredSize(new Dimension(320, 400));
        qrPanel.setMinimumSize(new Dimension(320, 400));
        
        JLabel qrTitle = new JLabel("<html><center><b>Scan QR Code</b><br><small>for Ticket Details</small></center></html>", SwingConstants.CENTER);
        qrTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        qrTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel qrLabel = new JLabel();
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
        qrLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        if (qrImageHolder[0] != null) {
            // Scale the QR code image to fit better
            Image scaledQR = qrImageHolder[0].getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            qrLabel.setIcon(new ImageIcon(scaledQR));
            
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
            qrLabel.setText("<html><center><font size='4'>QR Code<br>Not Available</font></center></html>");
            qrLabel.setPreferredSize(new Dimension(200, 200));
        }
        
        // Add a small instruction label
        JLabel instructionLabel = new JLabel("<html><center><small><i>Click QR code to view details</i></small></center></html>", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        instructionLabel.setForeground(new Color(100, 100, 100));
        
        qrPanel.add(qrTitle, BorderLayout.NORTH);
        qrPanel.add(qrLabel, BorderLayout.CENTER);
        qrPanel.add(instructionLabel, BorderLayout.SOUTH);

        // Add both panels to main container with proper spacing
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.weightx = 0.65;
        mainDetailsPanel.add(rectPanel, mainGbc);
        
        mainGbc.gridx = 1;
        mainGbc.gridy = 0;
        mainGbc.weightx = 0.35;
        mainDetailsPanel.add(qrPanel, mainGbc);

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
            // Create file chooser for PDF save location
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Ticket as PDF");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            
            // Set default filename with movie title and timestamp
            String defaultFilename = "Ticket_" + movie.getTitle().replaceAll("[^a-zA-Z0-9]", "_") + 
                                   "_" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                                   ".pdf";
            fileChooser.setSelectedFile(new java.io.File(defaultFilename));
            
            // Set file filter for PDF files
            javax.swing.filechooser.FileNameExtensionFilter pdfFilter = 
                new javax.swing.filechooser.FileNameExtensionFilter("PDF Documents (*.pdf)", "pdf");
            fileChooser.setFileFilter(pdfFilter);
            
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                
                // Ensure .pdf extension
                String tempPath = selectedFile.getAbsolutePath();
                if (!tempPath.toLowerCase().endsWith(".pdf")) {
                    tempPath += ".pdf";
                }
                final String filePath = tempPath;
                
                // Generate PDF in background to avoid UI freezing
                SwingUtilities.invokeLater(() -> {
                    try {
                        // Show progress dialog
                        JDialog progressDialog = new JDialog(this, "Generating PDF", true);
                        progressDialog.setSize(300, 100);
                        progressDialog.setLocationRelativeTo(this);
                        progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                        
                        JLabel progressLabel = new JLabel("Generating PDF ticket...", SwingConstants.CENTER);
                        progressDialog.add(progressLabel);
                        
                        // Show progress dialog in separate thread
                        Thread progressThread = new Thread(() -> progressDialog.setVisible(true));
                        progressThread.start();
                        
                        // Generate PDF
                        com.mms.util.PDFGenerator.generateTicketPDF(filePath, movie, showtime, seats, totalPrice, qrImageHolder[0]);
                        
                        // Close progress dialog
                        progressDialog.dispose();
                        
                        // Show success message
                        int openResult = JOptionPane.showConfirmDialog(this,
                            "PDF ticket generated successfully!\nSaved to: " + filePath + "\n\nWould you like to open the file?",
                            "PDF Generated",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        if (openResult == JOptionPane.YES_OPTION) {
                            try {
                                java.awt.Desktop.getDesktop().open(new java.io.File(filePath));
                            } catch (Exception openEx) {
                                JOptionPane.showMessageDialog(this,
                                    "PDF generated but couldn't open automatically.\nFile saved to: " + filePath,
                                    "File Saved",
                                    JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        
                    } catch (Exception ex) {
                        Logger.log(ex);
                        JOptionPane.showMessageDialog(this,
                            "Error generating PDF: " + ex.getMessage(),
                            "PDF Generation Failed",
                            JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
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
