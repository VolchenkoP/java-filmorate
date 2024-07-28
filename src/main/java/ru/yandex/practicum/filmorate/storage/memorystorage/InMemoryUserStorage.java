package ru.yandex.practicum.filmorate.storage.memorystorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        try {
            user.setId(getNextId());
            user.setFriends(new ArrayList<>());
            users.put(user.getId(), user);
            log.info("User создан с Id: {}", user.getId());
            return user;
        } catch (ValidationException e) {
            log.error("Ошибка при создании юзера", e);
            throw e;
        }
    }

    @Override
    public User update(User newUser) {
        if (users.containsKey(newUser.getId())) {
            users.put(newUser.getId(), newUser);
            log.info("Юзер с ID: {} успешно обновлен", newUser.getId());
            return newUser;
        } else {
            log.error("Ошибка при обновлении юзера");
            throw new NotFoundException("Юзер с ID: " + newUser.getId() + " не найден");
        }
    }

    @Override
    public boolean delete(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            return true;
        } else {
            log.error("Ошибка при удалении юзера с id: {}", user.getId());
            throw new NotFoundException("User с id: " + user.getId() + " не найден");
        }
    }

    @Override
    public User getUserById(Integer id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            log.error("Ошибка при получении юзера с id: {}", id);
            throw new NotFoundException("User с id: " + id + " не найден");
        }
    }

    private int getNextId() {
        int currentId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentId;
    }
}
