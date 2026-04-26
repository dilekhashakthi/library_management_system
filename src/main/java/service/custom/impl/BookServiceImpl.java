package service.custom.impl;

import model.Book;
import repository.DaoFactory;
import repository.custom.BookRepository;
import service.custom.BookService;
import util.DaoType;

import java.sql.SQLException;
import java.util.List;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository = DaoFactory.getInstance().getDao(DaoType.BOOK);

    @Override
    public boolean addBook(Book book) throws SQLException {
        return bookRepository.save(book);
    }

    @Override
    public boolean updateBook(Book book) throws SQLException {
        return bookRepository.update(book);
    }

    @Override
    public boolean deleteBook(String id) throws SQLException {
        return bookRepository.delete(id);
    }

    @Override
    public Book getBook(String id) throws SQLException {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> getAllBooks() throws SQLException {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> searchBooks(String keyword) throws SQLException {
        return bookRepository.searchBooks(keyword);
    }

    @Override
    public List<Book> getAvailableBooks() throws SQLException {
        return bookRepository.findAvailableBooks();
    }
}
