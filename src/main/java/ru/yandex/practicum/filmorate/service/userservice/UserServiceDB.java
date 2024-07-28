package ru.yandex.practicum.filmorate.service.userservice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Service("UserServiceDB")
@Slf4j
@Primary
public class UserServiceDB implements UserService {
    private final Validator validator;
    private final UserStorage userStorage;
    private int increment;

    public UserServiceDB(Validator validator, @Qualifier("UserStorageDB") UserStorage userStorage) {
        this.increment = 0;
        this.validator = validator;
        this.userStorage = userStorage;
    }

    @Override
    public List<User> findAllUsers() {
        return userStorage.getAllUsers();
    }


    @Override
    public User createUser(User user) {
        validate(user);
        return userStorage.create(user);
    }

    @Override
    public User updateUser(User user) {
        try {
            validate(user);
            return userStorage.update(user);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка валидации пользователя", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Неизвестная ошибка при обновлении пользователя", e);
        }
    }

    @Override
    public User getUserById(int supposedId) {
        return getStoredUser(supposedId);
    }

    private void validate(final User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("Поле name не задано. Установлено значение {} из поля login", user.getLogin());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info(
                    "Поле name не содержит буквенных символов. " + "Установлено значение {} из поля login",
                    user.getLogin());
        }
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<User> userConstraintViolation : violations) {
                messageBuilder.append(userConstraintViolation.getMessage());
            }
            throw new UserValidationException("Ошибка валидации Пользователя: " + messageBuilder, violations);
        }
        if (user.getId() == 0) {
            user.setId(++increment);
        }
    }

    @Override
    public User getStoredUser(int userId) {
        if (userId == Integer.MIN_VALUE) {
            throw new ru.yandex.practicum.filmorate.exceptions.ValidationException("Не удалось распознать идентификатор " +
                    "пользователя: " + "значение " + userId);
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не зарегистрирован!");
        }
        return user;
    }
}
