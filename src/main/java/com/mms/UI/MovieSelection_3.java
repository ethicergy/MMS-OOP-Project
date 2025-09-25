package com.mms.UI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.image.BufferedImage;

import com.mms.dao.MovieDAO;
import com.mms.dao.ShowtimeDAO;
import com.mms.models.Movie;
import com.mms.models.Showtime;

public class MovieSelection_3 extends JFrame {
    private LocalDate currentSelectedDate = LocalDate.now();
    private List<JButton> dateButtons = new ArrayList<>();
    private JPanel movieListPanel;

    public MovieSelection_3() {
        setTitle("NOW SHOWING: Book Your Movie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setIconImage(new ImageIcon("title-logo.png").getImage());

        // Background color
        Color bgColor = new Color(234, 224, 213);
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("NOW SHOWING: Book Your Movie", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0)); // spacing below title
        
        // Top panel: Title + Date Selection
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bgColor);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel dateSelectionPanel = createDateSelectionPanel();
        topPanel.add(dateSelectionPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Movie list panel
        movieListPanel = new JPanel();
        movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.Y_AXIS));
        movieListPanel.setBackground(bgColor);

        // Load movies for today initially
        refreshMoviesForDate(LocalDate.now());

        /*
        movieListPanel.add(createMovieRow("Inception", "2h 28m", "English", "Inception.jpg", rowColor1));
        movieListPanel.add(Box.createVerticalStrut(8));
        movieListPanel.add(createMovieRow("Lokah: Chapter 1", "2h 29m", "Malayalam", "lokah Chapter 1.jpg", rowColor2));
        movieListPanel.add(Box.createVerticalStrut(8));
        movieListPanel.add(createMovieRow("Hridayapoorvam", "2h 31m", "Malayalam", "Hridayapoorvam.jpg", rowColor1));
        movieListPanel.add(Box.createVerticalStrut(8));
        movieListPanel.add(createMovieRow("F1: The Movie", "2h 35m", "English", "F1 The Movie.jpg", rowColor2));
        */
        // Container with optimized margins for larger movie boxes
        JScrollPane scrollPane = new JScrollPane(movieListPanel);
