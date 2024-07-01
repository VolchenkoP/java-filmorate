package ru.yandex.practicum.filmorate.storage.userstorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User create(User user);

    User update(User newUser);

    User getUserById(Long id);

}
