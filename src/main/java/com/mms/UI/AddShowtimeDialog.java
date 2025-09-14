package com.mms.UI;

import javax.swing.*;
import java.awt.*;
import com.mms.dao.MovieDAO;
import com.mms.models.Movie;
import com.mms.dao.ShowtimeDAO;
import com.mms.models.Showtime;


public class AddShowtimeDialog extends JDialog {
    public AddShowtimeDialog(JFrame parent) {
        super(parent, "Add New Showtime", true);
        setSize(500, 500);
        setLocationRelativeTo(parent);
        getRootPane().setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(234, 224, 213)));       
        
        MovieDAO movieDAO = new MovieDAO();
        java.util.List<Movie> movies = movieDAO.getAllMovies();

        JComboBox<Movie> movieComboBox = new JComboBox<>();
        for (Movie movie : movies) {
            movieComboBox.addItem(movie);
        }
        JLabel movieLabel = new JLabel("Select Movie:");
        JLabel timeLabel = new JLabel("Time (HH:MM AM/PM):");
        JTextField hoursField = new JTextField(1);
        JTextField minutesField = new JTextField(1);
        JComboBox<String> amPmField = new JComboBox<>(new String[]{"AM", "PM"});

        JButton addButton = new JButton("Add Showtime");
        JButton cancelButton = new JButton("Cancel");
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.add(movieLabel);
        panel.add(movieComboBox);
        panel.add(timeLabel);
        panel.add(hoursField);
        panel.add(minutesField);
        panel.add(amPmField);
        panel.add(addButton);
        panel.add(cancelButton);
        add(panel);
        pack();
        addButton.addActionListener(e -> {
            Movie selectedMovie = (Movie) movieComboBox.getSelectedItem();
            String hours = hoursField.getText().trim();
            String minutes = minutesField.getText().trim();
            String amPm = (String) amPmField.getSelectedItem();

            if (selectedMovie == null || hours.isEmpty() || minutes.isEmpty() || amPm == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int hrs = Integer.parseInt(hours);
                int mins = Integer.parseInt(minutes);
                if (hrs < 1 || hrs > 12 || mins < 0 || mins > 59) {
                    JOptionPane.showMessageDialog(this, "Invalid time format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Showtime newShowtime = new Showtime();
                ShowtimeDAO showtimeDAO = new ShowtimeDAO();
                showtimeDAO.createShowtime(newShowtime);
                JOptionPane.showMessageDialog(this, "Showtime added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for hours and minutes.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            dispose();
        });
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
