package service.custom;

import model.User;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface UserService extends SuperService {
    boolean addUser(User user) throws SQLException;
    boolean updateUser(User user) throws SQLException;
    boolean deleteUser(String id) throws SQLException;
    User getUser(String id) throws SQLException;
    List<User> getAllUsers() throws SQLException;
    List<User> searchUsers(String keyword) throws SQLException;
}
