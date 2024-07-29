package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.friendservice.FriendService;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FriendController {
    private final FriendService friendService;

    @GetMapping("/{id}/friends")
    public Set<User> findFriends(@PathVariable Integer id) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/friends", id);
        return friendService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> findCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/friends/common/{}", id, otherId);
        return friendService.getAllCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос PUT к эндпоинту: /users/{}/friends/{}", id, friendId);
        friendService.addToFriends(id, friendId);
        log.info("Обновлен объект {} с идентификатором {}. Добавлен друг {}", User.class.getSimpleName(), id,
                friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос DELETE к эндпоинту: /users/{}/friends/{}", id, friendId);
        friendService.deleteFromFriends(id, friendId);
        log.info("Обновлен объект {} с идентификатором {}. Удален друг {}", User.class.getSimpleName(), id, friendId);
    }

}
