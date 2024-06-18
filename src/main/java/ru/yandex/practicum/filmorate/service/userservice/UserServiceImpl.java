package ru.yandex.practicum.filmorate.service.userservice;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userstorage.UserStorage;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    public UserServiceImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public User createUser(User user) throws ValidationException {
        return storage.create(user);
    }

    @Override
    public User updateUser(User user) throws ValidationException, NotFoundException {
        storage.update(user);
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return storage.findAll();
    }
}
