package service.custom;

import model.BorrowingRecords;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface BorrowingRecordsService extends SuperService {
    boolean addRecord(BorrowingRecords record) throws SQLException;
    boolean updateRecord(BorrowingRecords record) throws SQLException;
    boolean deleteRecord(String id) throws SQLException;
    BorrowingRecords getRecord(String id) throws SQLException;
    List<BorrowingRecords> getAllRecords() throws SQLException;
    List<BorrowingRecords> getRecordsByUser(String userId) throws SQLException;
    List<BorrowingRecords> getBorrowedBooks() throws SQLException;
    List<BorrowingRecords> getOverdueBooks() throws SQLException;
}
