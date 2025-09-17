package com.mms.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bookings {
	
	private int bookingId;
	private int userId;
	private int showtimeId;
	private int seatId;
	private LocalDateTime bookingTime;
	private BigDecimal price;
	private boolean isPaid;
	
	public Bookings() {}
	
	public Bookings(int bookingId, int userId, int showtimeId, int seatId, LocalDateTime bookingTime, BigDecimal price, boolean isPaid) {
		this.bookingId = bookingId;
		this.userId = userId;
		this.showtimeId = showtimeId;
		this.seatId = seatId;
		this.bookingTime = bookingTime;
		this.price = price;
		this.isPaid = isPaid;
	}
	
	public Bookings(int userId, int showtimeId, int seatId, BigDecimal price) {
		this.userId = userId;
		this.showtimeId = showtimeId;
		this.seatId = seatId;
		this.price = price;
		this.bookingTime = LocalDateTime.now();
		this.isPaid = false;
	}
	
	public int getBookingId() { return bookingId; }
	public void setBookingId(int bookingId) { this.bookingId = bookingId; }
	
	public int getUserId() { return userId; }
	public void setUserId(int userId) { this.userId = userId; }
	
	public int getShowtimeId() { return showtimeId; }
	public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
	
	public int getSeatId() { return seatId; }
	public void setSeatId(int seatId) { this.seatId = seatId; }
	
	public LocalDateTime getBookingTime() { return bookingTime; }
	public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
	
	public BigDecimal getPrice() { return price; }
	public void setPrice(BigDecimal price) { this.price = price; }
	
	public boolean isPaid() { return isPaid; }
	public void setPaid(boolean isPaid) { this.isPaid = isPaid; }
	
	@Override
	public String toString() {
		return String.format("Booking{id=%d, userId=%d, showtimeId=%d, seatId=%d, price=%.2f, paid=%b}", 
				bookingId, userId, showtimeId, seatId, price, isPaid);
	}
}
