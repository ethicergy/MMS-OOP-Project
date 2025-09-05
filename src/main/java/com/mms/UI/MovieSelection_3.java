package com.mms.UI;

import javax.swing.*;
import java.awt.*;

public class MovieSelection_3 {

    public MovieSelection_3() {
        JFrame frame = new JFrame("NOW SHOWING: Book Your Movie");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setIconImage(new ImageIcon("title-logo.png").getImage());

        // Background color
        Color bgColor = new Color(234, 224, 213);
        frame.getContentPane().setBackground(bgColor);
        frame.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("NOW SHOWING: Book Your Movie", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0)); // spacing below title
        frame.add(titleLabel, BorderLayout.NORTH);

        // Movie list panel
        JPanel movieListPanel = new JPanel();
        movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.Y_AXIS));
        movieListPanel.setBackground(bgColor);

        // Alternate row colors
        Color rowColor1 = new Color(234, 224, 213);   // matches background
        Color rowColor2 = new Color(198, 172, 143);   // updated lighter brown

        movieListPanel.add(createMovieRow("Inception", "2h 28m", "English", "Inception.jpg", rowColor1));
        movieListPanel.add(Box.createVerticalStrut(8));
        movieListPanel.add(createMovieRow("Lokah: Chapter 1", "2h 29m", "Malayalam", "lokah Chapter 1.jpg", rowColor2));
        movieListPanel.add(Box.createVerticalStrut(8));
        movieListPanel.add(createMovieRow("Hridayapoorvam", "2h 31m", "Malayalam", "Hridayapoorvam.jpg", rowColor1));
        movieListPanel.add(Box.createVerticalStrut(8));
        movieListPanel.add(createMovieRow("F1: The Movie", "2h 35m", "English", "F1 The Movie.jpg", rowColor2));

        // Container with margin
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(bgColor);
        container.setBorder(BorderFactory.createEmptyBorder(40, 100, 20, 100));
        container.add(movieListPanel, BorderLayout.CENTER);

        frame.add(container, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createMovieRow(String title, String duration, String language, String imagePath, Color bgColor) {
        JPanel rowPanel = new JPanel(new BorderLayout(20, 0));
        rowPanel.setBackground(bgColor);
        rowPanel.setPreferredSize(new Dimension(880, 140));   // width & height
        rowPanel.setMaximumSize(new Dimension(880, 140));
        rowPanel.setBorder(BorderFactory.createLineBorder(new Color(34, 51, 59), 2));

        // Poster
        JLabel posterLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaled = icon.getImage().getScaledInstance(90, 120, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            posterLabel.setText("No Image");
        }
        posterLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rowPanel.add(posterLabel, BorderLayout.WEST);

        // Details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(bgColor);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel durationLabel = new JLabel("Duration: " + duration);
        durationLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel languageLabel = new JLabel("Language: " + language);
        languageLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        detailsPanel.add(titleLabel);
        detailsPanel.add(Box.createVerticalStrut(6));
        detailsPanel.add(durationLabel);
        detailsPanel.add(Box.createVerticalStrut(6));
        detailsPanel.add(languageLabel);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        rowPanel.add(detailsPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 40));
        buttonPanel.setBackground(bgColor);

        String[] times = {"9:25 am", "1:00 pm", "4:00 pm", "11:47 pm"};
        for (String time : times) {
            JButton timeButton = new JButton(time);
            timeButton.setPreferredSize(new Dimension(95, 35));
            timeButton.setBackground(new Color(34, 51, 59));
            timeButton.setForeground(Color.WHITE);
            timeButton.setFont(new Font("Arial", Font.BOLD, 13));
            timeButton.setFocusPainted(false);
            buttonPanel.add(timeButton);
        }

        rowPanel.add(buttonPanel, BorderLayout.EAST);

        return rowPanel;
    }

    // Main method only invokes constructor
    public static void main(String[] args) {
        new MovieSelection_3();
    }
}
