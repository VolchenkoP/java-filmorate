package ru.yandex.practicum.filmorate.service.userservice;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    List<User> findAllUsers();

    List<User> findAllFriends(long id);

    List<User> getAllCommonFriends(long id, long otherId);

    void addToFriends(long id, long friendId);

    void deleteFromFriends(long id, long friendId);
}
