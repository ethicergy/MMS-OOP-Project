package com.mms.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.mms.controllers.UserController;
import com.mms.controllers.AuthenticationException;
import com.mms.models.User;
import com.mms.util.InputValidator;
import com.mms.util.Logger;

public class LoginFrame_1 extends JFrame {
    private JPanel loginBox;
    private JPanel formPanel;
    private JLabel messageLabel;
    UserController userController = new UserController();
    public LoginFrame_1() {
        setLayout(new GridBagLayout()); // centers loginBox
        setTitle("Login");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("title-logo.png").getImage());
        getContentPane().setBackground(new Color(234, 224, 213));
        setVisible(true);

        // Login box
        loginBox = new JPanel(new BorderLayout());
        loginBox.setPreferredSize(new Dimension(700, 500));
        loginBox.setBackground(new Color(198, 172, 143));
        loginBox.setBorder(BorderFactory.createLineBorder(new Color(94, 80, 63), 4));

        // Logo
        ImageIcon logo = new ImageIcon("title-logo.png");
        Image scaledImage = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        loginBox.add(logoLabel, BorderLayout.NORTH);

        // Form panel
        formPanel = new JPanel(new GridBagLayout());
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
    private void showLoginForm()
    {
        formPanel.removeAll();
        clearMessages();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(60, 80, 40, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fieldFont = new Font("SansSerif", Font.PLAIN, 18); 
        Font labelFont = new Font("SansSerif", Font.BOLD, 18);

        // Username row
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JTextField usernameField = new JTextField(15);
        usernameField.setFont(fieldFont);
        usernameField.setPreferredSize(new Dimension(150, 35));
        usernameField.setMinimumSize(new Dimension(150, 35));
        usernameField.setMaximumSize(new Dimension(150, 35)); 
        formPanel.add(usernameField, gbc);

        // Password row
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(fieldFont);
        passwordField.setPreferredSize(new Dimension(150, 35));
        passwordField.setMinimumSize(new Dimension(150, 35));
        passwordField.setMaximumSize(new Dimension(150, 35)); 
        formPanel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        loginBtn.setPreferredSize(new Dimension(200, 45));
        loginBtn.setBackground(new Color(34,51,59));
        loginBtn.setFocusable(false);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            // Use InputValidator for input checks
            if (InputValidator.isNullOrEmpty(username) || InputValidator.isNullOrEmpty(password)) {
                JOptionPane.showMessageDialog(LoginFrame_1.this, "Please enter both username and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                User user = userController.authenticate(username, password);
                JOptionPane.showMessageDialog(LoginFrame_1.this, "Login successful! Welcome, " + user.getName() + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                userController.rbacDirect(user, LoginFrame_1.this);
            } catch (AuthenticationException ex) {
                Logger.log(ex);
                JOptionPane.showMessageDialog(LoginFrame_1.this, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                Logger.log(ex);
                JOptionPane.showMessageDialog(LoginFrame_1.this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(loginBtn, gbc);

        // Register link (styled label)
        gbc.gridy = 3;
        JLabel registerLabel = new JLabel("<HTML><U>New user? Register here</U></HTML>");
        registerLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.setForeground(new Color(10,9,8));
        formPanel.add(registerLabel, gbc);

        // Add click event (just an example)
        registerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showRegistrationForm();
            }
        });

        loginBox.add(formPanel, BorderLayout.CENTER);
        add(loginBox, new GridBagConstraints());
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

            boolean success = userController.createUser(user);
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
