package com.mms.dao;

import com.mms.db.DBManager;
import com.mms.models.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;


public class MovieDAO {
	 
	public boolean createMovie(Movie movie) {
		String sql = "insert into movies(title,duration,genre,language,certificate,poster_url,created_at) values (?,?,?,?,?,?,NOW())";
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
		
			stmt.setString(1, movie.getTitle());
			stmt.setInt(2, movie.getDuration());
			stmt.setString(3, movie.getGenre());
			stmt.setString(4, movie.getLanguage());
			stmt.setString(5, movie.getCertificate());
			stmt.setString(6, movie.getPosterUrl());
			
			int rows = stmt.executeUpdate();
			return rows >0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
				
	}
	public Movie getMoviebyId(int id) {
		String sql = "select * from movies where movie_id = ?";
		
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				Movie movie = new Movie();
	            movie.setMovieId(rs.getInt("movie_id"));
	            movie.setTitle(rs.getString("title"));
	            movie.setDuration(rs.getInt("duration"));
	            movie.setGenre(rs.getString("genre"));
	            movie.setLanguage(rs.getString("language"));
	            movie.setCertificate(rs.getString("certificate"));
	            movie.setPosterUrl(rs.getString("poster_url"));
	            movie.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
	            return movie;
			}
			
			
		
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Movie> getAllMovies() {
		String sql = "select * from movies order by created_at";
		List<Movie> movies = new ArrayList<>();
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);ResultSet rs = stmt.executeQuery()){
			
			while(rs.next()) {
				Movie movie = new Movie();
				movie.setMovieId(rs.getInt("movie_id"));
	            movie.setTitle(rs.getString("title"));
	            movie.setDuration(rs.getInt("duration"));
	            movie.setGenre(rs.getString("genre"));
	            movie.setLanguage(rs.getString("language"));
	            movie.setCertificate(rs.getString("certificate"));
	            movie.setPosterUrl(rs.getString("poster_url"));
	            movie.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
	            
	            movies.add(movie);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return movies;
	}
	public boolean updateMovie(Movie movie) {
		String sql = "update movies set duration = ?, genre = ?, language = ?, certificate = ?, poster_url = ? where movie_id =?";
		try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setInt(1, movie.getDuration());
	        stmt.setString(2, movie.getGenre());
	        stmt.setString(3, movie.getLanguage());
	        stmt.setString(4, movie.getCertificate());
	        stmt.setString(5, movie.getPosterUrl());
	        stmt.setInt(6, movie.getMovieId());
	        
	        int rows = stmt.executeUpdate();
	        
	        if (rows == 0) {
	        	System.err.println("There was no fuckin movie like that in the first place");
	        	return false;
	        	
	        }
	        System.out.println("Good Shot Bhaiya, Movie updated");
	        return true;
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}
	return false;	
	}
	
	public boolean deleteMovie(int id) {
		String checkSql = "select count(*) from showtimes where movie_id = ?";
		String deleteSql = "delete from movies where movie_id = ?";
		try (DBManager db = new DBManager();
		         Connection conn = db.getConnection();
		         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
		
			checkStmt.setInt(1, id);
			ResultSet rs = checkStmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0) {
				System.err.println("There exists some data of the movie in showtime, check again please");
				return false;
			}
			
			try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
		         deleteStmt.setInt(1, id);
		         int rows = deleteStmt.executeUpdate();
		         
		         if(rows >0) {
		        	 System.out.println("Movie deleted");
		        	 return true;
		         } else {
		        	 System.err.println("No movie like that in db");
		        	 return false;
		        	 
		         }
		         }
		
		} catch (SQLException e) {
			System.err.println("Error: Couldn't delete");
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Movie> searchMovies(String title, String genre, String language, String certificate){
		
		String sql = "select * from movies where 1=1 "+
					 "AND (lower(title) like lower(?) or ? IS NULL) "+
					 "AND (genre = ? OR ? IS NULL) " +
					 "AND (language = ? or ? IS NULL) "+
					 "AND (certificate = ? OR ? IS NULL) "+
					 "ORDER BY created_at DESC";
		List <Movie> movies = new ArrayList<>();
		
		 try (DBManager db = new DBManager();
		         Connection conn = db.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(sql)) {
		 
			 if (title != null && !title.isBlank()) {
		            stmt.setString(1, "%" + title + "%");
		            stmt.setNull(2, java.sql.Types.VARCHAR);
		        } else {
		            stmt.setNull(1, java.sql.Types.VARCHAR);
		            stmt.setNull(2, java.sql.Types.VARCHAR);
		        }

		        if (genre != null && !genre.isBlank()) {
		            stmt.setString(3, genre);
		            stmt.setNull(4, java.sql.Types.VARCHAR);
		        } else {
		            stmt.setNull(3, java.sql.Types.VARCHAR);
		            stmt.setNull(4, java.sql.Types.VARCHAR);
		        }

		        if (language != null && !language.isBlank()) {
		            stmt.setString(5, language);
		            stmt.setNull(6, java.sql.Types.VARCHAR);
		        } else {
		            stmt.setNull(5, java.sql.Types.VARCHAR);
		            stmt.setNull(6, java.sql.Types.VARCHAR);
		        }

		    
		        if (certificate != null && !certificate.isBlank()) {
		            stmt.setString(7, certificate);
		            stmt.setNull(8, java.sql.Types.VARCHAR);
		        } else {
		            stmt.setNull(7, java.sql.Types.VARCHAR);
		            stmt.setNull(8, java.sql.Types.VARCHAR);
		        }
			 ResultSet rs = stmt.executeQuery();
			 
			 while (rs.next()) {
		            Movie movie = new Movie();
		            movie.setMovieId(rs.getInt("movie_id"));
		            movie.setTitle(rs.getString("title"));
		            movie.setDuration(rs.getInt("duration"));
		            movie.setGenre(rs.getString("genre"));
		            movie.setLanguage(rs.getString("language"));
		            movie.setCertificate(rs.getString("certificate"));
		            movie.setPosterUrl(rs.getString("poster_url"));
		            movie.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

		            movies.add(movie);
			 }
			 
		 
		 } catch (SQLException e) {
			 System.err.println("The search dint work");
			 e.printStackTrace();
		 }
		 return movies;
		 
		
	}
	
	
	
}
