package com.bookstore.gui;

import com.bookstore.model.Book;
import com.bookstore.model.User;
import com.bookstore.service.BookService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Main window for the Book Store application
 */
public class BookStoreMainWindow extends JFrame {
    private User currentUser;
    private BookService bookService;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private JButton searchButton;
    private JButton addBookButton;
    private JButton editBookButton;
    private JButton deleteBookButton;
    private JButton refreshButton;
    private JButton logoutButton;
    private JLabel userLabel;
    private JLabel statusLabel;
    
    public BookStoreMainWindow(User user) {
        this.currentUser = user;
        this.bookService = new BookService();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadBooks();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Book Store Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }
    
    private void initializeComponents() {
        // Table setup
        String[] columnNames = {"ID", "Title", "Author", "ISBN", "Category", "Price", "Stock", "Publisher"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setAutoCreateRowSorter(true); // enable sorting
        bookTable.setRowHeight(24);
        bookTable.setFillsViewportHeight(true);
        bookTable.setShowGrid(false);
        // zebra striping
        bookTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(250, 250, 250) : Color.WHITE);
                }
                return c;
            }
        });
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getTableHeader().setReorderingAllowed(false);
        bookTable.getTableHeader().setFont(bookTable.getTableHeader().getFont().deriveFont(Font.BOLD));
        
        // Set column widths
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Title
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Author
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(120); // ISBN
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Category
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Price
        bookTable.getColumnModel().getColumn(6).setPreferredWidth(60);  // Stock
        bookTable.getColumnModel().getColumn(7).setPreferredWidth(150); // Publisher
        
        // Search components
        searchField = new JTextField(20);
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("All Categories");
        
        // Buttons
        searchButton = new JButton("Search");
        addBookButton = new JButton("Add Book");
        editBookButton = new JButton("Edit Book");
        deleteBookButton = new JButton("Delete Book");
        refreshButton = new JButton("Refresh");
        logoutButton = new JButton("Logout");
        
        // User label
        userLabel = new JLabel("Welcome, " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Status label
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        
        // Tooltips
        searchField.setToolTipText("Type title or author and press Enter");
        searchButton.setToolTipText("Search by title/author and category");
        refreshButton.setToolTipText("Reload all books");
        addBookButton.setToolTipText("Add a new book");
        editBookButton.setToolTipText("Edit selected book");
        deleteBookButton.setToolTipText("Delete selected book");
        logoutButton.setToolTipText("Log out of the application");
        
        // Style buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        searchButton.setFont(buttonFont);
        addBookButton.setFont(buttonFont);
        editBookButton.setFont(buttonFont);
        deleteBookButton.setFont(buttonFont);
        refreshButton.setFont(buttonFont);
        logoutButton.setFont(buttonFont);
        
        // Set button colors
        addBookButton.setBackground(new Color(34, 139, 34));
        addBookButton.setForeground(Color.WHITE);
        editBookButton.setBackground(new Color(255, 140, 0));
        editBookButton.setForeground(Color.WHITE);
        deleteBookButton.setBackground(new Color(220, 20, 60));
        deleteBookButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(105, 105, 105));
        logoutButton.setForeground(Color.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem miRefresh = new JMenuItem("Refresh");
        JMenuItem miExit = new JMenuItem("Exit");
        fileMenu.add(miRefresh);
        fileMenu.addSeparator();
        fileMenu.add(miExit);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem miAbout = new JMenuItem("About");
        helpMenu.add(miAbout);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        
        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton tbAdd = new JButton("Add");
        JButton tbEdit = new JButton("Edit");
        JButton tbDelete = new JButton("Delete");
        JButton tbRefresh = new JButton("Refresh");
        toolBar.add(tbAdd);
        toolBar.add(tbEdit);
        toolBar.add(tbDelete);
        toolBar.addSeparator();
        toolBar.add(tbRefresh);
        
        // bind toolbar actions
        tbAdd.addActionListener(e -> showAddBookDialog());
        tbEdit.addActionListener(e -> editSelectedBook());
        tbDelete.addActionListener(e -> deleteSelectedBook());
        tbRefresh.addActionListener(e -> loadBooks());
        
        // Top panel with user info and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel userBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userBar.add(userLabel);
        topPanel.add(userBar, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);
        topPanel.add(toolBar, BorderLayout.SOUTH);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Books"));
        searchPanel.add(new JLabel("Title/Author:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Category:"));
        searchPanel.add(categoryComboBox);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Book Management"));
        buttonPanel.add(addBookButton);
        buttonPanel.add(editBookButton);
        buttonPanel.add(deleteBookButton);
        
        // Control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(searchPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Books"));
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(controlPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Add all panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230,230,230)));
        statusBar.add(statusLabel, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);
        
        // Load categories
        loadCategories();
    }
    
    private void setupEventHandlers() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
                statusLabel.setText("Search completed at " + java.time.LocalTime.now().withNano(0));
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBooks();
                statusLabel.setText("List refreshed at " + java.time.LocalTime.now().withNano(0));
            }
        });
        
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddBookDialog();
                statusLabel.setText("Add book dialog opened");
            }
        });
        
        editBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedBook();
                statusLabel.setText("Edit book dialog opened");
            }
        });
        
        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedBook();
                statusLabel.setText("Delete requested");
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
                statusLabel.setText("Logging out...");
            }
        });
        
        // Enter key search
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        
        // Category change search
        categoryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
    }
    
    private void loadBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            updateTable(books);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadCategories() {
        try {
            List<String> categories = bookService.getAllCategories();
            categoryComboBox.removeAllItems();
            categoryComboBox.addItem("All Categories");
            for (String category : categories) {
                categoryComboBox.addItem(category);
            }
        } catch (Exception e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }
    }
    
    private void updateTable(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book book : books) {
            Object[] row = {
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCategory(),
                "$" + book.getPrice().toString(),
                book.getStockQuantity(),
                book.getPublisher()
            };
            tableModel.addRow(row);
        }
    }
    
    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        
        try {
            List<Book> books;
            
            if (!searchTerm.isEmpty()) {
                // Search by title or author
                List<Book> titleResults = bookService.searchBooksByTitle(searchTerm);
                List<Book> authorResults = bookService.searchBooksByAuthor(searchTerm);
                
                // Combine results and remove duplicates
                books = titleResults;
                for (Book book : authorResults) {
                    if (!books.contains(book)) {
                        books.add(book);
                    }
                }
            } else {
                books = bookService.getAllBooks();
            }
            
            // Filter by category if not "All Categories"
            if (!"All Categories".equals(selectedCategory)) {
                books = books.stream()
                    .filter(book -> selectedCategory.equals(book.getCategory()))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            updateTable(books);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching books: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showAddBookDialog() {
        BookDialog dialog = new BookDialog(this, null);
        dialog.setVisible(true);
        loadBooks(); // Refresh the table
        loadCategories(); // Refresh categories
    }
    
    private void editSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int bookId = (Integer) tableModel.getValueAt(selectedRow, 0);
        try {
            Book book = bookService.getBookById(bookId);
            if (book != null) {
                BookDialog dialog = new BookDialog(this, book);
                dialog.setVisible(true);
                loadBooks(); // Refresh the table
                loadCategories(); // Refresh categories
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading book: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int bookId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String bookTitle = (String) tableModel.getValueAt(selectedRow, 1);
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete the book '" + bookTitle + "'?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                boolean success = bookService.deleteBook(bookId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully.", 
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadBooks(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete book.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting book: " + e.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginWindow().setVisible(true);
            });
        }
    }
}
