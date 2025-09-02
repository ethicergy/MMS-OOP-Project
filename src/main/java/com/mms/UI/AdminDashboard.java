package com.mms.UI;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.getContentPane().setBackground(new Color(235, 224, 213)); // Light beige background
        frame.setLayout(null); // Absolute positioning

        // Title Label
        JLabel titleLabel = new JLabel("ADMIN DASHBOARD");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBounds(650, 20, 300, 30);
        frame.add(titleLabel);

        // "Movie List" Label
        JLabel movieListLabel = new JLabel("Movie List");
        movieListLabel.setFont(new Font("Arial", Font.BOLD, 24));
        movieListLabel.setBounds(300, 70, 2000, 20);
        frame.add(movieListLabel);

        // Movie List Panel
        JPanel moviePanel = new JPanel();
        moviePanel.setBackground(new Color(188, 155, 103)); // Brownish background
        moviePanel.setBounds(300, 100, 750, 300); // X, Y, Width, Height
        moviePanel.setSize(1000, 500);
        moviePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(moviePanel);

        // Show frame
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
    }
}




