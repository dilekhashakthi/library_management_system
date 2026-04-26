package repository.custom;

import model.Book;
import repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public interface BookRepository extends CrudRepository<Book, String> {
    List<Book> searchBooks(String keyword) throws SQLException;
    List<Book> findAvailableBooks() throws SQLException;
}
