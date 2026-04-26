package repository.custom;

import model.BorrowingRecords;
import repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public interface BorrowingRecordsRepository extends CrudRepository<BorrowingRecords, String> {
    List<BorrowingRecords> findByUserId(String userId) throws SQLException;
    List<BorrowingRecords> findBorrowedBooks() throws SQLException;
    List<BorrowingRecords> findOverdueBooks() throws SQLException;
    boolean updateBookAvailability(String bookId, String status) throws SQLException;
}
