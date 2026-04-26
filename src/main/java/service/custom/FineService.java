package service.custom;

import model.Fine;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface FineService extends SuperService {
    boolean addFine(Fine fine) throws SQLException;
    boolean updateFine(Fine fine) throws SQLException;
    boolean deleteFine(String id) throws SQLException;
    Fine getFine(String id) throws SQLException;
    List<Fine> getAllFines() throws SQLException;
    List<Fine> getUnpaidFines() throws SQLException;
    boolean payFine(String fineID) throws SQLException;
}
