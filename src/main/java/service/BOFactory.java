package service;

import service.custom.BookService;
import service.custom.BorrowingRecordsService;
import service.custom.FineService;
import service.custom.UserService;
import service.custom.impl.BookServiceImpl;
import service.custom.impl.BorrowingRecordsServiceImpl;
import service.custom.impl.FineServiceImpl;
import service.custom.impl.UserServiceImpl;
import util.BOType;

public class BOFactory {

    private static BOFactory instance;

    private BOFactory() {}

    public static BOFactory getInstance() {
        return instance == null ? instance = new BOFactory() : instance;
    }

    public <T extends SuperService> T getBO(BOType type) {
        switch (type) {
            case BOOK: return (T) new BookServiceImpl();
            case USER: return (T) new UserServiceImpl();
            case BORROWING_RECORDS: return (T) new BorrowingRecordsServiceImpl();
            case FINE: return (T) new FineServiceImpl();
            default: return null;
        }
    }
}
