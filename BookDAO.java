package com.bookstore.dao;

import com.bookstore.database.DatabaseConnection;
import com.bookstore.model.Book;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Book operations
 */
public class BookDAO {
    private DatabaseConnection dbConnection;
    
    public BookDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Create a new book
     */
    public boolean createBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, category, price, stock_quantity, description, publication_date, publisher) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getCategory());
            stmt.setBigDecimal(5, book.getPrice());
            stmt.setInt(6, book.getStockQuantity());
            stmt.setString(7, book.getDescription());
            stmt.setDate(8, book.getPublicationDate() != null ? Date.valueOf(book.getPublicationDate()) : null);
            stmt.setString(9, book.getPublisher());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating book: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get book by ID
     */
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ? AND is_active = TRUE";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting book by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get book by ISBN
     */
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ? AND is_active = TRUE";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting book by ISBN: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update book information
     */
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, category = ?, price = ?, stock_quantity = ?, description = ?, publication_date = ?, publisher = ?, is_active = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getCategory());
            stmt.setBigDecimal(5, book.getPrice());
            stmt.setInt(6, book.getStockQuantity());
            stmt.setString(7, book.getDescription());
            stmt.setDate(8, book.getPublicationDate() != null ? Date.valueOf(book.getPublicationDate()) : null);
            stmt.setString(9, book.getPublisher());
            stmt.setBoolean(10, book.isActive());
            stmt.setInt(11, book.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete book (soft delete by setting is_active to false)
     */
    public boolean deleteBook(int id) {
        String sql = "UPDATE books SET is_active = FALSE WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE is_active = TRUE ORDER BY title";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all books: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Search books by title
     */
    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? AND is_active = TRUE ORDER BY title";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + title + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching books by title: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Search books by author
     */
    public List<Book> searchBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE author LIKE ? AND is_active = TRUE ORDER BY title";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + author + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching books by author: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Get books by category
     */
    public List<Book> getBooksByCategory(String category) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE category = ? AND is_active = TRUE ORDER BY title";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
            }
            }
        } catch (SQLException e) {
            System.err.println("Error getting books by category: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Get all categories
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM books WHERE category IS NOT NULL AND is_active = TRUE ORDER BY category";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all categories: " + e.getMessage());
        }
        return categories;
    }
    
    /**
     * Update stock quantity
     */
    public boolean updateStockQuantity(int bookId, int newQuantity) {
        String sql = "UPDATE books SET stock_quantity = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, bookId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating stock quantity: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Check if ISBN exists
     */
    public boolean isbnExists(String isbn) {
        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking ISBN existence: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Map ResultSet to Book object
     */
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setCategory(rs.getString("category"));
        book.setPrice(rs.getBigDecimal("price"));
        book.setStockQuantity(rs.getInt("stock_quantity"));
        book.setDescription(rs.getString("description"));
        
        Date pubDate = rs.getDate("publication_date");
        if (pubDate != null) {
            book.setPublicationDate(pubDate.toLocalDate());
        }
        
        book.setPublisher(rs.getString("publisher"));
        book.setActive(rs.getBoolean("is_active"));
        return book;
    }
}
