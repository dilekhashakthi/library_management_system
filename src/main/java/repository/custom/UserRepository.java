package repository.custom;

import model.User;
import repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> searchUsers(String keyword) throws SQLException;
}
