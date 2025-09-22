package com.mms.models;

public class Seat {
	
	private int seatId;
	private int showtimeId;
	private String seatRow;
	private int seatCol;
	private String status;
	
	public Seat() {}
	
	public Seat(int showtimeId, String seatRow, int seatCol, String status) {
		this.showtimeId = showtimeId;
		this.seatRow = seatRow;
		this.seatCol = seatCol;
		this.status = status;
		
	}
	
	public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(int showtimeId) {
        this.showtimeId = showtimeId;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatCol() {
        return seatCol;
    }

    public void setSeatCol(int seatCol) {
        this.seatCol = seatCol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return String.format("Seat{id=%d, showtimeId=%d, position=%s%d, status='%s'}", 
                seatId, showtimeId, seatRow, seatCol, status);
    }
}
