package com.mms.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.mms.dao.UserDAO;
import com.mms.models.User;
//import com.mms.UI.AdminDashboard_2;
import com.mms.UI.MovieSelection_3;
import com.mms.UI.AdminDashboard_2;
import javax.swing.Timer;

public class LoginFrame_1 extends JFrame {
    private JPanel loginBox;
    private JPanel formPanel;
    private JLabel messageLabel;

    public LoginFrame_1() {
        setLayout(new GridBagLayout());
        setTitle("Login");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("title-logo.png").getImage());
        getContentPane().setBackground(new Color(234, 224, 213));

        loginBox = new JPanel(new BorderLayout());
        loginBox.setPreferredSize(new Dimension(700, 500));
        loginBox.setBackground(new Color(198, 172, 143));
        loginBox.setBorder(BorderFactory.createLineBorder(new Color(94, 80, 63), 4));

        ImageIcon logo = new ImageIcon("title-logo.png");
        Image scaledImage = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        loginBox.add(logoLabel, BorderLayout.NORTH);

        formPanel = new JPanel();
        formPanel.setBackground(new Color(198, 172, 143));
        loginBox.add(formPanel, BorderLayout.CENTER);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(Color.RED);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginBox.add(messageLabel, BorderLayout.SOUTH);

        add(loginBox, new GridBagConstraints());
        showLoginForm();
        setVisible(true);
    }

    private void showLoginForm() {
        formPanel.removeAll();
        clearMessages();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(60, 80, 40, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fieldFont = new Font("SansSerif", Font.PLAIN, 18);
        Font labelFont = new Font("SansSerif", Font.BOLD, 18);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.LINE_END;
        JLabel userLabel = new JLabel("Email:");
        userLabel.setFont(labelFont);
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        JTextField usernameField = new JTextField();
        usernameField.setFont(fieldFont);
        usernameField.setPreferredSize(new Dimension(150, 35));
        usernameField.setMinimumSize(new Dimension(150, 35));
        usernameField.setMaximumSize(new Dimension(150, 35));
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.LINE_END;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(fieldFont);
        passwordField.setPreferredSize(new Dimension(150, 35));
        passwordField.setMinimumSize(new Dimension(150, 35));
        passwordField.setMaximumSize(new Dimension(150, 35));
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        loginBtn.setPreferredSize(new Dimension(180, 45));
        loginBtn.setMinimumSize(new Dimension(180, 45));
        loginBtn.setMaximumSize(new Dimension(180, 45));
        loginBtn.setBackground(new Color(34, 51, 59));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            UserDAO userDAO = new UserDAO();
            User user = userDAO.authenticateUser(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(LoginFrame_1.this, "Login successful! Welcome, " + user.getName() + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Must route to the correct dashboard based on role
                if (user.getRole().equals("admin")) {
                    new AdminDashboard_2().setVisible(true);
                } else {
                    new MovieSelection_3().setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame_1.this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(loginBtn, gbc);

        gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel registerLabel = new JLabel("<HTML><U>New user? Register here</U></HTML>");
        registerLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        registerLabel.setForeground(new Color(10, 9, 8));
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(registerLabel, gbc);

        registerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showRegistrationForm();
            }
        });

        formPanel.revalidate();
        formPanel.repaint();
    }

    private void showRegistrationForm() {
        formPanel.removeAll();
        clearMessages();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 20, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fieldFont = new Font("SansSerif", Font.PLAIN, 18);
        Font labelFont = new Font("SansSerif", Font.BOLD, 18);

        // --- Name Row ---
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        JTextField nameField = new JTextField();
        nameField.setFont(fieldFont);
        nameField.setPreferredSize(new Dimension(180, 35));
        nameField.setMinimumSize(new Dimension(180, 35));
        nameField.setMaximumSize(new Dimension(180, 35));
        formPanel.add(nameField, gbc);

        // --- Email Row ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        JTextField emailField = new JTextField();
        emailField.setFont(fieldFont);
        emailField.setPreferredSize(new Dimension(180, 35));
        emailField.setMinimumSize(new Dimension(180, 35));
        emailField.setMaximumSize(new Dimension(180, 35));
        formPanel.add(emailField, gbc);

        // --- Password Row ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        JPasswordField passField = new JPasswordField();
        passField.setFont(fieldFont);
        passField.setPreferredSize(new Dimension(180, 35));
        passField.setMinimumSize(new Dimension(180, 35));
        passField.setMaximumSize(new Dimension(180, 35));
        formPanel.add(passField, gbc);

        // --- Confirm Password Row ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(labelFont);
        formPanel.add(confirmLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        JPasswordField confirmPassField = new JPasswordField();
        confirmPassField.setFont(fieldFont);
        confirmPassField.setPreferredSize(new Dimension(180, 35));
        confirmPassField.setMinimumSize(new Dimension(180, 35));
        confirmPassField.setMaximumSize(new Dimension(180, 35));
        formPanel.add(confirmPassField, gbc);

        // --- Register Button ---
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 15, 5, 15);
        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        registerBtn.setPreferredSize(new Dimension(180, 45));
        registerBtn.setMinimumSize(new Dimension(180, 45));
        registerBtn.setMaximumSize(new Dimension(180, 45));
        registerBtn.setBackground(new Color(34, 51, 59));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusable(false);
        formPanel.add(registerBtn, gbc);

        // --- Back to Login Link ---
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 15, 15, 15);
        // **THIS IS THE FIX: Using non-breaking spaces (&nbsp;) to prevent wrapping**
        JLabel backLabel = new JLabel("<HTML><U>Back&nbsp;to&nbsp;Login</U></HTML>");
        backLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backLabel.setForeground(new Color(10, 9, 8));
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(backLabel, gbc);

        registerBtn.addActionListener(e -> handleRegistration(nameField.getText().trim(),
                emailField.getText().trim(),
                new String(passField.getPassword()),
                new String(confirmPassField.getPassword())));

        backLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showLoginForm();
            }
        });

        formPanel.revalidate();
        formPanel.repaint();
    }

    private void handleRegistration(String name, String email, String pass, String confirmPass) {
        clearMessages();
        if (!validateRegistrationInput(name, email, pass, confirmPass)) return;

        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(pass);
            user.setRole("user");

            boolean success = UserDAO.createUser(user);
            if (success) {
                showSuccess("Registration successful! Redirecting...");
                Timer t = new Timer(2000, e -> showLoginForm());
                t.setRepeats(false);
                t.start();
            } else {
                showError("Email already exists.");
            }
        } catch (Exception ex) {
            showError("Registration failed: " + ex.getMessage());
        }
    }

    private boolean validateRegistrationInput(String name, String email, String pass, String confirmPass) {
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            showError("All fields are required.");
            return false;
        }
        if (name.length() > 80) {
            showError("Name cannot exceed 80 characters.");
            return false;
        }
        if (!isValidEmail(email)) {
            showError("Invalid email format.");
            return false;
        }
        if (pass.length() < 8) {
            showError("Password must be at least 8 characters.");
            return false;
        }
        if (!pass.equals(confirmPass)) {
            showError("Passwords do not match.");
            return false;
        }
        return true;
    }

    private void showError(String msg) {
        messageLabel.setForeground(Color.RED);
        messageLabel.setText(msg);
    }



    private void showSuccess(String msg) {
        messageLabel.setForeground(new Color(0, 128, 0));
        messageLabel.setText(msg);
    }

    private void clearMessages() {
        messageLabel.setText("");
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    public static void main(String[] args) {
        new LoginFrame_1();
    }
}