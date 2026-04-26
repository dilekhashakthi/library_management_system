package service.custom.impl;

import model.BorrowingRecords;
import repository.DaoFactory;
import repository.custom.BorrowingRecordsRepository;
import service.custom.BorrowingRecordsService;
import util.DaoType;

import java.sql.SQLException;
import java.util.List;

public class BorrowingRecordsServiceImpl implements BorrowingRecordsService {

    private final BorrowingRecordsRepository repo = DaoFactory.getInstance().getDao(DaoType.BORROWING_RECORDS);

    @Override
    public boolean addRecord(BorrowingRecords record) throws SQLException {
        return repo.save(record);
    }

    @Override
    public boolean updateRecord(BorrowingRecords record) throws SQLException {
        return repo.update(record);
    }

    @Override
    public boolean deleteRecord(String id) throws SQLException {
        return repo.delete(id);
    }

    @Override
    public BorrowingRecords getRecord(String id) throws SQLException {
        return repo.findById(id);
    }

    @Override
    public List<BorrowingRecords> getAllRecords() throws SQLException {
        return repo.findAll();
    }

    @Override
    public List<BorrowingRecords> getRecordsByUser(String userId) throws SQLException {
        return repo.findByUserId(userId);
    }

    @Override
    public List<BorrowingRecords> getBorrowedBooks() throws SQLException {
        return repo.findBorrowedBooks();
    }

    @Override
    public List<BorrowingRecords> getOverdueBooks() throws SQLException {
        return repo.findOverdueBooks();
    }
}
