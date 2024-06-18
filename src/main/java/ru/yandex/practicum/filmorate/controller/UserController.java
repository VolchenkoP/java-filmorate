package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.userservice.UserService;
import ru.yandex.practicum.filmorate.service.userservice.UserServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userService = userServiceImpl;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Получение списка всех юзеров");
        return userService.findAllUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создание нового user");
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Обновление user с ID {}", newUser.getId());
        return userService.updateUser(newUser);
    }

}