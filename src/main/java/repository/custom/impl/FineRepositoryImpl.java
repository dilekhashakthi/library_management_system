package repository.custom.impl;

import db.DBConnection;
import model.Fine;
import repository.custom.FineRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FineRepositoryImpl implements FineRepository {

    @Override
    public boolean save(Fine fine) throws SQLException {
        String SQL = "INSERT INTO library_fines (fineID, recordID, userID, fineAmount, status, paidDate) VALUES (?,?,?,?,?,?)";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, fine.getFineID());
        pstm.setString(2, fine.getRecordID());
        pstm.setString(3, fine.getUserID());
        pstm.setInt(4, fine.getFineAmount());
        pstm.setString(5, fine.getStatus());
        pstm.setDate(6, fine.getPaidDate() != null ? Date.valueOf(fine.getPaidDate()) : null);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean update(Fine fine) throws SQLException {
        String SQL = "UPDATE library_fines SET recordID=?, userID=?, fineAmount=?, status=?, paidDate=? WHERE fineID=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, fine.getRecordID());
        pstm.setString(2, fine.getUserID());
        pstm.setInt(3, fine.getFineAmount());
        pstm.setString(4, fine.getStatus());
        pstm.setDate(5, fine.getPaidDate() != null ? Date.valueOf(fine.getPaidDate()) : null);
        pstm.setString(6, fine.getFineID());
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String SQL = "DELETE FROM library_fines WHERE fineID=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, id);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public Fine findById(String id) throws SQLException {
        String SQL = "SELECT * FROM library_fines WHERE fineID=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, id);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<Fine> findAll() throws SQLException {
        List<Fine> list = new ArrayList<>();
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM library_fines");
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public List<Fine> findUnpaidFines() throws SQLException {
        List<Fine> list = new ArrayList<>();
        String SQL = "SELECT * FROM library_fines WHERE status = 'Unpaid'";
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public boolean markAsPaid(String fineID) throws SQLException {
        String SQL = "UPDATE library_fines SET status='Paid', paidDate=? WHERE fineID=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setDate(1, Date.valueOf(LocalDate.now()));
        pstm.setString(2, fineID);
        return pstm.executeUpdate() > 0;
    }

    private Fine mapRow(ResultSet rs) throws SQLException {
        Date paidDate = rs.getDate("paidDate");
        return new Fine(
                rs.getString("fineID"),
                rs.getString("recordID"),
                rs.getString("userID"),
                rs.getInt("fineAmount"),
                rs.getString("status"),
                paidDate != null ? paidDate.toLocalDate() : null
        );
    }
}
