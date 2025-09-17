package com.mms.dao;

import java.util.*;
import com.mms.db.DBManager;
import com.mms.models.Bookings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.math.BigDecimal;

public class BookingDAO {
	
	public boolean createBooking(Bookings booking) {
		String sql = "insert into bookings (user_id, showtime_id, seat_id, booking_time, price, is_paid) values (?,?,?,?,?,?)";
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setInt(1, booking.getUserId());
			stmt.setInt(2, booking.getShowtimeId());
			stmt.setInt(3, booking.getSeatId());
			stmt.setObject(4, booking.getBookingTime());
			stmt.setBigDecimal(5, booking.getPrice());
			stmt.setBoolean(6, booking.isPaid());
			
			int rows = stmt.executeUpdate();
			return rows != 0;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Bookings getBookingById(int id) {
		String sql = "select * from bookings where booking_id = ?";
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				Bookings booking = new Bookings();
				booking.setBookingId(rs.getInt("booking_id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setShowtimeId(rs.getInt("showtime_id"));
				booking.setSeatId(rs.getInt("seat_id"));
				booking.setBookingTime(rs.getTimestamp("booking_time").toLocalDateTime());
				booking.setPrice(rs.getBigDecimal("price"));
				booking.setPaid(rs.getBoolean("is_paid"));
				return booking;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Bookings> getAllBookings() {
		String sql = "select * from bookings order by booking_time desc";
		List<Bookings> bookings = new ArrayList<>();
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
			
			while(rs.next()) {
				Bookings booking = new Bookings();
				booking.setBookingId(rs.getInt("booking_id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setShowtimeId(rs.getInt("showtime_id"));
				booking.setSeatId(rs.getInt("seat_id"));
				booking.setBookingTime(rs.getTimestamp("booking_time").toLocalDateTime());
				booking.setPrice(rs.getBigDecimal("price"));
				booking.setPaid(rs.getBoolean("is_paid"));
				
				bookings.add(booking);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookings;
	}
	
	public List<Bookings> getBookingsByUserId(int userId) {
		String sql = "select * from bookings where user_id = ? order by booking_time desc";
		List<Bookings> bookings = new ArrayList<>();
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Bookings booking = new Bookings();
				booking.setBookingId(rs.getInt("booking_id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setShowtimeId(rs.getInt("showtime_id"));
				booking.setSeatId(rs.getInt("seat_id"));
				booking.setBookingTime(rs.getTimestamp("booking_time").toLocalDateTime());
				booking.setPrice(rs.getBigDecimal("price"));
				booking.setPaid(rs.getBoolean("is_paid"));
				
				bookings.add(booking);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookings;
	}
	
	public List<Bookings> getBookingsByShowtimeId(int showtimeId) {
		String sql = "select * from bookings where showtime_id = ? order by booking_time desc";
		List<Bookings> bookings = new ArrayList<>();
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, showtimeId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Bookings booking = new Bookings();
				booking.setBookingId(rs.getInt("booking_id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setShowtimeId(rs.getInt("showtime_id"));
				booking.setSeatId(rs.getInt("seat_id"));
				booking.setBookingTime(rs.getTimestamp("booking_time").toLocalDateTime());
				booking.setPrice(rs.getBigDecimal("price"));
				booking.setPaid(rs.getBoolean("is_paid"));
				
				bookings.add(booking);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookings;
	}
	
	public boolean updateBookingPaymentStatus(int bookingId, boolean isPaid) {
		String sql = "update bookings set is_paid = ? where booking_id = ?";
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setBoolean(1, isPaid);
			stmt.setInt(2, bookingId);
			
			int rows = stmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteBooking(int id) {
		String sql = "delete from bookings where booking_id = ?";
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			int rows = stmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean cancelBooking(int bookingId) {
		// This method cancels a booking by deleting it
		// In a real system, you might want to keep the record and mark it as cancelled
		return deleteBooking(bookingId);
	}
	
	public BigDecimal getTotalRevenueByShowtime(int showtimeId) {
		String sql = "select sum(price) as total_revenue from bookings where showtime_id = ? and is_paid = true";
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, showtimeId);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				return rs.getBigDecimal("total_revenue");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BigDecimal.ZERO;
	}
	
	public int getBookedSeatsCountForShowtime(int showtimeId) {
		String sql = "select count(*) as booked_count from bookings where showtime_id = ?";
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, showtimeId);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("booked_count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean isSeatBookedForShowtime(int showtimeId, int seatId) {
		String sql = "select count(*) as booking_count from bookings where showtime_id = ? and seat_id = ?";
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, showtimeId);
			stmt.setInt(2, seatId);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("booking_count") > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
