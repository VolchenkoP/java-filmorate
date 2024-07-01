package ru.yandex.practicum.filmorate.storage.userstorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        try {
            user.setId(getNextId());
            user.setFriends(new HashSet<>());
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
        try {
            if (users.containsKey(newUser.getId())) {
                users.put(newUser.getId(), newUser);
                log.info("Юзер с ID: {} успешно обновлен", newUser.getId());
                return newUser;
            }
            throw new NotFoundException("Юзер с ID: " + newUser.getId() + " не найден");
        } catch (ValidationException | NotFoundException e) {
            log.error("Ошибка при обновлении юзера", e);
            throw e;
        }
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("User с id: " + id + " не найден");
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
