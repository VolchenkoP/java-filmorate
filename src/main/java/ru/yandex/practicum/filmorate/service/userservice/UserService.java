package ru.yandex.practicum.filmorate.service.userservice;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    List<User> findAllUsers();

    User getUserById(int id);

    User getStoredUser(int userId);
}
