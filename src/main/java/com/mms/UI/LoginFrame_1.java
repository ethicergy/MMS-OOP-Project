package com.mms.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.mms.dao.UserDAO;
import com.mms.models.User;
//import com.mms.UI.AdminDashboard_2;
import com.mms.UI.MovieSelection_3;
import com.mms.UI.AdminDashboard_2;

public class LoginFrame_1 extends JFrame {
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
        JPanel loginBox = new JPanel(new BorderLayout());
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
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(198, 172, 143));
        formPanel.setBorder(BorderFactory.createEmptyBorder(1, 80, 40, 80));

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
        usernameField.setPreferredSize(new Dimension(250, 35)); 
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
        passwordField.setPreferredSize(new Dimension(250, 35)); 
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
                JOptionPane.showMessageDialog(LoginFrame_1.this, "Open registration form here.");
            }
        });

        loginBox.add(formPanel, BorderLayout.CENTER);
        add(loginBox, new GridBagConstraints());
    }

    public static void main(String[] args) {
       new LoginFrame_1(); 
    }
}
