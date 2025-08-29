package com.mms;

import com.mms.db.DBManager;
import com.mms.dao.UserDAO;
import com.mms.models.User;
import com.mms.UI.*; 

public class Main {
public static void main(String[] args){
	
	if(true) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			new LoginFrame().setVisible(true); 	
		});
		return; 
	}
	
	try(DBManager db = new DBManager()) {
	System.out.println("Shi working till now");
	
	
	} catch (Exception e) {
	e.printStackTrace();
	}
	
	UserDAO userDAO = new UserDAO();
	
	User newUser = new User("Protagonist", "tenet@gmail.com", "tenet", "user");
	boolean working = userDAO.createUser(newUser);
	
	if(working) {
		System.out.println("Shi working till now");
		
		
	}
	else {
		System.out.println("F Shi not working");

	}
	
	}
	}
