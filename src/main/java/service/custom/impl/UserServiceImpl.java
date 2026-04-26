package service.custom.impl;

import model.User;
import repository.DaoFactory;
import repository.custom.UserRepository;
import service.custom.UserService;
import util.DaoType;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository = DaoFactory.getInstance().getDao(DaoType.USER);

    @Override
    public boolean addUser(User user) throws SQLException {
        return userRepository.save(user);
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        return userRepository.update(user);
    }

    @Override
    public boolean deleteUser(String id) throws SQLException {
        return userRepository.delete(id);
    }

    @Override
    public User getUser(String id) throws SQLException {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        return userRepository.findAll();
    }

    @Override
    public List<User> searchUsers(String keyword) throws SQLException {
        return userRepository.searchUsers(keyword);
    }
}
