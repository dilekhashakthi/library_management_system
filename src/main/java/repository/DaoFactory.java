package repository;

import repository.custom.BookRepository;
import repository.custom.BorrowingRecordsRepository;
import repository.custom.FineRepository;
import repository.custom.UserRepository;
import repository.custom.impl.BookRepositoryImpl;
import repository.custom.impl.BorrowingRecordsRepositoryImpl;
import repository.custom.impl.FineRepositoryImpl;
import repository.custom.impl.UserRepositoryImpl;
import util.DaoType;

public class DaoFactory {

    private static DaoFactory instance;

    private DaoFactory() {}

    public static DaoFactory getInstance() {
        return instance == null ? instance = new DaoFactory() : instance;
    }

    public <T extends SuperRepository> T getDao(DaoType type) {
        switch (type) {
            case BOOK: return (T) new BookRepositoryImpl();
            case USER: return (T) new UserRepositoryImpl();
            case BORROWING_RECORDS: return (T) new BorrowingRecordsRepositoryImpl();
            case FINE: return (T) new FineRepositoryImpl();
            default: return null;
        }
    }
}
