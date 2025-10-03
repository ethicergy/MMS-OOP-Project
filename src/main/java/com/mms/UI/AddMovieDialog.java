package com.mms.UI;

import javax.swing.*;
import java.awt.*;
import com.mms.controllers.MovieController;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import com.mms.util.InputValidator;
import com.mms.util.Logger;


public class AddMovieDialog extends JDialog {
    private final Color bgColor = new Color(198, 172, 143);
    private final MovieController movieController = new MovieController();
    private Runnable onSaveCallback;

    public AddMovieDialog(JFrame parent) {
        this(parent, null);
    }

    public AddMovieDialog(JFrame parent, Runnable onSaveCallback) {
        this(parent, null, onSaveCallback);
    }

    /**
     * New constructor for editing a movie. If movie is null, behaves as add dialog.
     * @param parent parent frame
     * @param movie movie to edit (if null, acts as add)
     * @param onSaveCallback callback to run after save
     */
    public AddMovieDialog(JFrame parent, com.mms.models.Movie movie, Runnable onSaveCallback) {
        super(parent, movie == null ? "Add New Movie" : "Edit Movie", true);
        this.onSaveCallback = onSaveCallback;
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
        JButton saveButton = new JButton(movie == null ? "Add Movie" : "Save Changes");
        JButton cancelButton = new JButton("Cancel");
        saveButton.setBackground(new Color(34, 51, 59));
        saveButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(34, 51, 59));
        cancelButton.setForeground(Color.WHITE);
        uploadButton.setBackground(new Color(34, 51, 59));
        uploadButton.setForeground(Color.WHITE);

        // If editing, pre-fill fields
        if (movie != null) {
            titleField.setText(movie.getTitle());
            durationField.setText(String.valueOf(movie.getDuration()));
            genreField.setText(movie.getGenre());
            languageField.setText(movie.getLanguage());
            certificateField.setText(movie.getCertificate());
            posterField.setText(movie.getPosterUrl() != null ? movie.getPosterUrl() : "");
        }

        // File upload functionality (delegated to controller)
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif", "bmp"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    String relPath = movieController.uploadPosterImage(selectedFile);
                    posterField.setText(relPath);
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
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        add(panel);
        pack();

        saveButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String duration = durationField.getText().trim();
            String genre_var = genreField.getText().trim();
            String language = languageField.getText().trim();
            String certificate = certificateField.getText().trim();
            // Use InputValidator for field checks
            if (InputValidator.isNullOrEmpty(title) || InputValidator.isNullOrEmpty(duration) ||
                InputValidator.isNullOrEmpty(genre_var) || InputValidator.isNullOrEmpty(language) ||
                InputValidator.isNullOrEmpty(certificate)) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int dur = Integer.parseInt(duration);
                String posterUrl = posterField.getText().trim();
                if (movie == null) {
                    movieController.createMovie(title, dur, genre_var, language, certificate, posterUrl);
                    JOptionPane.showMessageDialog(this, "Movie added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    com.mms.models.Movie updatedMovie = new com.mms.models.Movie(movie.getMovieId(), title, dur, genre_var, language, certificate, posterUrl);
                    movieController.updateMovie(updatedMovie);
                    JOptionPane.showMessageDialog(this, "Movie updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                if (onSaveCallback != null) {
                    onSaveCallback.run();
                }
                dispose();
            } catch (NumberFormatException ex) {
                Logger.log(ex);
                JOptionPane.showMessageDialog(this, "Duration must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                Logger.log(ex);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                Logger.log(ex);
                JOptionPane.showMessageDialog(this, "Error saving movie: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(e -> dispose());
        setVisible(true);
    }

    // Legacy main for testing
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        new AddMovieDialog(frame);
    }
}