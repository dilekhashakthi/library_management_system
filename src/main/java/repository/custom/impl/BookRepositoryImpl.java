package repository.custom.impl;

import db.DBConnection;
import model.Book;
import repository.custom.BookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    @Override
    public boolean save(Book book) throws SQLException {
        String SQL = "INSERT INTO library_books (bookID, ISBN, title, author, genre, availabilityStatus) VALUES (?,?,?,?,?,?)";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, book.getBookID());
        pstm.setString(2, book.getISBN());
        pstm.setString(3, book.getTitle());
        pstm.setString(4, book.getAuthor());
        pstm.setString(5, book.getGenre());
        pstm.setString(6, book.getAvailabilityStatus());
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean update(Book book) throws SQLException {
        String SQL = "UPDATE library_books SET ISBN=?, title=?, author=?, genre=?, availabilityStatus=? WHERE bookID=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, book.getISBN());
        pstm.setString(2, book.getTitle());
        pstm.setString(3, book.getAuthor());
        pstm.setString(4, book.getGenre());
        pstm.setString(5, book.getAvailabilityStatus());
        pstm.setString(6, book.getBookID());
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String SQL = "DELETE FROM library_books WHERE bookID=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, id);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public Book findById(String id) throws SQLException {
        String SQL = "SELECT * FROM library_books WHERE bookID=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, id);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return new Book(rs.getString("bookID"), rs.getString("ISBN"), rs.getString("title"),
                    rs.getString("author"), rs.getString("genre"), rs.getString("availabilityStatus"));
        }
        return null;
    }

    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM library_books");
        while (rs.next()) {
            books.add(new Book(rs.getString("bookID"), rs.getString("ISBN"), rs.getString("title"),
                    rs.getString("author"), rs.getString("genre"), rs.getString("availabilityStatus")));
        }
        return books;
    }

    @Override
    public List<Book> searchBooks(String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        String SQL = "SELECT * FROM library_books WHERE title LIKE ? OR author LIKE ? OR genre LIKE ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        String k = "%" + keyword + "%";
        pstm.setString(1, k);
        pstm.setString(2, k);
        pstm.setString(3, k);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            books.add(new Book(rs.getString("bookID"), rs.getString("ISBN"), rs.getString("title"),
                    rs.getString("author"), rs.getString("genre"), rs.getString("availabilityStatus")));
        }
        return books;
    }

    @Override
    public List<Book> findAvailableBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String SQL = "SELECT * FROM library_books WHERE availabilityStatus = 'Available'";
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            books.add(new Book(rs.getString("bookID"), rs.getString("ISBN"), rs.getString("title"),
                    rs.getString("author"), rs.getString("genre"), rs.getString("availabilityStatus")));
        }
        return books;
    }
}
