package service.custom.impl;

import model.Fine;
import repository.DaoFactory;
import repository.custom.FineRepository;
import service.custom.FineService;
import util.DaoType;

import java.sql.SQLException;
import java.util.List;

public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository = DaoFactory.getInstance().getDao(DaoType.FINE);

    @Override
    public boolean addFine(Fine fine) throws SQLException {
        return fineRepository.save(fine);
    }

    @Override
    public boolean updateFine(Fine fine) throws SQLException {
        return fineRepository.update(fine);
    }

    @Override
    public boolean deleteFine(String id) throws SQLException {
        return fineRepository.delete(id);
    }

    @Override
    public Fine getFine(String id) throws SQLException {
        return fineRepository.findById(id);
    }

    @Override
    public List<Fine> getAllFines() throws SQLException {
        return fineRepository.findAll();
    }

    @Override
    public List<Fine> getUnpaidFines() throws SQLException {
        return fineRepository.findUnpaidFines();
    }

    @Override
    public boolean payFine(String fineID) throws SQLException {
        return fineRepository.markAsPaid(fineID);
    }
}
