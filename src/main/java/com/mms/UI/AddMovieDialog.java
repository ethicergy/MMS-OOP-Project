package com.mms.UI;

import javax.swing.*;
import java.awt.*;
import com.mms.dao.MovieDAO;
import com.mms.models.Movie;

public class AddMovieDialog extends JDialog {
    public AddMovieDialog(JFrame parent) {
        super(parent, "Add New Movie", true);
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
        JButton addButton = new JButton("Add Movie");
        JButton cancelButton = new JButton("Cancel");
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(durationLabel);
        panel.add(durationField);
        panel.add(genre);
        panel.add(genreField);
        panel.add(languageLabel);
        panel.add(languageField);
        panel.add(certificateLabel);
        panel.add(certificateField);
        panel.add(addButton);
        panel.add(cancelButton);
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
                    Movie newMovie = new Movie(title, dur, genre_var, language, certificate, "");
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
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        new AddMovieDialog(frame);
    }
}
