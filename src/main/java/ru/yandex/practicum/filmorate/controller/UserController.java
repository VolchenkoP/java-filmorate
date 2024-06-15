package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        log.info("Получение списка всех юзеров");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        try {
            userValidation(user);
            user.setId(getNextId());
            users.put(user.getId(), user);
            return user;
        } catch (ValidationException e) {
            log.error("Ошибка при создании юзера", e);
            throw e;
        }
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        try {
            if (users.containsKey(newUser.getId())) {
                userValidation(newUser);
                users.put(newUser.getId(), newUser);
                log.debug("Юзер с ID: " + newUser.getId() + " успешно обновлен");
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