scrollPane.setBackground(bgColor);
scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60)); // Reduced side margins to fit larger boxes
scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }
    private JPanel createDateSelectionPanel() {
    JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    datePanel.setBackground(new Color(234, 224, 213));
    datePanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));
    
    LocalDate today = LocalDate.now();
    
    // Create 10 date buttons (today + next 9 days)
    for (int i = 0; i < 10; i++) {
        LocalDate date = today.plusDays(i);
        JButton dateButton = createDateButton(date, i == 0); // first button is selected
        
        // Add click functionality to each date button
        dateButton.addActionListener(e -> {
            selectDate(date, dateButton);
            refreshMoviesForDate(date);
        });
        
        dateButtons.add(dateButton); // Store reference for styling updates
        datePanel.add(dateButton);
    }
    
    return datePanel;
}
private JButton createDateButton(LocalDate date, boolean isSelected) {
    String dateText = formatDateForButton(date);
    JButton button = new JButton(dateText);
    
    button.setPreferredSize(new Dimension(100, 65));
    button.setFont(new Font("Arial", Font.BOLD, 12));
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(34, 51, 59), 1),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    
    // Style based on selection state
    if (isSelected) {
        button.setBackground(new Color(34, 51, 59)); // Selected: Dark blue
        button.setForeground(Color.WHITE);
    } else {
        button.setBackground(new Color(198, 172, 143)); // Normal: Light brown
        button.setForeground(Color.BLACK);
    }
    
    // Enhanced hover effects for date buttons
    button.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            if (isSelected) {
                button.setBackground(new Color(45, 66, 79)); // Darker selected on hover
            } else {
                button.setBackground(new Color(34, 51, 59)); // Dark theme on hover
                button.setForeground(Color.WHITE);
            }
            button.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            if (isSelected) {
                button.setBackground(new Color(34, 51, 59)); // Back to selected
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(198, 172, 143)); // Back to normal
                button.setForeground(Color.BLACK);
            }
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 51, 59), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }
    });
    
    return button;
}
private String formatDateForButton(LocalDate date) {
    LocalDate today = LocalDate.now();
    
    if (date.equals(today)) {
        return "<html><center><b>Today</b><br>" + date.getDayOfMonth() + " " + 
               date.getMonth().toString().substring(0, 3) + "</center></html>";
    } else if (date.equals(today.plusDays(1))) {
        return "<html><center><b>Tomorrow</b><br>" + date.getDayOfMonth() + " " + 
               date.getMonth().toString().substring(0, 3) + "</center></html>";
    } else {
        String dayName = date.getDayOfWeek().toString().substring(0, 3);
        return "<html><center><b>" + dayName + "</b><br>" + date.getDayOfMonth() + " " + 
               date.getMonth().toString().substring(0, 3) + "</center></html>";
    }
}
private void selectDate(LocalDate date, JButton selectedButton) {
    // Reset all date buttons to normal state
    for (JButton btn : dateButtons) {
        btn.setBackground(new Color(198, 172, 143)); // Normal color
        btn.setForeground(Color.BLACK);
    }
    
    // Highlight the selected button
    selectedButton.setBackground(new Color(34, 51, 59)); // Selected color
    selectedButton.setForeground(Color.WHITE);
    
    // Update current selected date
    currentSelectedDate = date;
}
private void refreshMoviesForDate(LocalDate selectedDate) {
    movieListPanel.removeAll();
    JLabel loadingLabel = new JLabel("Loading movies for " + formatDateForDisplay(selectedDate) + "...", SwingConstants.CENTER);
    loadingLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    loadingLabel.setForeground(Color.GRAY);
    movieListPanel.add(loadingLabel);
    movieListPanel.revalidate();
    movieListPanel.repaint();
    
    try {
        ShowtimeDAO showtimeDAO = new ShowtimeDAO();
        List<Showtime> showtimesForDate = showtimeDAO.getShowtimesByDate(selectedDate);
        
        movieListPanel.removeAll();
        
        if (showtimesForDate.isEmpty()) {
            showNoShowsForDateMessage(selectedDate);
            return;
        }
        
        MovieDAO movieDAO = new MovieDAO();
        Set<Integer> movieIdsWithShows = new HashSet<>();
        for (Showtime showtime : showtimesForDate) {
            movieIdsWithShows.add(showtime.getMovieId());
        }
        
        Color rowColor1 = new Color(234, 224, 213);
        Color rowColor2 = new Color(198, 172, 143);
        int rowIndex = 0;
        
        for (Integer movieId : movieIdsWithShows) {
            Movie movie = movieDAO.getMoviebyId(movieId);
            if (movie != null) {
                List<Showtime> movieShowtimes = new ArrayList<>();
                for (Showtime showtime : showtimesForDate) {
                    if (showtime.getMovieId() == movieId) {
                        movieShowtimes.add(showtime);
                    }
                }
                
                Color bgColor = (rowIndex % 2 == 0) ? rowColor1 : rowColor2;
                JPanel movieRow = createMovieRow(movie, movieShowtimes, bgColor);
                movieListPanel.add(movieRow);
                movieListPanel.add(Box.createVerticalStrut(8));
                rowIndex++;
            }
        }
        
    } catch (Exception e) {
        movieListPanel.removeAll();
        showErrorMessage("Unable to load movies for " + formatDateForDisplay(selectedDate));
        e.printStackTrace();
    }
    
    movieListPanel.revalidate();
    movieListPanel.repaint();
}
private void showNoMoviesMessage() {
    JLabel noMoviesLabel = new JLabel(
        "<html><center><h2>No Movies Available</h2><br>Please check back later for new releases.</center></html>",
        SwingConstants.CENTER
    );
    noMoviesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    noMoviesLabel.setForeground(Color.GRAY);
    noMoviesLabel.setPreferredSize(new Dimension(1000, 200));
    movieListPanel.add(noMoviesLabel);
}

private void showNoShowsForDateMessage(LocalDate date) {
    JLabel noShowsLabel = new JLabel(
        "<html><center><h2>No Shows Available</h2><br>No movies scheduled for " + 
        formatDateForDisplay(date) + 
        "<br>Please select a different date.</center></html>",
        SwingConstants.CENTER
    );
    noShowsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    noShowsLabel.setForeground(Color.GRAY);
    noShowsLabel.setPreferredSize(new Dimension(1000, 200));
    movieListPanel.add(noShowsLabel);
}

