package ru.yandex.practicum.filmorate.service.userservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userstorage.UserStorage;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Override
    public User createUser(User user) throws ValidationException {
        userValidation(user);
        return storage.create(user);
    }

    @Override
    public User updateUser(User newUser) throws ValidationException, NotFoundException {
        userValidation(newUser);
        return storage.update(newUser);
    }

    @Override
    public List<User> findAllUsers() {
        return storage.findAll();
    }

    private void userValidation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (storage.findAll().stream().anyMatch(x -> x.getEmail().equals(user.getEmail()) &&
                x.getId() != user.getId())) {
            String messageError = "Этот имейл уже используется";
            log.error("Этот имейл уже используется");
            throw new NotFoundException(messageError);
        }
    }
}
