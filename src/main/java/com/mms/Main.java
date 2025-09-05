package com.mms;

import com.mms.db.DBManager;
import com.mms.dao.UserDAO;
import com.mms.models.User;
import com.mms.UI.LoginFrame_1;   // explicitly import LoginFrame
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // --- Database test ---
        try (DBManager db = new DBManager()) {
            System.out.println("DB connection working till now");
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserDAO userDAO = new UserDAO();
        User newUser = new User("Protagonist", "tenet@gmail.com", "tenet", "user");
        boolean working = userDAO.createUser(newUser);

        if (working) {
            System.out.println("User creation working");
        } else {
            System.out.println("User creation failed");
        }

        // --- Launch Login UI ---
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