private void showErrorMessage(String message) {
    JLabel errorLabel = new JLabel(
        "<html><center><h2>Something went wrong</h2><br>" + message + 
        "<br><br>Please refresh the page or try again later.</center></html>",
        SwingConstants.CENTER
    );
    errorLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    errorLabel.setForeground(Color.RED);
    errorLabel.setPreferredSize(new Dimension(1000, 200));
    movieListPanel.add(errorLabel);
}

private String formatDateForDisplay(LocalDate date) {
    return date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
}


    private JPanel createMovieRow(Movie movie, List<Showtime> showtimes, Color bgColor) {
    JPanel rowPanel = new JPanel(new BorderLayout(20, 0));
    rowPanel.setBackground(bgColor);
    // Increased size for better spacing and visual appeal
    rowPanel.setPreferredSize(new Dimension(1000, 180));   // width & height - increased for better layout
    rowPanel.setMaximumSize(new Dimension(1000, 180));
    rowPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(34, 51, 59), 2),
        BorderFactory.createEmptyBorder(10, 10, 10, 10) // Internal padding
    ));

    // Poster - using placeholder for now
    JLabel posterLabel = createPosterLabel(movie);
    rowPanel.add(posterLabel, BorderLayout.WEST);

    // Details - Enhanced with genre and certificate
    JPanel detailsPanel = new JPanel();
    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
    detailsPanel.setBackground(bgColor);

    // Title with better typography
    JLabel titleLabel = new JLabel(movie.getTitle());
    titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
    titleLabel.setForeground(new Color(34, 51, 59)); // Dark theme color

    // Genre and Certificate in one line
    JPanel genreCertPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    genreCertPanel.setBackground(bgColor);
    
    JLabel genreLabel = new JLabel("🎭 " + movie.getGenre());
    genreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    genreLabel.setForeground(new Color(100, 100, 100));
    
    JLabel certLabel = new JLabel("📋 " + movie.getCertificate());
    certLabel.setFont(new Font("Arial", Font.BOLD, 12));
    certLabel.setForeground(new Color(34, 51, 59));
    certLabel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(34, 51, 59), 1),
        BorderFactory.createEmptyBorder(2, 6, 2, 6)
    ));
    
    genreCertPanel.add(genreLabel);
    genreCertPanel.add(certLabel);

    // Duration and Language
    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
    infoPanel.setBackground(bgColor);
    
    JLabel durationLabel = new JLabel("⏱️ " + movie.getDuration() + " mins");
    durationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    durationLabel.setForeground(new Color(80, 80, 80));
    
    JLabel languageLabel = new JLabel("🌐 " + movie.getLanguage());
    languageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    languageLabel.setForeground(new Color(80, 80, 80));
    
    infoPanel.add(durationLabel);
    infoPanel.add(languageLabel);

    // Add all components with enhanced spacing for better visual hierarchy
    detailsPanel.add(titleLabel);
    detailsPanel.add(Box.createVerticalStrut(12));  // More space after title
    detailsPanel.add(genreCertPanel);
    detailsPanel.add(Box.createVerticalStrut(10));  // Better spacing between sections
    detailsPanel.add(infoPanel);
    detailsPanel.add(Box.createVerticalGlue());     // Push content to top
    detailsPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 15, 20)); // Better padding all around

    rowPanel.add(detailsPanel, BorderLayout.CENTER);

    // Showtime buttons - real data from database with better spacing
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 30)); // Tighter horizontal, centered vertical
    buttonPanel.setBackground(bgColor);

    if (showtimes.isEmpty()) {
        // No showtimes available for this movie on selected date
        JLabel noShowsLabel = new JLabel("No showtimes available");
        noShowsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        noShowsLabel.setForeground(Color.GRAY);
        buttonPanel.add(noShowsLabel);
    } else {
        // Create buttons for each showtime
        for (Showtime showtime : showtimes) {
            JButton timeButton = createShowtimeButton(showtime, movie);
            buttonPanel.add(timeButton);
        }
    }

    rowPanel.add(buttonPanel, BorderLayout.EAST);

    return rowPanel;
}
private JButton createShowtimeButton(Showtime showtime, Movie movie) {
    // Format time in 12-hour format with AM/PM
    String timeText = formatTimeForButton(showtime.getTime()) + " (Screen " + showtime.getScreenNumber() + ")";
    
    JButton timeButton = new JButton(timeText);
    timeButton.setPreferredSize(new Dimension(150, 40));
    timeButton.setBackground(new Color(34, 51, 59));
    timeButton.setForeground(Color.WHITE);
    timeButton.setFont(new Font("Arial", Font.BOLD, 13));
    timeButton.setFocusPainted(false);
    timeButton.setBorder(BorderFactory.createRaisedBevelBorder());
    
    // Enhanced hover effects
    timeButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            timeButton.setBackground(new Color(45, 66, 79)); // Lighter on hover
            timeButton.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            timeButton.setBackground(new Color(34, 51, 59)); // Back to original
            timeButton.setBorder(BorderFactory.createRaisedBevelBorder());
        }
    });
    
    // Store showtime data for when button is clicked
    timeButton.putClientProperty("showtime", showtime);
    
    // Add click handler - navigate to seat selection
    timeButton.addActionListener(e -> {
        JButton clickedButton = (JButton) e.getSource();
        Showtime selectedShowtime = (Showtime) clickedButton.getClientProperty("showtime");
        
        // Navigate to seat selection
        navigateToSeatSelection(selectedShowtime, movie);
    });
    
    return timeButton;
}
private String formatTimeForButton(java.time.LocalTime time) {
    int hour = time.getHour();
    int minute = time.getMinute();
    String amPm = (hour >= 12) ? "PM" : "AM";
    hour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
    
    return String.format("%d:%02d %s", hour, minute, amPm);
}

