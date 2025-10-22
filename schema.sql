-- Book Store Database Schema
-- This script creates the complete database structure for the book store application

-- Create database (uncomment if creating a new database)
-- CREATE DATABASE bookstore_db;
-- USE bookstore_db;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role ENUM('ADMIN', 'CUSTOMER') DEFAULT 'CUSTOMER',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create books table
CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    category VARCHAR(100),
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    description TEXT,
    publication_date DATE,
    publisher VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create orders table
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    shipping_address TEXT,
    payment_method VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create order_items table
CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_category ON books(category);
CREATE INDEX idx_books_isbn ON books(isbn);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_book_id ON order_items(book_id);

-- Insert sample data

-- Insert sample users
INSERT INTO users (username, password, email, first_name, last_name, role) VALUES
('admin', 'admin123', 'admin@bookstore.com', 'Admin', 'User', 'ADMIN'),
('john_doe', 'password123', 'john.doe@email.com', 'John', 'Doe', 'CUSTOMER'),
('jane_smith', 'password123', 'jane.smith@email.com', 'Jane', 'Smith', 'CUSTOMER'),
('bob_wilson', 'password123', 'bob.wilson@email.com', 'Bob', 'Wilson', 'CUSTOMER');

-- Insert sample books
INSERT INTO books (title, author, isbn, category, price, stock_quantity, description, publication_date, publisher) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'Fiction', 12.99, 50, 'A classic American novel set in the Jazz Age.', '1925-04-10', 'Scribner'),
('To Kill a Mockingbird', 'Harper Lee', '9780061120084', 'Fiction', 14.99, 30, 'A gripping tale of racial injustice and childhood innocence.', '1960-07-11', 'J.B. Lippincott & Co.'),
('1984', 'George Orwell', '9780451524935', 'Dystopian Fiction', 13.99, 40, 'A dystopian social science fiction novel.', '1949-06-08', 'Secker & Warburg'),
('Pride and Prejudice', 'Jane Austen', '9780141439518', 'Romance', 11.99, 25, 'A romantic novel of manners.', '1813-01-28', 'T. Egerton'),
('The Catcher in the Rye', 'J.D. Salinger', '9780316769174', 'Fiction', 15.99, 35, 'A coming-of-age story.', '1951-07-16', 'Little, Brown and Company'),
('Lord of the Flies', 'William Golding', '9780571056866', 'Fiction', 12.99, 20, 'A story about British boys stranded on an island.', '1954-09-17', 'Faber and Faber'),
('The Hobbit', 'J.R.R. Tolkien', '9780547928227', 'Fantasy', 16.99, 45, 'A fantasy novel about a hobbit''s adventure.', '1937-09-21', 'George Allen & Unwin'),
('Harry Potter and the Philosopher''s Stone', 'J.K. Rowling', '9780747532699', 'Fantasy', 18.99, 60, 'The first book in the Harry Potter series.', '1997-06-26', 'Bloomsbury'),
('The Da Vinci Code', 'Dan Brown', '9780307474278', 'Mystery', 14.99, 30, 'A mystery thriller novel.', '2003-03-18', 'Doubleday'),
('The Alchemist', 'Paulo Coelho', '9780061122415', 'Fiction', 13.99, 25, 'A philosophical novel about a young shepherd.', '1988-01-01', 'HarperCollins');

-- Insert sample orders
INSERT INTO orders (user_id, total_amount, status, shipping_address, payment_method) VALUES
(2, 26.98, 'DELIVERED', '123 Main St, Anytown, USA', 'Credit Card'),
(3, 41.97, 'SHIPPED', '456 Oak Ave, Somewhere, USA', 'PayPal'),
(4, 15.99, 'PENDING', '789 Pine St, Elsewhere, USA', 'Credit Card');

-- Insert sample order items
INSERT INTO order_items (order_id, book_id, quantity, unit_price, subtotal) VALUES
(1, 1, 1, 12.99, 12.99),
(1, 2, 1, 14.99, 14.99),
(2, 3, 1, 13.99, 13.99),
(2, 4, 1, 11.99, 11.99),
(2, 5, 1, 15.99, 15.99),
(3, 6, 1, 15.99, 15.99);

-- Display success message
SELECT 'Database schema created successfully!' AS message;
SELECT 'Sample data inserted successfully!' AS message;
