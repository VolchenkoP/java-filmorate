package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.userservice.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(@Autowired(required = false) @Qualifier("UserServiceDB") UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Получен запрос GET к эндпоинту: /users");
        return userService.findAllUsers();
    }


    @GetMapping("/{id}")
    public User findUser(@PathVariable Integer id) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/", id);
        return userService.getUserById(id);
    }


    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос POST. Данные тела запроса: {}", user);
        final User validUser = userService.createUser(user);
        log.info("Создан объект {} с идентификатором {}", User.class.getSimpleName(), validUser.getId());
        return validUser;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", user);
        try {
            final User validUser = userService.updateUser(user);
            log.info("Обновлен объект {} с идентификатором {}", User.class.getSimpleName(), validUser.getId());
            return validUser;
        } catch (ResponseStatusException e) {
            log.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Неизвестная ошибка при обновлении пользователя: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Неизвестная ошибка при обновлении пользователя", e);
        }
    }

}