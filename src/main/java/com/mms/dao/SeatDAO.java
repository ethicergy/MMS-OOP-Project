package com.mms.dao;

import com.mms.db.DBManager;
import com.mms.models.Seat;

import java.sql.*;
import java.util.*;

public class SeatDAO {

    public Seat getSeatById(int seatId) {
        String sql = "SELECT seat_id, showtime_id, seat_row, seat_col, status FROM seats WHERE seat_id = ?";
        try (DBManager db = new DBManager();
             Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seatId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSeat(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching seat by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Seat getSeatByShowtimeRowCol(int showtimeId, String row, int col) {
        String sql = "SELECT seat_id, showtime_id, seat_row, seat_col, status FROM seats WHERE showtime_id = ? AND seat_row = ? AND seat_col = ?";
        try (DBManager db = new DBManager();
             Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, showtimeId);
            ps.setString(2, row);
            ps.setInt(3, col);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSeat(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching seat by showtime/row/col: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Seat> getSeatsByShowtime(int showtimeId) {
        String sql = "SELECT seat_id, showtime_id, seat_row, seat_col, status FROM seats WHERE showtime_id = ? ORDER BY seat_row, seat_col";
        List<Seat> list = new ArrayList<>();
        try (DBManager db = new DBManager();
             Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, showtimeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToSeat(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching seats for showtime " + showtimeId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Seat> getAvailableSeats(int showtimeId) {
        String sql = "SELECT seat_id, showtime_id, seat_row, seat_col, status FROM seats WHERE showtime_id = ? AND status = 'available' ORDER BY seat_row, seat_col";
        List<Seat> list = new ArrayList<>();
        try (DBManager db = new DBManager();
             Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, showtimeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToSeat(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching available seats for showtime " + showtimeId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public boolean createSeat(Seat seat) {
        String sql = "INSERT INTO seats (showtime_id, seat_row, seat_col, status) VALUES (?, ?, ?, ?)";
        try (DBManager db = new DBManager();
             Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, seat.getShowtimeId());
            ps.setString(2, seat.getSeatRow());
            ps.setInt(3, seat.getSeatCol());
            ps.setString(4, seat.getStatus() == null ? "available" : seat.getStatus());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        seat.setSeatId(keys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating seat: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean createSeatsForShowtime(int showtimeId, List<String> rows, int colsPerRow) {
        String sql = "INSERT INTO seats (showtime_id, seat_row, seat_col, status) VALUES (?, ?, ?, 'available')";
        DBManager db = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            db = new DBManager();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);

            for (String r : rows) {
                for (int c = 1; c <= colsPerRow; c++) {
                    ps.setInt(1, showtimeId);
                    ps.setString(2, r);
                    ps.setInt(3, c);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error creating seats for showtime: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (ps != null) {
                try { ps.close(); } catch (SQLException ignored) {}
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
            if (db != null) {
                try { db.close(); } catch (Exception ignored) {}
            }
        }
        return false;
    }

    public boolean updateSeatStatus(int seatId, String status) {
        String sql = "UPDATE seats SET status = ? WHERE seat_id = ?";
        try (DBManager db = new DBManager();
             Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, seatId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating seat status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private Seat mapRowToSeat(ResultSet rs) throws SQLException {
        Seat s = new Seat();
        s.setSeatId(rs.getInt("seat_id"));
        s.setShowtimeId(rs.getInt("showtime_id"));
        s.setSeatRow(rs.getString("seat_row"));
        s.setSeatCol(rs.getInt("seat_col"));
        s.setStatus(rs.getString("status"));
        return s;
    }

    public boolean deleteSeatsByShowtime(int showtimeId) {
        String sql = "DELETE FROM seats WHERE showtime_id = ?";
        try (DBManager db = new DBManager();
             Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, showtimeId);
            int rows = ps.executeUpdate();
            System.out.println("Deleted " + rows + " seats for showtime " + showtimeId);
            return true;

        } catch (SQLException e) {
            System.err.println("Error deleting seats for showtime " + showtimeId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public Seat getSeatForUpdate(Connection conn, int seatId) throws SQLException {
        String sql = "SELECT seat_id, showtime_id, seat_row, seat_col, status FROM seats WHERE seat_id = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSeat(rs);
                }
            }
        }
        return null;
    }

    public Seat getSeatForUpdate(Connection conn, int showtimeId, String row, int col) throws SQLException {
        String sql = "SELECT seat_id, showtime_id, seat_row, seat_col, status FROM seats WHERE showtime_id = ? AND seat_row = ? AND seat_col = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, showtimeId);
            ps.setString(2, row);
            ps.setInt(3, col);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSeat(rs);
                }
            }
        }
        return null;
    }

    public int updateSeatStatus(Connection conn, int seatId, String newStatus) throws SQLException {
        String sql = "UPDATE seats SET status = ? WHERE seat_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, seatId);
            return ps.executeUpdate();
        }
    }

    public int updateSeatStatusIfCurrent(Connection conn, int seatId, String newStatus, String expectedStatus) throws SQLException {
        String sql = "UPDATE seats SET status = ? WHERE seat_id = ? AND status = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, seatId);
            ps.setString(3, expectedStatus);
            return ps.executeUpdate();
        }
    }

    public List<Seat> getSeatsForUpdate(Connection conn, List<Integer> seatIds) throws SQLException {
        if (seatIds == null || seatIds.isEmpty()) return new ArrayList<>();
        List<Integer> ids = new ArrayList<>(seatIds);
        Collections.sort(ids);
        List<Seat> result = new ArrayList<>();
        String sql = "SELECT seat_id, showtime_id, seat_row, seat_col, status FROM seats WHERE seat_id = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Integer id : ids) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        result.add(mapRowToSeat(rs));
                    } else {
                        result.add(null);
                    }
                }
            }
        }
        return result;
    }

    public int updateSeatStatusBatch(Connection conn, Map<Integer, String> seatIdToStatus) throws SQLException {
        if (seatIdToStatus == null || seatIdToStatus.isEmpty()) return 0;
        Map<Integer, String> ordered = new LinkedHashMap<>();
        seatIdToStatus.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> ordered.put(e.getKey(), e.getValue()));
        String sql = "UPDATE seats SET status = ? WHERE seat_id = ?";
        int total = 0;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Map.Entry<Integer, String> e : ordered.entrySet()) {
                ps.setString(1, e.getValue());
                ps.setInt(2, e.getKey());
                ps.addBatch();
            }
            int[] counts = ps.executeBatch();
            for (int c : counts) {
                if (c != Statement.SUCCESS_NO_INFO && c != Statement.EXECUTE_FAILED) total += c;
                else if (c == Statement.SUCCESS_NO_INFO) total += 1;
            }
        }
        return total;
    }
    
    /**
     * Ensures seats exist for a showtime with the SeatSelection_4 layout
     * Creates seats matching the theater layout: A1-A16, B1-B16, etc.
     */
    public void ensureSeatsForShowtime(int showtimeId) {
        // Check if seats already exist
        String checkSql = "SELECT COUNT(*) as seat_count FROM seats WHERE showtime_id = ?";
        
        try(DBManager db = new DBManager(); Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(checkSql)) {
            stmt.setInt(1, showtimeId);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next() && rs.getInt("seat_count") > 0) {
                return; // Seats already exist
            }
            
            // Create seats matching SeatSelection_4 layout
            List<String> rows = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L");
            createSeatsForShowtime(showtimeId, rows, 16);
            
        } catch (SQLException e) {
            System.err.println("Error checking/creating seats for showtime " + showtimeId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
