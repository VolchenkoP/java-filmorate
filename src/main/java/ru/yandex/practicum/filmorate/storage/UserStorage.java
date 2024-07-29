package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserStorage {

    List<User> getAllUsers();

    User create(User user);

    User update(User newUser);

    User getUserById(Integer id);

    boolean delete(User user);

    User makeUser(ResultSet resultSet) throws SQLException;
}
