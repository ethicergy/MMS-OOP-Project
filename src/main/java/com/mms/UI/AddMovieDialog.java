package com.mms.UI;

import javax.swing.*;
import java.awt.*;
import com.mms.dao.MovieDAO;
import com.mms.dao.ShowtimeDAO;

import java.util.ArrayList;
import java.util.List;
import com.mms.models.Movie;
import com.mms.models.Showtime;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AddMovieDialog extends JDialog {
    Color bgColor = new Color(198, 172, 143);
    public AddMovieDialog(JFrame parent) {
        super(parent, "Add New Movie", true);
        setSize(500, 500);
        setLocationRelativeTo(parent);
        getRootPane().setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(234, 224, 213)));
        JLabel titleLabel = new JLabel("Movie Title:");
        JTextField titleField = new JTextField(10);
        JLabel durationLabel = new JLabel("Duration (mins):");
        JTextField durationField = new JTextField(4);
        JLabel genre = new JLabel("Genre:");
        JTextField genreField = new JTextField(10);
        JLabel languageLabel = new JLabel("Language:");
        JTextField languageField = new JTextField(10);
        JLabel certificateLabel = new JLabel("Certificate:");
        JTextField certificateField = new JTextField(10);
        JLabel posterLabel = new JLabel("Poster Image:");
        JTextField posterField = new JTextField(20);
        posterField.setEditable(false);
        JButton uploadButton = new JButton("Upload Image");
        JButton addButton = new JButton("Add Movie");
        JButton cancelButton = new JButton("Cancel");
        addButton.setBackground(new Color(34, 51, 59));
        addButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(34, 51, 59));
        cancelButton.setForeground(Color.WHITE);
        uploadButton.setBackground(new Color(34, 51, 59));
        uploadButton.setForeground(Color.WHITE);

        // File upload functionality
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif", "bmp"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    // Create images directory if it doesn't exist
                    File imagesDir = new File("./images");
                    if (!imagesDir.exists()) {
                        imagesDir.mkdirs();
                    }
                    // Save to images folder
                    File dest = new File("./images/" + selectedFile.getName());
                    Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    posterField.setText("images/" + selectedFile.getName()); // Save relative path
                    JOptionPane.showMessageDialog(this, "Image uploaded successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Failed to upload image: " + ex.getMessage());
                }
            }
        });
    
    List<JPanel> panels = new ArrayList<>();
        
    JPanel panel = new JPanel(new GridBagLayout());
    panels.add(panel);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.gridx = 0; gbc.gridy = 0;
    panel.add(titleLabel, gbc);
    gbc.gridy++;
    panel.add(durationLabel, gbc);
    gbc.gridy++;
    panel.add(genre, gbc);
    gbc.gridy++;
    panel.add(languageLabel, gbc);
    gbc.gridy++;
    panel.add(certificateLabel, gbc);
    gbc.gridy++;
    panel.add(posterLabel, gbc);

    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.gridx = 1; gbc.gridy = 0;
    panel.add(titleField, gbc);
    gbc.gridy++;
    panel.add(durationField, gbc);
    gbc.gridy++;
    panel.add(genreField, gbc);
    gbc.gridy++;
    panel.add(languageField, gbc);
    gbc.gridy++;
    panel.add(certificateField, gbc);
    gbc.gridy++;
    JPanel posterPanel = new JPanel(new BorderLayout(5,0));
    panels.add(posterPanel);
    posterPanel.add(posterField, BorderLayout.CENTER);
    posterPanel.add(uploadButton, BorderLayout.EAST);
    panel.add(posterPanel, gbc);

    // Button row
    gbc.gridx = 0; gbc.gridy++;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    panels.add(buttonPanel);
    for (JPanel p : panels) {
        p.setBackground(bgColor);
    }
    buttonPanel.add(addButton);
    buttonPanel.add(cancelButton);
    panel.add(buttonPanel, gbc);

    
    add(panel);
    pack();
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String duration = durationField.getText().trim();
            String genre_var = genreField.getText().trim();
            String language = languageField.getText().trim();
            String certificate = certificateField.getText().trim();
            if (title.isEmpty() || duration.isEmpty() || genre_var.isEmpty() || language.isEmpty() || certificate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int dur = Integer.parseInt(duration);
                if (dur <= 0) {
                    JOptionPane.showMessageDialog(this, "Duration must be a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    String posterUrl = posterField.getText().trim();
                    Movie newMovie = new Movie(title, dur, genre_var, language, certificate, posterUrl);
                    MovieDAO movieDAO = new MovieDAO();
                    movieDAO.createMovie(newMovie);
                    JOptionPane.showMessageDialog(this, "Movie added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    if (parent instanceof AdminDashboard_2) {
                        ((AdminDashboard_2) parent).dispose();
                        new AdminDashboard_2().setVisible(true);;
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Duration must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            dispose();
        } );
        cancelButton.addActionListener(e ->
            dispose()
        );
        setVisible(true);
        
    }

    // Constructor for adding movies with callback
    public AddMovieDialog(JFrame parent, Runnable onSaveCallback) {
        super(parent, "Add Movie", true);
        setSize(500, 500);
        setLocationRelativeTo(parent);
        getRootPane().setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(234, 224, 213)));
        JLabel titleLabel = new JLabel("Movie Title:");
        JTextField titleField = new JTextField(10);
        JLabel durationLabel = new JLabel("Duration (mins):");
        JTextField durationField = new JTextField(1);
        JLabel genre = new JLabel("Genre:");
        JTextField genreField = new JTextField(10);
        JLabel languageLabel = new JLabel("Language:");
        JTextField languageField = new JTextField(10);
        JLabel certificateLabel = new JLabel("Certificate:");
        JTextField certificateField = new JTextField(10);
        JLabel posterLabel = new JLabel("Poster Image:");
        JTextField posterField = new JTextField(20);
        posterField.setEditable(false);
        JButton uploadButton = new JButton("Upload Image");
        JButton addButton = new JButton("Add Movie");
        JButton cancelButton = new JButton("Cancel");
        
        // File upload functionality
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif", "bmp"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    // Create images directory if it doesn't exist
                    File imagesDir = new File("./images");
                    if (!imagesDir.exists()) {
                        imagesDir.mkdirs();
                    }
                    // Save to images folder
                    File dest = new File("./images/" + selectedFile.getName());
                    Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    posterField.setText("images/" + selectedFile.getName()); // Save relative path
                    JOptionPane.showMessageDialog(this, "Image uploaded successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Failed to upload image: " + ex.getMessage());
                }
            }
        });
    List<JPanel> panels = new ArrayList<>();
    JPanel panel = new JPanel(new GridBagLayout());
    panels.add(panel);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8);
    gbc.anchor = GridBagConstraints.LINE_END;
    gbc.gridx = 0; gbc.gridy = 0;
    panel.add(titleLabel, gbc);
    gbc.gridy++;
    panel.add(durationLabel, gbc);
    gbc.gridy++;
    panel.add(genre, gbc);
    gbc.gridy++;
    panel.add(languageLabel, gbc);
    gbc.gridy++;
    panel.add(certificateLabel, gbc);
    gbc.gridy++;
    panel.add(posterLabel, gbc);

    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.gridx = 1; gbc.gridy = 0;
    panel.add(titleField, gbc);
    gbc.gridy++;
    panel.add(durationField, gbc);
    gbc.gridy++;
    panel.add(genreField, gbc);
    gbc.gridy++;
    panel.add(languageField, gbc);
    gbc.gridy++;
    panel.add(certificateField, gbc);
    gbc.gridy++;
    JPanel posterPanel = new JPanel(new BorderLayout(5,0));
    panels.add(posterPanel);
    posterPanel.add(posterField, BorderLayout.CENTER);
    posterPanel.add(uploadButton, BorderLayout.EAST);
    panel.add(posterPanel, gbc);

    // Button row
    gbc.gridx = 0; gbc.gridy++;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    for (JPanel p : panels) {
        p.setBackground(bgColor);
    }
    panels.add(buttonPanel);
    buttonPanel.add(addButton);
    buttonPanel.add(cancelButton);
    panel.add(buttonPanel, gbc);

    add(panel);
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(34, 51, 59));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(34, 51, 59));
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String duration = durationField.getText().trim();
            String genre_var = genreField.getText().trim();
            String language = languageField.getText().trim();
            String certificate = certificateField.getText().trim();
            if (title.isEmpty() || duration.isEmpty() || genre_var.isEmpty() || language.isEmpty() || certificate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int dur = Integer.parseInt(duration);
                if (dur <= 0) {
                    JOptionPane.showMessageDialog(this, "Duration must be a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    String posterUrl = posterField.getText().trim();
                    Movie newMovie = new Movie(title, dur, genre_var, language, certificate, posterUrl);
                    MovieDAO movieDAO = new MovieDAO();
                    movieDAO.createMovie(newMovie);
                    JOptionPane.showMessageDialog(this, "Movie added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    if (onSaveCallback != null) {
                        onSaveCallback.run();
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Duration must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding movie: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(e ->
            dispose()
        );
        setVisible(true);
    }

public AddMovieDialog(JFrame parent, Movie movie) {
    this(parent, movie, null);
}

public AddMovieDialog(JFrame parent, Movie movie, Runnable onSaveCallback) {
    super(parent, "Edit Movie", true);
    setSize(600, 600);
    setLocationRelativeTo(parent);
    getRootPane().setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(234, 224, 213)));

    // Movie fields
    JLabel titleLabel = new JLabel("Movie Title:");
    JTextField titleField = new JTextField(movie.getTitle(), 10);
    JLabel durationLabel = new JLabel("Duration (mins):");
    JTextField durationField = new JTextField(String.valueOf(movie.getDuration()), 1);
    JLabel genreLabel = new JLabel("Genre:");
    JTextField genreField = new JTextField(movie.getGenre(), 10);
    JLabel languageLabel = new JLabel("Language:");
    JTextField languageField = new JTextField(movie.getLanguage(), 10);
    JLabel certificateLabel = new JLabel("Certificate:");
    JTextField certificateField = new JTextField(movie.getCertificate(), 10);
    JLabel posterLabel = new JLabel("Poster Image:");
    JTextField posterField = new JTextField(movie.getPosterUrl(), 20);
    posterField.setEditable(false);
    JButton uploadButton = new JButton("Upload Image");
    
    // File upload functionality
    uploadButton.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif", "bmp"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Create images directory if it doesn't exist
                File imagesDir = new File("./images");
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs();
                }
                // Save to images folder
                File dest = new File("./images/" + selectedFile.getName());
                Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                posterField.setText("images/" + selectedFile.getName()); // Save relative path
                JOptionPane.showMessageDialog(this, "Image uploaded successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to upload image: " + ex.getMessage());
            }
        }
    });

    // Date picker and showtimes
    JLabel dateLabel = new JLabel("Date of shows:");
    JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
    JButton loadShowtimesBtn = new JButton("Load Showtimes");
    List<JPanel> panels = new ArrayList<>();
    JPanel showtimesPanel = new JPanel();
    panels.add(showtimesPanel);
    showtimesPanel.setLayout(new BoxLayout(showtimesPanel, BoxLayout.Y_AXIS));
    JScrollPane showtimesScroll = new JScrollPane(showtimesPanel);

    // For editing showtimes
    java.util.List<JTextField> hourFields = new java.util.ArrayList<>();
    java.util.List<JTextField> minuteFields = new java.util.ArrayList<>();
    java.util.List<JComboBox<String>> amPmFields = new java.util.ArrayList<>();
    java.util.List<Showtime> loadedShowtimes = new java.util.ArrayList<>();

    loadShowtimesBtn.addActionListener(e -> {
        showtimesPanel.removeAll();
        hourFields.clear();
        minuteFields.clear();
        amPmFields.clear();
        loadedShowtimes.clear();

        java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
        java.time.LocalDate selectedDate = utilDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        ShowtimeDAO showtimeDAO = new ShowtimeDAO();
        java.util.List<Showtime> showtimes = showtimeDAO.getShowtimesByMovieId(movie.getMovieId());
        int count = 0;
        for (Showtime s : showtimes) {
            if (s.getDate().isEqual(selectedDate)) {
                loadedShowtimes.add(s);
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
                panels.add(row);
                JLabel showLabel = new JLabel("Show " + (++count) + ":");
                int hour = s.getTime().getHour();
                int minute = s.getTime().getMinute();
                String amPm = (hour >= 12) ? "PM" : "AM";
                int displayHour = (hour == 0) ? 12 : (hour > 12 ? hour - 12 : hour);

                JTextField hourField = new JTextField(String.valueOf(displayHour), 3);
                hourField.setHorizontalAlignment(JTextField.CENTER);
                JTextField minuteField = new JTextField(String.format("%02d", minute), 3);
                minuteField.setHorizontalAlignment(JTextField.CENTER);
                JComboBox<String> amPmField = new JComboBox<>(new String[]{"AM", "PM"});
                amPmField.setSelectedItem(amPm);

                row.add(showLabel);
                row.add(hourField);
                row.add(new JLabel(":"));
                row.add(minuteField);
                row.add(amPmField);

                showtimesPanel.add(row);
                hourFields.add(hourField);
                minuteFields.add(minuteField);
                amPmFields.add(amPmField);
            }
        }
        if (count == 0) {
            showtimesPanel.add(new JLabel("No showtimes found for this date."));
        }
        showtimesPanel.revalidate();
        showtimesPanel.repaint();
    });

    JButton saveButton = new JButton("Save Changes");
    JButton cancelButton = new JButton("Cancel");

    saveButton.addActionListener(e -> {
        // Validate and update movie details
        String title = titleField.getText().trim();
        String duration = durationField.getText().trim();
        String genre_var = genreField.getText().trim();
        String language = languageField.getText().trim();
        String certificate = certificateField.getText().trim();
        if (title.isEmpty() || duration.isEmpty() || genre_var.isEmpty() || language.isEmpty() || certificate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int dur = Integer.parseInt(duration);
            if (dur <= 0) {
                JOptionPane.showMessageDialog(this, "Duration must be a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String posterUrl = posterField.getText().trim();
            Movie updatedMovie = new Movie(movie.getMovieId(), title, dur, genre_var, language, certificate, posterUrl);
            MovieDAO movieDAO = new MovieDAO();
            movieDAO.updateMovie(updatedMovie); // You need to implement updateMovie in MovieDAO

            // Update showtimes if any loaded
            if (!loadedShowtimes.isEmpty()) {
                ShowtimeDAO showtimeDAO = new ShowtimeDAO();
                for (int i = 0; i < loadedShowtimes.size(); i++) {
                    Showtime s = loadedShowtimes.get(i);
                    String hours = hourFields.get(i).getText().trim();
                    String minutes = minuteFields.get(i).getText().trim();
                    String amPm = (String) amPmFields.get(i).getSelectedItem();
                    int hrs = Integer.parseInt(hours);
                    int mins = Integer.parseInt(minutes);
                    if (hrs < 1 || hrs > 12 || mins < 0 || mins > 59) {
                        JOptionPane.showMessageDialog(this, "Invalid time format for show " + (i+1), "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (amPm.equals("PM") && hrs != 12) hrs += 12;
                    if (amPm.equals("AM") && hrs == 12) hrs = 0;
                    java.time.LocalTime newTime = java.time.LocalTime.of(hrs, mins);
                    s.setTime(newTime);
                    showtimeDAO.updateShowtimeTime(s.getShowtimeId(), newTime); // You need to implement this in ShowtimeDAO
                }
            }

            JOptionPane.showMessageDialog(this, "Movie and showtimes updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Call the callback to refresh the parent table
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duration must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    cancelButton.addActionListener(e -> dispose());

    JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
    panels.add(formPanel);
    formPanel.add(titleLabel); formPanel.add(titleField);
    formPanel.add(durationLabel); formPanel.add(durationField);
    formPanel.add(genreLabel); formPanel.add(genreField);
    formPanel.add(languageLabel); formPanel.add(languageField);
    formPanel.add(certificateLabel); formPanel.add(certificateField);
    formPanel.add(posterLabel); formPanel.add(posterField);
    formPanel.add(new JLabel()); formPanel.add(uploadButton);

    JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panels.add(datePanel);
    datePanel.add(dateLabel);
    datePanel.add(dateSpinner);
    datePanel.add(loadShowtimesBtn);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    panels.add(buttonPanel);
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

    JPanel mainPanel = new JPanel();
    panels.add(mainPanel);
    for (JPanel p : panels) {
        p.setBackground(bgColor);
    }
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(formPanel);
    mainPanel.add(datePanel);
    mainPanel.add(showtimesScroll);
    mainPanel.add(buttonPanel);

    add(mainPanel);
    pack();
    setVisible(true);
}
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        new AddMovieDialog(frame);
    }
}
