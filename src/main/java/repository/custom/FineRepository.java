package repository.custom;

import model.Fine;
import repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public interface FineRepository extends CrudRepository<Fine, String> {
    List<Fine> findUnpaidFines() throws SQLException;
    boolean markAsPaid(String fineID) throws SQLException;
}
