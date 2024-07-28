package ru.yandex.practicum.filmorate.service.userservice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
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
            throw new ValidationException("Ошибка при обновлении Юзера c  id: " + user.getId());
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь не найден при обновлении");
        }
    }

    @Override
    public User getUserById(int supposedId) {
        return getStoredUser(supposedId);
    }

    private void validate(final User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Поле name не содержит буквенных символов. Установлено значение {} из поля login",
                    user.getLogin());
        }
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<User> userConstraintViolation : violations) {
                messageBuilder.append(userConstraintViolation.getMessage());
            }
            throw new ValidationException("Ошибка валидации Фильма: " + messageBuilder, violations);
        }
    }

    @Override
    public User getStoredUser(int userId) {
        if (userId == Integer.MIN_VALUE) {
            throw new ValidationException("Не удалось распознать идентификатор пользователя: значение " + userId);
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не зарегистрирован!");
        }
        return user;
    }
}
