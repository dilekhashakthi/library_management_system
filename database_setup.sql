CREATE DATABASE IF NOT EXISTS librarymanagementsystem;
USE librarymanagementsystem;

CREATE TABLE IF NOT EXISTS logs (
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS library_books (
    bookID VARCHAR(50) PRIMARY KEY,
    ISBN VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    availabilityStatus VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS library_users (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_information VARCHAR(100) NOT NULL,
    membership_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS library_borrow_records (
    RecordID VARCHAR(50) PRIMARY KEY,
    UserID VARCHAR(50) NOT NULL,
    BookID VARCHAR(50) NOT NULL,
    BorrowDate DATE NOT NULL,
    ReturnDate DATE,
    Fine INT DEFAULT 0,
    FOREIGN KEY (UserID) REFERENCES library_users(id),
    FOREIGN KEY (BookID) REFERENCES library_books(bookID)
);

CREATE TABLE IF NOT EXISTS library_fines (
    fineID VARCHAR(50) PRIMARY KEY,
    recordID VARCHAR(50) NOT NULL,
    userID VARCHAR(50) NOT NULL,
    fineAmount INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Unpaid',
    paidDate DATE,
    FOREIGN KEY (recordID) REFERENCES library_borrow_records(RecordID),
    FOREIGN KEY (userID) REFERENCES library_users(id)
);
