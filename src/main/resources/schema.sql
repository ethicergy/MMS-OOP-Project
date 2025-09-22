-- Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Movies Table
CREATE TABLE IF NOT EXISTS movies (
    movie_id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    duration INT NOT NULL, -- in minutes
    genre VARCHAR(50) NOT NULL,
    language VARCHAR(50) NOT NULL,
    certificate VARCHAR(10) NOT NULL,
    poster_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Showtimes Table
CREATE TABLE IF NOT EXISTS showtimes (
    showtime_id SERIAL PRIMARY KEY,
    movie_id INT REFERENCES movies(movie_id),
    date DATE NOT NULL,
    time TIME NOT NULL,
    screen_number INT NOT NULL
);

-- Seats Table
CREATE TABLE IF NOT EXISTS seats (
    seat_id SERIAL PRIMARY KEY,
    showtime_id INT REFERENCES showtimes(showtime_id),
    seat_row CHAR(1) NOT NULL,
    seat_col INT NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('available', 'booked'))
);

-- Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    showtime_id INT REFERENCES showtimes(showtime_id),
    seat_id INT REFERENCES seats(seat_id),
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    price DECIMAL(8,2) NOT NULL,
    is_paid BOOLEAN DEFAULT FALSE
);


