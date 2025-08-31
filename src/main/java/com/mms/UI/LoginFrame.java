package com.mms.UI;

import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Container contentPane = getContentPane();
        contentPane.setBackground(Color.white);
        contentPane.setLayout(new BorderLayout());

        // --- Top Panel: App Title ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel appTitle = new JLabel("Movie Ticket Booking");
        appTitle.setFont(new Font("Arial", Font.BOLD, 26));
        appTitle.setForeground(new Color(0, 0, 0, 120)); // semi-transparent
        appTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
        topPanel.add(appTitle, BorderLayout.WEST);

        contentPane.add(topPanel, BorderLayout.NORTH);

        // --- Login Heading ---
        JLabel heading = new JLabel("Login", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 45));
        heading.setForeground(Color.black);
        heading.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField userField = new JTextField(15);
        userField.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JPasswordField passField = new JPasswordField(15);
        passField.setFont(new Font("Arial", Font.PLAIN, 18));

        // add username row
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(userField, gbc);

        // add password row
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passField, gbc);

        // add login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBackground(new Color(33, 182, 168));
        loginButton.setForeground(Color.white);
        loginButton.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        // --- Container to center heading + form ---
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BorderLayout());
        formContainer.setOpaque(false);
        formContainer.add(heading, BorderLayout.NORTH);
        formContainer.add(formPanel, BorderLayout.CENTER);

        // Add padding around form container
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        GridBagConstraints wrapperGbc = new GridBagConstraints();
        wrapperGbc.gridy = 0;
        wrapperGbc.weighty = 0.8; // pushes it slightly upwards instead of dead center
        wrapper.add(formContainer, wrapperGbc);

        contentPane.add(wrapper, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
