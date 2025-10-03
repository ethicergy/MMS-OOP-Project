package com.mms.dao;

import java.util.*;
import com.mms.db.DBManager;
import com.mms.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class UserDAO {
	public User getUserByUsername(String username) {
		String sql = "select * from users where name = ?";
		try (DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean createUser(User user) {
		String sql = "insert into users (name, email, password,role) values (?,?,?,?)";
	
	
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			
			
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getPassword());
			stmt.setString(4, user.getRole());
			
			int rows = stmt.executeUpdate();
			return rows != 0;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		return false;
		
	
	
	}
	public User getUserbyId(int id) {
		String sql = "select * from users where user_id = ?";
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				
				User user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				return user;
			}
	
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	
	return null;
	}
	public User getUserbyEmail(String email) {
		String sql = "select * from users where email = ?";
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, email);
			
			ResultSet rs = stmt.executeQuery();
			//there might be a need to implement some auto closing
			if(rs.next()) {
				
				User user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				return user;
			}
	
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	
	return null;
	}
	public List<User> getAllUsers() {
		String sql = "select * from users";
		List<User> users = new ArrayList<>();
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);ResultSet rs = stmt.executeQuery()){
			
			while(rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	
	}
	public boolean deleteUser(int id) {
		String sql = "delete from users where user_id = ?";
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			int rows = stmt.executeUpdate();
			return rows >0;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		}
	
	public User authenticateUser(String name, String password) {
		String sql = "select * from users where name = ? and password = ?";
		try (DBManager db = new DBManager();
			 Connection conn = db.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
