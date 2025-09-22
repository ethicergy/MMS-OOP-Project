package com.mms.dao;
import com.mms.db.DBManager;
import com.mms.models.Showtime;

import java.sql.*;
import java.util.*;
public class ShowtimeDAO {
	
	public boolean createShowtime(Showtime s) {
		String sql = "insert into showtimes(movie_id, date, time, screen_number) values (?,?,?,?)";
		
		try(DBManager db = new DBManager();
				Connection conn = db.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, s.getMovieId());
			stmt.setDate(2, java.sql.Date.valueOf(s.getDate()));
			stmt.setTime(3, java.sql.Time.valueOf(s.getTime()));
			stmt.setInt(4,  s.getScreenNumber());
			
			int rows = stmt.executeUpdate();
			return rows >0;
			
			
		} catch(SQLException e) {
			System.err.println("Error creating showtime: "+e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateShowtimeTime(int showtimeId, java.time.LocalTime newTime) {
		String sql = "update showtimes set time = ? where showtime_id = ?";

		try(DBManager db = new DBManager();
				Connection conn = db.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setTime(1, java.sql.Time.valueOf(newTime));
			stmt.setInt(2, showtimeId);
			int rows = stmt.executeUpdate();
			return rows > 0;
		} catch(SQLException e) {
			System.err.println("Error updating showtime: "+e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public Showtime getShowtimeByMovieIdAndDateTime(int movieId, java.time.LocalDate date, java.time.LocalTime time) {
		String sql = "Select * from showtimes where movie_id = ? and date = ? and time = ?";
		
		try(DBManager db = new DBManager();
				Connection conn = db.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, movieId);
			stmt.setDate(2, java.sql.Date.valueOf(date));
			stmt.setTime(3, java.sql.Time.valueOf(time));
			try(ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Showtime s = mapRowtoShowtime(rs);
					return s;
					
				}
			}
		} catch (SQLException e) {
			System.err.println("Error getching showtime: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public Showtime getShowtimeById(int id) {
		String sql = "Select * from showtimes where showtime_id = ?";
		
		try(DBManager db = new DBManager();
				Connection conn = db.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			try(ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Showtime s = mapRowtoShowtime(rs);
					return s;
					
				}
			}
		} catch (SQLException e) {
			System.err.println("Error getching showtime: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	public List<Showtime> getShowtimesByMovieId(int movieId) {
		String sql = "select * from showtimes where movie_id = ? order by date, time";
		List<Showtime> list = new ArrayList<>();
		
		try (DBManager db = new DBManager();
	             Connection conn = db.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, movieId);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    list.add(mapRowtoShowtime(rs));
	                }
	            }

	        } catch (SQLException e) {
	            System.err.println("Error fetching showtimes for movie " + movieId + ": " + e.getMessage());
	            e.printStackTrace();
	        }
	        return list;
	    }
	
	private Showtime mapRowtoShowtime(ResultSet rs) throws SQLException {
		Showtime s = new Showtime();
		s.setShowtimeId(rs.getInt("showtime_id"));
		s.setMovieId(rs.getInt("movie_id"));
		
		java.sql.Date d = rs.getDate("date");
		if(d != null) s.setDate(d.toLocalDate());
		
		java.sql.Time t = rs.getTime("time");
		if(t != null) s.setTime(t.toLocalTime());
		
		s.setScreenNumber(rs.getInt("screen_number"));
		return s;
		
	}
	
	public boolean deleteShowtime(int showtimeId) {
		String checkBookingsSql = "select count(*) from bookings where showtime_id = ?";
		String checkSeatsSql = "select count(*) from seats where showtime_id = ?";
		String deleteSql = "delete from showtimes where showtime_id = ?";
		
		try(DBManager db = new DBManager();
				Connection conn = db.getConnection();
				PreparedStatement checkBookingsStmt = conn.prepareStatement(checkBookingsSql)){
			checkBookingsStmt.setInt(1, showtimeId);
			try(ResultSet rs = checkBookingsStmt.executeQuery()) {
				if(rs.next() && rs.getInt(1)>0) {
					System.err.println("Cannot delete showtime, records exist in bookings");
					return false;
				}
			}
				
				
		try(PreparedStatement checkSeatsStmt = conn.prepareStatement(checkSeatsSql)){
			checkSeatsStmt.setInt(1, showtimeId);
			try(ResultSet rs2 = checkSeatsStmt.executeQuery()) {
				if(rs2.next() && rs2.getInt(1)>0) {
					System.err.println("Cannot delete showtime records exist in Seats");
					return false;
				}
				}
			}
		try(PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)){
			deleteStmt.setInt(1, showtimeId);
			int rows = deleteStmt.executeUpdate();
			if (rows >0) {
				System.out.println("Showtime deleted");
				return true;
			} else {
				System.err.println("No showtime found with id");
				return false;
				
			}
		}
		} catch (SQLException e) {
			System.err.println("Error deleting showtime ");
			e.printStackTrace();
			return false;
			
		}
	}

}
