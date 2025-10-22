package com.bookstore.gui;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Dialog for adding/editing books
 */
public class BookDialog extends JDialog {
    private Book book;
    private BookService bookService;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField isbnField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTextField stockField;
    private JTextArea descriptionArea;
    private JTextField publisherField;
    private JTextField publicationDateField;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean isEditMode;
    
    public BookDialog(JFrame parent, Book book) {
        super(parent, book == null ? "Add New Book" : "Edit Book", true);
        this.book = book;
        this.bookService = new BookService();
        this.isEditMode = book != null;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initializeComponents() {
        titleField = new JTextField(30);
        authorField = new JTextField(30);
        isbnField = new JTextField(30);
        categoryField = new JTextField(30);
        priceField = new JTextField(30);
        stockField = new JTextField(30);
        descriptionArea = new JTextArea(5, 30);
        publisherField = new JTextField(30);
        publicationDateField = new JTextField(30);
        
        saveButton = new JButton(isEditMode ? "Update" : "Save");
        cancelButton = new JButton("Cancel");
        
        // Style components
        Font fieldFont = new Font("Arial", Font.PLAIN, 12);
        titleField.setFont(fieldFont);
        authorField.setFont(fieldFont);
        isbnField.setFont(fieldFont);
        categoryField.setFont(fieldFont);
        priceField.setFont(fieldFont);
        stockField.setFont(fieldFont);
        descriptionArea.setFont(fieldFont);
        publisherField.setFont(fieldFont);
        publicationDateField.setFont(fieldFont);
        
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        // Set button sizes
        saveButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.setPreferredSize(new Dimension(100, 30));
        
        // Set button colors
        saveButton.setBackground(new Color(34, 139, 34));
        saveButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(105, 105, 105));
        cancelButton.setForeground(Color.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(isEditMode ? "Edit Book Information" : "Add New Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(51, 102, 153));
        titlePanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Title:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(titleField, gbc);
        
        // Author
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Author:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(authorField, gbc);
        
        // ISBN
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("ISBN:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(isbnField, gbc);
        
        // Category
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Category:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(categoryField, gbc);
        
        // Price
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Price ($):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(priceField, gbc);
        
        // Stock Quantity
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Stock Quantity:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(stockField, gbc);
        
        // Publisher
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Publisher:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(publisherField, gbc);
        
        // Publication Date
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Publication Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(publicationDateField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        formPanel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBook();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void populateFields() {
        if (isEditMode && book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            isbnField.setText(book.getIsbn());
            categoryField.setText(book.getCategory());
            priceField.setText(book.getPrice().toString());
            stockField.setText(String.valueOf(book.getStockQuantity()));
            descriptionArea.setText(book.getDescription());
            publisherField.setText(book.getPublisher());
            if (book.getPublicationDate() != null) {
                publicationDateField.setText(book.getPublicationDate().toString());
            }
        }
    }
    
    private void saveBook() {
        try {
            // Validate required fields
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (authorField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Author is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (isbnField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ISBN is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (priceField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Price is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (stockField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Stock quantity is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Parse numeric fields
            BigDecimal price;
            int stockQuantity;
            
            try {
                price = new BigDecimal(priceField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid price format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                stockQuantity = Integer.parseInt(stockField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid stock quantity format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Parse publication date
            LocalDate publicationDate = null;
            if (!publicationDateField.getText().trim().isEmpty()) {
                try {
                    publicationDate = LocalDate.parse(publicationDateField.getText().trim());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Create or update book
            Book bookToSave;
            if (isEditMode) {
                bookToSave = book;
                bookToSave.setTitle(titleField.getText().trim());
                bookToSave.setAuthor(authorField.getText().trim());
                bookToSave.setIsbn(isbnField.getText().trim());
                bookToSave.setCategory(categoryField.getText().trim());
                bookToSave.setPrice(price);
                bookToSave.setStockQuantity(stockQuantity);
                bookToSave.setDescription(descriptionArea.getText().trim());
                bookToSave.setPublisher(publisherField.getText().trim());
                bookToSave.setPublicationDate(publicationDate);
                
                boolean success = bookService.updateBook(bookToSave);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update book.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                bookToSave = new Book(
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    isbnField.getText().trim(),
                    categoryField.getText().trim(),
                    price,
                    stockQuantity,
                    descriptionArea.getText().trim(),
                    publicationDate,
                    publisherField.getText().trim()
                );
                
                boolean success = bookService.addBook(bookToSave);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add book.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
