package com.bookstore.service;

import com.bookstore.dao.UserDAO;
import com.bookstore.dao.BookDAO;
import com.bookstore.model.User;
import com.bookstore.model.Book;
import java.util.List;

/**
 * Service layer for User operations
 * Contains business logic and validation
 */
public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Authenticate user login
     */
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        return userDAO.authenticateUser(username.trim(), password);
    }
    
    /**
     * Register a new user
     */
    public boolean register(User user) {
        validateUser(user);
        
        // Check if username already exists
        if (userDAO.usernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Check if email already exists
        if (userDAO.emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        return userDAO.createUser(user);
    }
    
    /**
     * Update user profile
     */
    public boolean updateProfile(User user) {
        validateUser(user);
        
        // Check if username is taken by another user
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        if (existingUser != null && existingUser.getId() != user.getId()) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Check if email is taken by another user
        User existingEmailUser = userDAO.getUserByEmail(user.getEmail());
        if (existingEmailUser != null && existingEmailUser.getId() != user.getId()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        return userDAO.updateUser(user);
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }
    
    /**
     * Get all users (admin only)
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    /**
     * Delete user
     */
    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }
    
    /**
     * Validate user data
     */
    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        if (user.getUsername().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        
        if (user.getRole() == null || (!user.getRole().equals("ADMIN") && !user.getRole().equals("CUSTOMER"))) {
            throw new IllegalArgumentException("Role must be ADMIN or CUSTOMER");
        }
    }
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
