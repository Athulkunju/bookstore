package com.bookstore.service;

import com.bookstore.dao.BookDAO;
import com.bookstore.model.Book;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Book operations
 * Contains business logic and validation
 */
public class BookService {
    private BookDAO bookDAO;
    
    public BookService() {
        this.bookDAO = new BookDAO();
    }
    
    /**
     * Add a new book
     */
    public boolean addBook(Book book) {
        validateBook(book);
        
        // Check if ISBN already exists
        if (bookDAO.isbnExists(book.getIsbn())) {
            throw new IllegalArgumentException("ISBN already exists");
        }
        
        return bookDAO.createBook(book);
    }
    
    /**
     * Update book information
     */
    public boolean updateBook(Book book) {
        validateBook(book);
        
        // Check if ISBN is taken by another book
        Book existingBook = bookDAO.getBookByIsbn(book.getIsbn());
        if (existingBook != null && existingBook.getId() != book.getId()) {
            throw new IllegalArgumentException("ISBN already exists");
        }
        
        return bookDAO.updateBook(book);
    }
    
    /**
     * Get book by ID
     */
    public Book getBookById(int id) {
        return bookDAO.getBookById(id);
    }
    
    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }
    
    /**
     * Search books by title
     */
    public List<Book> searchBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookDAO.searchBooksByTitle(title.trim());
    }
    
    /**
     * Search books by author
     */
    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookDAO.searchBooksByAuthor(author.trim());
    }
    
    /**
     * Get books by category
     */
    public List<Book> getBooksByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookDAO.getBooksByCategory(category.trim());
    }
    
    /**
     * Get all categories
     */
    public List<String> getAllCategories() {
        return bookDAO.getAllCategories();
    }
    
    /**
     * Delete book
     */
    public boolean deleteBook(int id) {
        return bookDAO.deleteBook(id);
    }
    
    /**
     * Update stock quantity
     */
    public boolean updateStock(int bookId, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        
        return bookDAO.updateStockQuantity(bookId, newQuantity);
    }
    
    /**
     * Reduce stock quantity (for orders)
     */
    public boolean reduceStock(int bookId, int quantity) {
        Book book = getBookById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        if (book.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + book.getStockQuantity());
        }
        
        int newQuantity = book.getStockQuantity() - quantity;
        return updateStock(bookId, newQuantity);
    }
    
    /**
     * Validate book data
     */
    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN is required");
        }
        
        if (!isValidISBN(book.getIsbn())) {
            throw new IllegalArgumentException("Invalid ISBN format");
        }
        
        if (book.getPrice() == null || book.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        
        if (book.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        
        if (book.getPublicationDate() != null && book.getPublicationDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Publication date cannot be in the future");
        }
    }
    
    /**
     * Validate ISBN format (basic validation)
     */
    private boolean isValidISBN(String isbn) {
        // Remove hyphens and spaces
        String cleanIsbn = isbn.replaceAll("[\\s-]", "");
        
        // Check if it's 10 or 13 digits
        return cleanIsbn.matches("^\\d{10}$") || cleanIsbn.matches("^\\d{13}$");
    }
}
