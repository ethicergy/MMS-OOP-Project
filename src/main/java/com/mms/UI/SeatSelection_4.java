package com.mms.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
// import java.math.BigDecimal;
// import java.time.format.DateTimeFormatter;
import com.mms.models.Movie;
import com.mms.models.Showtime;
import com.mms.controllers.BookingController;
import com.mms.controllers.SeatController;

/**
 * SeatSelection — exact layout requested
 */
public class SeatSelection_4 extends JFrame {
    private Showtime selectedShowtime;
    private Movie selectedMovie;
    private BookingController bookingController;
    private SeatController seatController;

    // Default constructor for backward compatibility
    public SeatSelection_4() {
        this(null, null);
    }

    public SeatSelection_4(Showtime showtime, Movie movie) {
        super("Seat Selection");
        this.selectedShowtime = showtime;
        this.selectedMovie = movie;
        this.bookingController = new BookingController();
        this.seatController = new SeatController();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(234, 224, 213));
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon("title-logo.png").getImage());

        // Heading - dynamic based on showtime and movie data
        String headingText = getHeadingText();
        JLabel heading = new JLabel(headingText, SwingConstants.LEFT);
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(new Color(10, 9, 8));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 0));
        add(heading, BorderLayout.NORTH);

        // Design base width (columns total = 16 across blocks)
        int baseWidth = 1000;

        // Create scalable panel (it computes its own design height based on block sizes)
        int showtimeId = (selectedShowtime != null) ? selectedShowtime.getShowtimeId() : -1;
        ScalableSeatPanel scalable = new ScalableSeatPanel(baseWidth, showtimeId);
        scalable.setBackground(new Color(234, 224, 213));

        // Wrapper for seat block — lock width to 1000 and height to at least 800 (or computed)
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(new Color(234, 224, 213));
        int computedHeight = scalable.getDesignHeight();
        int panelWidth = baseWidth;
        int panelHeight = Math.max(800, computedHeight + 40); // ensure at least 800 high
        centerWrapper.setPreferredSize(new Dimension(panelWidth, panelHeight));

        // Add seat canvas centered inside the wrapper
        centerWrapper.add(scalable, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        // Bottom: legend + confirm button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(234, 224, 213));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 18, 16));

        JPanel legendAndButton = new JPanel(new BorderLayout());
        legendAndButton.setBackground(new Color(234, 224, 213));

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        legend.setBackground(new Color(234, 224, 213));
        legend.add(makeLegendBox(new Color(220, 53, 69), "Unavailable"));
        legend.add(makeLegendBox(new Color(255, 193, 7), "Selected"));
        legend.add(makeLegendBox(new Color(40, 167, 69), "Available"));
        
        // Add back button to legend
        JButton backButton = new JButton("← Back to Movies");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(160, 40));
        backButton.setBackground(new Color(108, 117, 125)); // Gray color
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> returnToMovieSelection());
        legend.add(backButton);

        JButton confirm = new JButton("Select seats");
        confirm.setFont(new Font("Arial", Font.BOLD, 16));
        confirm.setPreferredSize(new Dimension(180, 50));
        confirm.setBackground(new Color(20, 30, 40));
        confirm.setForeground(Color.WHITE);
        confirm.setFocusPainted(false);
        confirm.addActionListener(e -> processBooking(scalable));

        legendAndButton.add(legend, BorderLayout.WEST);
        legendAndButton.add(confirm, BorderLayout.EAST);

        bottomPanel.add(legendAndButton, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private String getHeadingText() {
        if (selectedShowtime == null || selectedMovie == null) {
            return "Inception - 1:00 pm | Screen 1"; // Default fallback
        }
        
        String time = formatTimeForDisplay(selectedShowtime.getTime());
        return selectedMovie.getTitle() + " - " + time + " | Screen " + selectedShowtime.getScreenNumber();
    }
    
    private String formatTimeForDisplay(java.time.LocalTime time) {
        int hour = time.getHour();
        int minute = time.getMinute();
        String amPm = (hour >= 12) ? "pm" : "am";
        hour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
        
        return String.format("%d:%02d %s", hour, minute, amPm);
    }
    
    private void processBooking(ScalableSeatPanel seatPanel) {
        List<String> selectedSeats = seatPanel.getSelectedSeatLabels();
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select at least one seat to continue.", 
                "No Seats Selected", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (selectedShowtime == null || selectedMovie == null) {
            JOptionPane.showMessageDialog(this, 
                "Booking information is incomplete. Please go back and select a movie.", 
                "Booking Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Calculate total price (using default price of ₹200 per seat)
        double pricePerSeat = 200.0;
        double totalPrice = bookingController.calculateTotalPrice(selectedSeats, pricePerSeat);
        // Confirm booking message
        String confirmMessage = String.format(
            "<html><h3>Confirm Your Booking</h3>" +
            "<b>Movie:</b> %s<br>" +
            "<b>Date & Time:</b> %s at %s<br>" +
            "<b>Screen:</b> %d<br>" +
            "<b>Selected Seats:</b> %s<br>" +
            "<b>Number of Seats:</b> %d<br>" +
            "<b>Price per Seat:</b> ₹%.2f<br>" +
            "<b>Total Amount:</b> ₹%.2f<br><br>" +
            "Do you want to confirm this booking?</html>",
            selectedMovie.getTitle(),
            selectedShowtime.getDate().toString(),
            formatTimeForDisplay(selectedShowtime.getTime()),
            selectedShowtime.getScreenNumber(),
            String.join(", ", selectedSeats),
            selectedSeats.size(),
            pricePerSeat,
            totalPrice
        );
        int choice = JOptionPane.showConfirmDialog(this,
            confirmMessage,
            "Confirm Booking",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            boolean success = bookingController.createBookingsForSeatLabels(
                selectedShowtime.getShowtimeId(),
                selectedSeats,
                1, // userId (should come from session)
                java.math.BigDecimal.valueOf(pricePerSeat)
            );
            if (success) {
                // Close this frame and open BookingSuccessfulFrame
                this.dispose();
                new BookingSuccessfulFrame(selectedMovie, selectedShowtime, selectedSeats, totalPrice);
            } else {
                JOptionPane.showMessageDialog(this,
                    "<html><h3>Booking Failed</h3>" +
                    "Sorry, we couldn't complete your booking.<br>" +
                    "Some seats might have been booked by another customer.<br>" +
                    "Please refresh and try selecting different seats.</html>",
                    "Booking Error",
                    JOptionPane.ERROR_MESSAGE);
                refreshSeatLayout();
            }
        }
    }
    
    // Removed: createBookingInDatabase (now handled by BookingController)
    
    private void refreshSeatLayout() {
        // Refresh the seat layout by recreating the seat panel
        // This is a simple approach - in a more complex app you might want to just refresh the data
        SwingUtilities.invokeLater(() -> {
            this.dispose();
            new SeatSelection_4(selectedShowtime, selectedMovie).setVisible(true);
        });
    }
    
    private void returnToMovieSelection() {
        SwingUtilities.invokeLater(() -> {
            this.dispose();
            new MovieSelection_3().setVisible(true);
        });
    }

    private JPanel makeLegendBox(Color color, String label) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setBackground(new Color(234, 224, 213));
        JPanel box = new JPanel();
        box.setBackground(color);
        box.setPreferredSize(new Dimension(20, 20));
        JLabel l = new JLabel(" - " + label);
        l.setFont(new Font("Arial", Font.PLAIN, 14));
        p.add(box);
        p.add(l);
        return p;
    }

    // -------------------- ScalableSeatPanel --------------------
    class ScalableSeatPanel extends JPanel {
        private final int baseW;
        private int baseH; // computed based on layout

        // visual constants
        private final int seatSize = 40;
        private final int seatGap = 10;
        private final int blockGap = 50;
        private final int topMargin = 40;
        private final int rowGap = 40;
        private final int bottomPadding = 50; // space below lower blocks for screen bar

        private final List<Seat> seats = new ArrayList<>();

        // Base Seat class - defines common properties and behavior
        abstract static class Seat {
            protected Rectangle bounds;
            protected String label;
            protected final int SEAT_SIZE = 40;
            protected final int CORNER_RADIUS = 8;
            
            public Seat(Rectangle bounds, String label) {
                this.bounds = bounds;
                this.label = label;
            }
            
            // Abstract methods that subclasses must implement
            public abstract Color getFillColor();
            public abstract Color getBorderColor();
            public abstract boolean isClickable();
            public abstract Seat onClick(); // Returns new seat state after click
            
            // Common drawing method used by all seat types
            public void draw(Graphics2D g2) {
                // Draw seat shape
                RoundRectangle2D rr = new RoundRectangle2D.Double(
                    bounds.x, bounds.y, bounds.width, bounds.height, 
                    CORNER_RADIUS, CORNER_RADIUS
                );
                
                // Fill seat
                g2.setColor(getFillColor());
                g2.fill(rr);
                
                // Draw border
                g2.setColor(getBorderColor());
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(rr);
                
                // Draw label
                drawLabel(g2);
            }
            
            protected void drawLabel(Graphics2D g2) {
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int strW = fm.stringWidth(label);
                int strH = fm.getAscent();
                int sx = bounds.x + (bounds.width - strW) / 2;
                int sy = bounds.y + (bounds.height + strH) / 2 - 2;
                
                g2.setColor(getLabelColor());
                g2.drawString(label, sx, sy);
            }
            
            protected Color getLabelColor() {
                return Color.BLACK;
            }
            
            // Common utility methods
            public boolean contains(Point point) {
                return bounds.contains(point);
            }
            
            public Rectangle getBounds() { return bounds; }
            public String getLabel() { return label; }
        }
        
        // Available seat - can be selected by users
        static class AvailableSeat extends Seat {
            public AvailableSeat(Rectangle bounds, String label) {
                super(bounds, label);
            }
            
            @Override
            public Color getFillColor() {
                return new Color(40, 167, 69); // Green for available
            }
            
            @Override
            public Color getBorderColor() {
                return Color.DARK_GRAY;
            }
            
            @Override
            public boolean isClickable() {
                return true;
            }
            
            @Override
            public Seat onClick() {
                return new SelectedSeat(bounds, label); // Convert to selected
            }
        }
        
        // Selected seat - currently selected by user
        static class SelectedSeat extends Seat {
            public SelectedSeat(Rectangle bounds, String label) {
                super(bounds, label);
            }
            
            @Override
            public Color getFillColor() {
                return new Color(255, 193, 7); // Yellow/amber for selected
            }
            
            @Override
            public Color getBorderColor() {
                return Color.DARK_GRAY;
            }
            
            @Override
            public boolean isClickable() {
                return true;
            }
            
            @Override
            public Seat onClick() {
                return new AvailableSeat(bounds, label); // Convert back to available
            }
        }
        
        // Booked seat - cannot be selected
        static class BookedSeat extends Seat {
            public BookedSeat(Rectangle bounds, String label) {
                super(bounds, label);
            }
            
            @Override
            public Color getFillColor() {
                return new Color(220, 53, 69); // Red for booked
            }
            
            @Override
            public Color getBorderColor() {
                return Color.DARK_GRAY;
            }
            
            @Override
            public boolean isClickable() {
                return false; // Booked seats cannot be clicked
            }
            
            @Override
            public Seat onClick() {
                return this; // No change for booked seats
            }
        }

        ScalableSeatPanel(int baseWidth) {
            this(baseWidth, -1); // Default constructor calls with no showtime
        }
        
        ScalableSeatPanel(int baseWidth, int showtimeId) {
            this.baseW = baseWidth;
            // build seats and compute baseH internally
            buildLayoutAndSeats();
            
            // Load booked seats from database if showtime ID is provided
            if (showtimeId > 0) {
                loadBookedSeatsFromDatabase(showtimeId);
            }
            
            // clicks: map panel point -> design coords, toggle selection
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point p = toDesignCoords(e.getPoint());
                    if (p == null) return;
                    for (int i = 0; i < seats.size(); i++) {
                        Seat s = seats.get(i);
                        if (s.contains(p) && s.isClickable()) {
                            seats.set(i, s.onClick());
                            repaint();
                            break;
                        }
                    }
                }
            });
        }

        /**
         * Public accessor so outer class can size wrapper to at least design height.
         */
        int getDesignHeight() {
            return baseH;
        }

        private Point toDesignCoords(Point panelPoint) {
            double scale = Math.min(getWidth() / (double) baseW, getHeight() / (double) baseH);
            double scaledW = baseW * scale;
            double scaledH = baseH * scale;
            double tx = (getWidth() - scaledW) / 2.0;
            double ty = (getHeight() - scaledH) / 2.0;
            double dx = (panelPoint.x - tx) / scale;
            double dy = (panelPoint.y - ty) / scale;
            if (dx < 0 || dy < 0 || dx > baseW || dy > baseH) return null;
            return new Point((int) dx, (int) dy);
        }

        /**
         * Build seat blocks to match requested layout:
         * Top: 1x16
         * Middle row of blocks: left 5x7, center 6x7, right 5x6  (total 16 columns)
         * Lower row of blocks: left 5x4, center 6x4, right 5x4 (total 16 columns)
         *
         * Seats are labeled rowChar + seatNumberAcross (1..16 across)
         */
        private void buildLayoutAndSeats() {
            seats.clear();

            // Top
            int topCols = 16;
            int topRows = 1;
            int topBlockW = topCols * seatSize + (topCols - 1) * seatGap;
            int topBlockH = topRows * seatSize;

            int topX = (baseW - topBlockW) / 2;
            int topY = topMargin;

            for (int c = 0; c < topCols; c++) {
                String label = "A" + (c + 1);
                int x = topX + c * (seatSize + seatGap);
                int y = topY;
                seats.add(new AvailableSeat(new Rectangle(x, y, seatSize, seatSize), label));
            }

            // Middle (upper) blocks: left 5x7, center 6x7, right 5x6
            int leftMidCols = 5, leftMidRows = 7;
            int centerMidCols = 6, centerMidRows = 7;
            int rightMidCols = 5, rightMidRows = 6;

            int midLeftW = leftMidCols * seatSize + (leftMidCols - 1) * seatGap;
            int midCenterW = centerMidCols * seatSize + (centerMidCols - 1) * seatGap;
            int midRightW = rightMidCols * seatSize + (rightMidCols - 1) * seatGap;

            int totalMidW = midLeftW + midCenterW + midRightW + 2 * blockGap; // 3 blocks + gaps
            int midStartX = (baseW - totalMidW) / 2;
            int midY = topY + topBlockH + rowGap;

            // For numbering across (1..16)
            int[] midBlockCols = new int[] { leftMidCols, centerMidCols, rightMidCols };
            int[] midBlockRows = new int[] { leftMidRows, centerMidRows, rightMidRows };
            //int midColsTotal = leftMidCols + centerMidCols + rightMidCols; // 16

            // compute max mid rows for vertical spacing
            int midRowsMax = Math.max(Math.max(leftMidRows, centerMidRows), rightMidRows);
            //int midBlockW = midLeftW; // not used; we'll compute each blockX

            // Build middle seats row by row (rows aligned at top)
            for (int r = 0; r < midRowsMax; r++) {
                char rowChar = (char) ('B' + r); // after top 'A'
                int blockX = midStartX;
                int cumulativeIndex = 0;
                // left block
                for (int b = 0; b < 3; b++) {
                    int cols = midBlockCols[b];
                    int rows = midBlockRows[b];
                    int blockWidth = cols * seatSize + (cols - 1) * seatGap;
                    if (r < rows) {
                        for (int c = 0; c < cols; c++) {
                            int seatNumberAcross = cumulativeIndex + c + 1;
                            String label = String.valueOf(rowChar) + seatNumberAcross;
                            int x = blockX + c * (seatSize + seatGap);
                            int y = midY + r * (seatSize + seatGap);
                            seats.add(new AvailableSeat(new Rectangle(x, y, seatSize, seatSize), label));
                        }
                    }
                    cumulativeIndex += cols;
                    blockX += blockWidth + blockGap;
                }
            }

            // Lower (bottom) blocks: left 5x4, center 6x4, right 5x4
            int leftBotCols = 5, leftBotRows = 4;
            int centerBotCols = 6, centerBotRows = 4;
            int rightBotCols = 5, rightBotRows = 4;

            int botLeftW = leftBotCols * seatSize + (leftBotCols - 1) * seatGap;
            int botCenterW = centerBotCols * seatSize + (centerBotCols - 1) * seatGap;
            int botRightW = rightBotCols * seatSize + (rightBotCols - 1) * seatGap;

            int totalBotW = botLeftW + botCenterW + botRightW + 2 * blockGap;
            int botStartX = (baseW - totalBotW) / 2;

            // position lower blocks under middle blocks (use midRowsMax height to space)
            int midBlocksHeight = midRowsMax * seatSize + (midRowsMax - 1) * seatGap;
            int botY = midY + midBlocksHeight + rowGap;

            int[] botBlockCols = new int[] { leftBotCols, centerBotCols, rightBotCols };
            int[] botBlockRows = new int[] { leftBotRows, centerBotRows, rightBotRows };

            int botRowsMax = Math.max(Math.max(leftBotRows, centerBotRows), rightBotRows);

            for (int r = 0; r < botRowsMax; r++) {
                char rowChar = (char) ('B' + midRowsMax + r); // continue row letters after middle rows
                int blockX = botStartX;
                int cumulativeIndex = 0;
                for (int b = 0; b < 3; b++) {
                    int cols = botBlockCols[b];
                    int rows = botBlockRows[b];
                    int blockWidth = cols * seatSize + (cols - 1) * seatGap;
                    if (r < rows) {
                        for (int c = 0; c < cols; c++) {
                            int seatNumberAcross = cumulativeIndex + c + 1;
                            String label = String.valueOf(rowChar) + seatNumberAcross;
                            int x = blockX + c * (seatSize + seatGap);
                            int y = botY + r * (seatSize + seatGap);
                            seats.add(new AvailableSeat(new Rectangle(x, y, seatSize, seatSize), label));
                        }
                    }
                    cumulativeIndex += cols;
                    blockX += blockWidth + blockGap;
                }
            }

            // compute baseH from layout so paintComponent can scale correctly
            int midH = midRowsMax * seatSize + (midRowsMax - 1) * seatGap;
            int botH = botRowsMax * seatSize + (botRowsMax - 1) * seatGap;
            baseH = topMargin + topBlockH + rowGap + midH + rowGap + botH + bottomPadding;
            // Booked seats will be loaded by loadBookedSeatsFromDatabase() if showtime ID is provided
            
        }
        
        private void loadBookedSeatsFromDatabase(int showtimeId) {
            try {
                seatController.ensureSeatsForShowtime(showtimeId);
                List<String> bookedSeatLabels = bookingController.getBookedSeatLabels(showtimeId);
                for (String seatLabel : bookedSeatLabels) {
                    markBooked(seatLabel);
                }
                System.out.println("Loaded " + bookedSeatLabels.size() + " booked seats for showtime " + showtimeId);
            } catch (Exception e) {
                System.err.println("Error loading booked seats from database: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void markBooked(String... labels) {
            for (String lbl : labels) {
                for (int i = 0; i < seats.size(); i++) {
                    Seat s = seats.get(i);
                    if (s.label.equals(lbl)) {
                        seats.set(i, new BookedSeat(s.bounds, s.label));
                    }
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            double scale = Math.min(getWidth() / (double) baseW, getHeight() / (double) baseH);
            double scaledW = baseW * scale;
            double scaledH = baseH * scale;
            double tx = (getWidth() - scaledW) / 2.0;
            double ty = (getHeight() - scaledH) / 2.0;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // translate + scale to design coords
            g2.translate(tx, ty);
            g2.scale(scale, scale);

            // background canvas (seat area)
            g2.setColor(new Color(198, 172, 143));
            g2.fillRect(0, 0, baseW, baseH);

            // draw seats using inheritance
            for (Seat s : seats) {
                s.draw(g2);
            }

            // draw screen bar near bottom of seat area
            int screenW = baseW / 3;
            int screenH = 28;
            int screenX = (baseW - screenW) / 2;
            int screenY = baseH - 40;
            g2.setColor(new Color(200, 200, 200));
            g2.fillRoundRect(screenX, screenY, screenW, screenH, 12, 12);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String screenText = "Screen this way";
            FontMetrics fm = g2.getFontMetrics();
            int sw = fm.stringWidth(screenText);
            int sh = fm.getAscent();
            g2.drawString(screenText, screenX + (screenW - sw) / 2, screenY + (screenH + sh) / 2 - 2);

            g2.dispose();
        }

        List<String> getSelectedSeatLabels() {
            List<String> out = new ArrayList<>();
            for (Seat s : seats) {
                if (s instanceof SelectedSeat) {
                    out.add(s.label);
                }
            }
            return out;
        }
    }

    // -------------------- main --------------------
    public static void main(String[] args) {
        new SeatSelection_4(); 
    }
}
