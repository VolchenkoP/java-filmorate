package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.friendService.FriendService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FriendController {
    private final FriendService friendService;

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable Long id) {
        log.info("Получение списка всех друзей user с id {}", id);
        return friendService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getAllCommonFriends(@PathVariable Long id,
                                          @PathVariable Long otherId) {
        log.info("Получение списка всех общих друзей с user {}", id);
        return friendService.getAllCommonFriends(id, otherId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addToFriends(@PathVariable Long userId,
                             @PathVariable Long friendId) {
        log.info("Добавление для user c Id {} нового друга с Id {}", userId, friendId);
        friendService.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable Long id,
                                  @PathVariable Long friendId) {
        log.info("Удаление для user c Id {} друга с Id {}", id, friendId);
        friendService.deleteFromFriends(id, friendId);
    }

}
