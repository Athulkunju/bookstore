# Book Store Application - Complete Setup Guide

## Overview
This is a complete Book Store Management System built with Java Swing for the frontend, Java backend with JDBC for database operations, and MySQL database. The application provides user authentication, book management, and order tracking capabilities.

## Prerequisites

### 1. Java Development Kit (JDK)
- **Required Version**: JDK 8 or higher
- **Download**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.java.net/)
- **Installation**: Follow the installer instructions for your operating system
- **Verification**: Open command prompt/terminal and run:
  ```bash
  java -version
  javac -version
  ```

### 2. MySQL Database Server
- **Required Version**: MySQL 5.7 or higher (MySQL 8.0 recommended)
- **Download**: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
- **Installation**: 
  - Windows: Download MySQL Installer and follow the setup wizard
  - macOS: Use Homebrew: `brew install mysql`
  - Linux: Use package manager (e.g., `sudo apt install mysql-server`)

### 3. MySQL JDBC Driver
- **Download**: [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
- **Version**: 8.0.x or higher
- **File**: `mysql-connector-java-8.0.x.jar`

### 4. IDE (Optional but Recommended)
- **IntelliJ IDEA**: [Download](https://www.jetbrains.com/idea/download/)
- **Eclipse**: [Download](https://www.eclipse.org/downloads/)
- **VS Code**: [Download](https://code.visualstudio.com/) with Java extensions

## Project Structure

```
bookstore/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── bookstore/
│                   ├── model/          # Data models
│                   │   ├── User.java
│                   │   ├── Book.java
│                   │   ├── Order.java
│                   │   └── OrderItem.java
│                   ├── dao/            # Data Access Objects
│                   │   ├── UserDAO.java
│                   │   └── BookDAO.java
│                   ├── service/        # Business logic layer
│                   │   ├── UserService.java
│                   │   └── BookService.java
│                   ├── database/       # Database utilities
│                   │   └── DatabaseConnection.java
│                   └── gui/           # Swing GUI components
│                       ├── LoginWindow.java
│                       ├── RegisterDialog.java
│                       ├── BookStoreMainWindow.java
│                       └── BookDialog.java
├── database/
│   └── schema.sql                     # Database schema
├── database.properties               # Database configuration
├── lib/                              # External libraries
│   └── mysql-connector-java-8.0.x.jar
└── README.md                         # This file
```

## Installation Steps

### Step 1: Download and Setup MySQL

1. **Install MySQL Server**:
   - Download and install MySQL Community Server
   - During installation, set a root password (remember this!)
   - Make sure MySQL service is running

2. **Create Database**:
   ```sql
   mysql -u root -p
   CREATE DATABASE bookstore_db;
   USE bookstore_db;
   ```

3. **Run Schema Script**:
   ```sql
   SOURCE database/schema.sql;
   ```

### Step 2: Configure Database Connection

1. **Edit `database.properties`**:
   ```properties
   db.url=jdbc:mysql://localhost:3306/bookstore_db
   db.username=root
   db.password=your_mysql_password
   db.driver=com.mysql.cj.jdbc.Driver
   ```

2. **Replace `your_mysql_password`** with your actual MySQL root password

### Step 3: Setup Java Project

1. **Create Project Directory**:
   ```bash
   mkdir bookstore
   cd bookstore
   ```

2. **Create Directory Structure**:
   ```bash
   mkdir -p src/main/java/com/bookstore/{model,dao,service,database,gui}
   mkdir database
   mkdir lib
   ```

3. **Download MySQL JDBC Driver**:
   - Download `mysql-connector-java-8.0.x.jar` from MySQL website
   - Place it in the `lib/` directory

### Step 4: Compile and Run

#### Option A: Command Line (Manual)

1. **Compile Java Files**:
   ```bash
   javac -cp "lib/mysql-connector-java-8.0.x.jar" -d . src/main/java/com/bookstore/**/*.java
   ```

2. **Run Application**:
   ```bash
   java -cp ".:lib/mysql-connector-java-8.0.x.jar" com.bookstore.gui.LoginWindow
   ```

#### Option B: Using IDE

1. **Import Project**:
   - Open your IDE (IntelliJ IDEA, Eclipse, etc.)
   - Import the project folder
   - Add `mysql-connector-java-8.0.x.jar` to project libraries

2. **Run Application**:
   - Right-click on `LoginWindow.java`
   - Select "Run" or "Debug"

## Configuration

### Database Configuration
The application uses a `database.properties` file for configuration:

```properties
# Database URL - modify host, port, and database name as needed
db.url=jdbc:mysql://localhost:3306/bookstore_db

# Database credentials
db.username=root
db.password=your_password

# JDBC Driver class
db.driver=com.mysql.cj.jdbc.Driver
```

### Sample Data
The schema includes sample data:
- **Admin User**: username: `admin`, password: `admin123`
- **Customer Users**: `john_doe`, `jane_smith`, `bob_wilson` (password: `password123`)
- **Sample Books**: 10 books with different categories

## Features

### User Management
- **Login System**: Secure authentication with username/password
- **User Registration**: New users can register with validation
- **Role-based Access**: Admin and Customer roles
- **User Profile Management**: Update user information

### Book Management
- **Add Books**: Add new books with complete information
- **Edit Books**: Modify existing book details
- **Delete Books**: Soft delete books (set inactive)
- **Search Books**: Search by title, author, or category
- **Stock Management**: Track and update inventory levels

### Order Management
- **Order Creation**: Create orders with multiple books
- **Order Tracking**: Track order status
- **Order History**: View past orders

## Troubleshooting

### Common Issues

1. **Database Connection Failed**:
   - Check if MySQL service is running
   - Verify database credentials in `database.properties`
   - Ensure database `bookstore_db` exists

2. **ClassNotFoundException for MySQL Driver**:
   - Ensure `mysql-connector-java-8.0.x.jar` is in classpath
   - Check if JAR file is in `lib/` directory

3. **Compilation Errors**:
   - Ensure JDK 8+ is installed
   - Check Java file paths and package declarations
   - Verify all dependencies are available

4. **GUI Not Displaying**:
   - Check if display is available (for headless systems)
   - Ensure Swing is properly initialized

### Database Issues

1. **Table Already Exists Error**:
   ```sql
   DROP DATABASE bookstore_db;
   CREATE DATABASE bookstore_db;
   USE bookstore_db;
   SOURCE database/schema.sql;
   ```

2. **Permission Denied**:
   - Ensure MySQL user has proper permissions
   - Grant privileges: `GRANT ALL PRIVILEGES ON bookstore_db.* TO 'root'@'localhost';`

## Development

### Adding New Features

1. **New Model Classes**: Add to `com.bookstore.model` package
2. **Database Operations**: Create DAO classes in `com.bookstore.dao`
3. **Business Logic**: Add service classes in `com.bookstore.service`
4. **GUI Components**: Create Swing components in `com.bookstore.gui`

### Code Structure

- **Model Layer**: Data transfer objects with getters/setters
- **DAO Layer**: Database access with SQL operations
- **Service Layer**: Business logic and validation
- **GUI Layer**: User interface components

## Security Considerations

1. **Password Storage**: Currently stored in plain text (consider hashing)
2. **SQL Injection**: Uses PreparedStatements for protection
3. **Input Validation**: Client and server-side validation
4. **Access Control**: Role-based permissions

## Performance Optimization

1. **Database Indexes**: Already created for common queries
2. **Connection Pooling**: Can be implemented for better performance
3. **Caching**: Consider implementing for frequently accessed data

## Future Enhancements

1. **Password Hashing**: Implement secure password storage
2. **Order Management GUI**: Complete order management interface
3. **Reporting**: Add sales and inventory reports
4. **Backup/Restore**: Database backup functionality
5. **Multi-language Support**: Internationalization
6. **Web Interface**: Convert to web application

## Support

For issues or questions:
1. Check this documentation first
2. Verify all prerequisites are met
3. Check database connectivity
4. Review error messages carefully

## License

This project is for educational purposes. Feel free to modify and extend as needed.
