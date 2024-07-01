package ru.yandex.practicum.filmorate.storage.userstorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public List<User> findAll();

    public User create(User user);

    public User update(User newUser);

    public User getUserById(Long id);

}
