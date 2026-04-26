package repository.custom.impl;

import db.DBConnection;
import model.User;
import repository.custom.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public boolean save(User user) throws SQLException {
        String SQL = "INSERT INTO library_users (id, name, contact_information, membership_date) VALUES (?,?,?,?)";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, user.getId());
        pstm.setString(2, user.getName());
        pstm.setString(3, user.getContactInfomation());
        pstm.setDate(4, Date.valueOf(user.getMembershipDate()));
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean update(User user) throws SQLException {
        String SQL = "UPDATE library_users SET name=?, contact_information=?, membership_date=? WHERE id=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, user.getName());
        pstm.setString(2, user.getContactInfomation());
        pstm.setDate(3, Date.valueOf(user.getMembershipDate()));
        pstm.setString(4, user.getId());
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String SQL = "DELETE FROM library_users WHERE id=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, id);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public User findById(String id) throws SQLException {
        String SQL = "SELECT * FROM library_users WHERE id=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setString(1, id);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return new User(rs.getString("id"), rs.getString("name"),
                    rs.getString("contact_information"), rs.getDate("membership_date").toLocalDate());
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM library_users");
        while (rs.next()) {
            users.add(new User(rs.getString("id"), rs.getString("name"),
                    rs.getString("contact_information"), rs.getDate("membership_date").toLocalDate()));
        }
        return users;
    }

    @Override
    public List<User> searchUsers(String keyword) throws SQLException {
        List<User> users = new ArrayList<>();
        String SQL = "SELECT * FROM library_users WHERE name LIKE ? OR id LIKE ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        String k = "%" + keyword + "%";
        pstm.setString(1, k);
        pstm.setString(2, k);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            users.add(new User(rs.getString("id"), rs.getString("name"),
                    rs.getString("contact_information"), rs.getDate("membership_date").toLocalDate()));
        }
        return users;
    }
}
