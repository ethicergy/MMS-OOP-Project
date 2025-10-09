package com.mms.controllers;

import com.mms.dao.UserDAO;
import com.mms.models.User;
import com.mms.UI.AdminDashboard_2;
import com.mms.UI.MovieSelection_3;

public class UserController extends BaseController {
    private UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }



    /**
     * Authenticates a user by username and password.
     * Throws AuthenticationException if login fails.
     */
    public User authenticate(String username, String password) throws AuthenticationException {
        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            throw new AuthenticationException("Invalid username or password");
        }
    }
    /**
     * Directs user to the appropriate dashboard based on their role.
     */
    public void rbacDirect(User user, javax.swing.JFrame currentFrame) {
        if (user.getRole().equals("admin")) {
            new AdminDashboard_2().setVisible(true);
        } else {
            new MovieSelection_3().setVisible(true);
        }
        currentFrame.dispose();
    }

    public boolean createUser(User user){
        
        return userDAO.createUser(user);
    }

    // You can add more user-related business logic here
}
