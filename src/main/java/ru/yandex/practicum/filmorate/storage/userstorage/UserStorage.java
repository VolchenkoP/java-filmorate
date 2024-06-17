package ru.yandex.practicum.filmorate.storage.userstorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public List<User> findAll() {
        log.info("Получение списка всех юзеров");
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        try {
            log.info("Создание нового user");
            userValidation(user);
            user.setId(getNextId());
            users.put(user.getId(), user);
            log.info("User создан с Id: {}", user.getId());
            return user;
        } catch (ValidationException e) {
            log.error("Ошибка при создании юзера", e);
            throw e;
        }
    }

    public User update(User newUser) {
        try {
            if (users.containsKey(newUser.getId())) {
                userValidation(newUser);
                users.put(newUser.getId(), newUser);
                log.debug("Юзер с ID: {} успешно обновлен", newUser.getId());
                return newUser;
            }
            throw new NotFoundException("Юзер с ID: " + newUser.getId() + " не найден");
        } catch (ValidationException | NotFoundException e) {
            log.error("Ошибка при обновлении юзера", e);
            throw e;
        }
    }

    private void userValidation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (users.values().stream().anyMatch(x -> x.getEmail().equals(user.getEmail()) && x.getId() != user.getId())) {
            String messageError = "Этот имейл уже используется";
            log.error(messageError);
            throw new ValidationException(messageError);
        }
    }

    private long getNextId() {
        long currentId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentId;
    }
}
