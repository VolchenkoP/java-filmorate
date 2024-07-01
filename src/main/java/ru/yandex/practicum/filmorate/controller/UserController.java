package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.userservice.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        log.info("Получение списка всех юзеров");
        return userService.findAllUsers();
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable Long id) {
        log.info("Получение списка всех друзей user с id {}", id);
        return userService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getAllCommonFriends(@PathVariable Long id,
                                          @PathVariable Long otherId) {
        log.info("Получение списка всех общих друзей с user {}", id);
        return userService.getAllCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        log.info("Создание нового user");
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Обновление user с Id {}", newUser.getId());
        return userService.updateUser(newUser);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addToFriends(@PathVariable Long userId,
                             @PathVariable Long friendId) {
        log.info("Добавление для user c Id {} нового друга с Id {}", userId, friendId);
        userService.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable Long id,
                                  @PathVariable Long friendId) {
        log.info("Удаление для user c Id {} друга с Id {}", id, friendId);
        userService.deleteFromFriends(id, friendId);
    }

}