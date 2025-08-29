package com.mms.UI; 
import java.awt.Color;
import java.awt.Container;

import javax.swing.*;
import java.awt.*; 

public class LoginFrame extends JFrame{
	public LoginFrame() {
		
		setTitle("Login");
		setSize(800, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
		Container contentPane = getContentPane(); 
		contentPane.setBackground(Color.darkGray);
		contentPane.setLayout(new BorderLayout());	
		
		JLabel heading = new JLabel("Login"	, SwingConstants.CENTER);
		heading.setFont(new Font("Arial", Font.BOLD, 45));
		heading.setForeground(Color.white); 
		heading.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0)); 

		
		
		 contentPane.add(heading, BorderLayout.NORTH);
	}
}