private void navigateToSeatSelection(Showtime showtime, Movie movie) {
    // Close current movie selection window
    this.dispose();
    
    // Open seat selection with the selected showtime and movie
    SwingUtilities.invokeLater(() -> {
        SeatSelection_4 seatSelection = new SeatSelection_4(showtime, movie);
        seatSelection.setVisible(true);
    });
}
    private JLabel createPosterLabel(Movie movie) {
        JLabel posterLabel = new JLabel();
        
        try {
            // TODO: Implement actual poster loading from movie.getPosterUrl()
            String posterPath = movie.getPosterUrl();
            if (posterPath != null && !posterPath.isEmpty()) {
                // Try to load actual poster
                ImageIcon icon = new ImageIcon(posterPath);
                Image scaled = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
                posterLabel.setIcon(new ImageIcon(scaled));
            } else {
                createPlaceholderPoster(posterLabel, movie.getTitle());
            }
        } catch (Exception e) {
            createPlaceholderPoster(posterLabel, movie.getTitle());
        }
        
        posterLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return posterLabel;
    }

    private void createPlaceholderPoster(JLabel label, String movieTitle) {
        // Create a more attractive placeholder with larger dimensions
        BufferedImage placeholder = new BufferedImage(120, 160, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholder.createGraphics();
        
        // Gradient background
        GradientPaint gradient = new GradientPaint(0, 0, new Color(70, 90, 110), 
                                                 120, 160, new Color(40, 60, 80));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 120, 160);
        
        // Border with rounded corners effect
        g2d.setColor(new Color(34, 51, 59));
        g2d.drawRect(0, 0, 119, 159);
        g2d.drawRect(1, 1, 117, 157);
        
        // Movie icon (simple film strip) - adjusted for larger size
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 6; i++) {
            g2d.fillRect(15 + i * 16, 30, 12, 100);
        }
        
        // Title text with better formatting
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        FontMetrics fm = g2d.getFontMetrics();
        
        String[] words = movieTitle.split(" ");
        int y = 50;
        for (String word : words) {
            if (word.length() > 10) word = word.substring(0, 10) + "...";
            int x = (100 - fm.stringWidth(word)) / 2;
            g2d.drawString(word, x, y);
            y += 14;
            if (y > 120) break;
        }
        
        g2d.dispose();
        label.setIcon(new ImageIcon(placeholder));
    }

    // Main method only invokes constructor
    public static void main(String[] args) {
        new MovieSelection_3();
    }
}
