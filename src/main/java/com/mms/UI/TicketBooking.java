package com.mms.UI;

import javax.swing.*;
import java.awt.*;

public class TicketBooking {

    public static void main(String[] args) {
        JFrame frame = new JFrame("NOW SHOWING: Book Your Movie");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.getContentPane().setBackground(new Color(235, 224, 213)); // Light beige background
        frame.setLayout(null); // Absolute positioning

        // Title Label
        JLabel titleLabel = new JLabel("NOW SHOWING: Book Your Movie");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBounds(370, 20, 650, 30);
        frame.add(titleLabel);

        // Movie List Panel
        JPanel moviePanel = new JPanel();
        moviePanel.setBackground(new Color(188, 155, 103)); // Brownish background
        moviePanel.setLayout(null);  // Important: for absolute positioning inside panel
        moviePanel.setBounds(100, 70, 1000, 650); // Bigger panel to fit all movies
        moviePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(moviePanel);

        // Add movies with different vertical offsets
        int startY = 20;  // start 20 px from top of panel
        int verticalSpacing = 150; // spacing between movies

        addMovieToPanel(moviePanel, "Inception", "2h 28m", "English", "Inception.jpg", startY);
        addMovieToPanel(moviePanel, "Lokah: Chapter 1", "2h 29m", "Malayalam", "lokah Chapter 1.jpg", startY + verticalSpacing);
        addMovieToPanel(moviePanel, "Hridayapoorvam", "2h 31m", "Malayalam", "Hridayapoorvam.jpg", startY + 2 * verticalSpacing);
        addMovieToPanel(moviePanel, "F1: The Movie", "2h 35m", "English", "F1 The Movie.jpg", startY + 3 * verticalSpacing);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void addMovieToPanel(JPanel panel, String title, String duration, String language, String imagePath, int yOffset) {
        JLabel posterLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaled = icon.getImage().getScaledInstance(90, 110, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            posterLabel.setText("No Image");
        }
        posterLabel.setBounds(20, yOffset-20, 120, 160);
        panel.add(posterLabel);

        JLabel infoLabel = new JLabel(title + "    Duration: " + duration + "    Language: " + language);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        infoLabel.setBounds(160, yOffset, 800, 30); // Slightly lower than poster top
        panel.add(infoLabel);

        int buttonsY = yOffset + 40; // below poster
        String[] times = {"9:25 am", "1:00 pm", "4:00 pm", "11:47 pm"};
        int x = 160;
        for (String time : times) {
            JButton timeButton = new JButton(time);
            timeButton.setBounds(x, buttonsY, 100, 40);
            timeButton.setBackground(new Color(31, 47, 48));
            timeButton.setForeground(Color.WHITE);
            timeButton.setFont(new Font("Arial", Font.BOLD, 14));
            timeButton.setFocusPainted(false);
            panel.add(timeButton);
            x += 130;
        }
    }
}
        





