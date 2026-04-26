package service.custom;

import model.Book;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface BookService extends SuperService {
    boolean addBook(Book book) throws SQLException;
    boolean updateBook(Book book) throws SQLException;
    boolean deleteBook(String id) throws SQLException;
    Book getBook(String id) throws SQLException;
    List<Book> getAllBooks() throws SQLException;
    List<Book> searchBooks(String keyword) throws SQLException;
    List<Book> getAvailableBooks() throws SQLException;
}
