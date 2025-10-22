package com.bookstore.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Book model class representing a book in the store
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private BigDecimal price;
    private int stockQuantity;
    private String description;
    private LocalDate publicationDate;
    private String publisher;
    private boolean isActive;

    // Default constructor
    public Book() {}

    // Constructor with all fields
    public Book(int id, String title, String author, String isbn, String category,
                BigDecimal price, int stockQuantity, String description,
                LocalDate publicationDate, String publisher, boolean isActive) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.isActive = isActive;
    }

    // Constructor without ID (for new books)
    public Book(String title, String author, String isbn, String category,
                BigDecimal price, int stockQuantity, String description,
                LocalDate publicationDate, String publisher) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.isActive = true;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", publisher='" + publisher + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
