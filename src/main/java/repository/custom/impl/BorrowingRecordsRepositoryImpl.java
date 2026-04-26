package repository.custom.impl;

import db.DBConnection;
import model.BorrowingRecords;
import repository.custom.BorrowingRecordsRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowingRecordsRepositoryImpl implements BorrowingRecordsRepository {

    @Override
    public boolean save(BorrowingRecords record) throws SQLException {
        String SQL = "INSERT INTO library_borrow_records (RecordID, UserID, BookID, BorrowDate, ReturnDate, Fine) VALUES (?,?,?,?,?,?)";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, record.getRecordID());
        pstm.setString(2, record.getUserID());
        pstm.setString(3, record.getBookID());
        pstm.setDate(4, Date.valueOf(record.getBorrowDate()));
        pstm.setDate(5, record.getReturn_Date() != null ? Date.valueOf(record.getReturn_Date()) : null);
        pstm.setInt(6, record.getFine() != null ? record.getFine() : 0);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean update(BorrowingRecords record) throws SQLException {
        String SQL = "UPDATE library_borrow_records SET UserID=?, BookID=?, BorrowDate=?, ReturnDate=?, Fine=? WHERE RecordID=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, record.getUserID());
        pstm.setString(2, record.getBookID());
        pstm.setDate(3, Date.valueOf(record.getBorrowDate()));
        pstm.setDate(4, record.getReturn_Date() != null ? Date.valueOf(record.getReturn_Date()) : null);
        pstm.setInt(5, record.getFine() != null ? record.getFine() : 0);
        pstm.setString(6, record.getRecordID());
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String SQL = "DELETE FROM library_borrow_records WHERE RecordID=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, id);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public BorrowingRecords findById(String id) throws SQLException {
        String SQL = "SELECT * FROM library_borrow_records WHERE RecordID=?";
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
    public List<BorrowingRecords> findAll() throws SQLException {
        List<BorrowingRecords> list = new ArrayList<>();
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM library_borrow_records");
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public List<BorrowingRecords> findByUserId(String userId) throws SQLException {
        List<BorrowingRecords> list = new ArrayList<>();
        String SQL = "SELECT * FROM library_borrow_records WHERE UserID=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, userId);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public List<BorrowingRecords> findBorrowedBooks() throws SQLException {
        List<BorrowingRecords> list = new ArrayList<>();
        String SQL = "SELECT * FROM library_borrow_records WHERE ReturnDate IS NULL OR ReturnDate >= CURDATE()";
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public List<BorrowingRecords> findOverdueBooks() throws SQLException {
        List<BorrowingRecords> list = new ArrayList<>();
        String SQL = "SELECT * FROM library_borrow_records WHERE ReturnDate < CURDATE()";
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public boolean updateBookAvailability(String bookId, String status) throws SQLException {
        String SQL = "UPDATE library_books SET availabilityStatus=? WHERE bookID=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, status);
        pstm.setString(2, bookId);
        return pstm.executeUpdate() > 0;
    }

    private BorrowingRecords mapRow(ResultSet rs) throws SQLException {
        return new BorrowingRecords(
                rs.getString("RecordID"),
                rs.getString("UserID"),
                rs.getString("BookID"),
                rs.getDate("BorrowDate").toLocalDate(),
                rs.getDate("ReturnDate") != null ? rs.getDate("ReturnDate").toLocalDate() : null,
                rs.getInt("Fine")
        );
    }
}
