package ru.yandex.practicum.filmorate.service.friendservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.userservice.UserService;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendStorage friendStorage;
    private final UserService userService;

    @Override
    public Set<User> findAllFriends(int id) {
        userService.getUserById(id);
        Map<Integer, User> allUsersMap = new HashMap<>();
        List<Integer> friendIds = friendStorage.findAllFriendsId(id);
        userService.findAllUsers().forEach(user -> allUsersMap.put(user.getId(), user));
        log.info("Список всех друзей для юзера с id: {} успешно составлен", id);
        return friendIds.stream()
                .filter(allUsersMap::containsKey)
                .map(allUsersMap::get)
                .collect(Collectors.toSet());
    }

    @Override
    public void addToFriends(int userId, int friendId) {
        userService.getUserById(userId);
        userService.getUserById(friendId);
        friendStorage.addToFriends(userId, friendId);
    }

    @Override
    public void deleteFromFriends(int userId, int friendId) {
        userService.getUserById(userId);
        userService.getUserById(friendId);
        friendStorage.deleteFromFriends(userId, friendId);
    }

    @Override
    public Set<User> getAllCommonFriends(int userId, int otherId) {
        if (userService.findAllUsers().stream().noneMatch(user -> user.getId() == userId)) {
            log.error("Ошибка при поиске общих друзей при поиске юзера с id: {}", userId);
            throw new NotFoundException("Юзер с ID: " + userId + " не найден");
        }
        if (userService.findAllUsers().stream().noneMatch(user -> user.getId() == otherId)) {
            log.error("Ошибка при поиске общих друзей при поиске юзера с id: {}", otherId);
            throw new NotFoundException("Юзер с ID: " + otherId + " не найден");
        }
        if (userId == otherId) {
            log.error("Введенные для вывода общего списка Id совпадают");
            throw new IllegalArgumentException();
        }

        List<Integer> usersFriends = userService.getUserById(userId).getFriends();
        List<Integer> otherUserFriends = userService.getUserById(otherId).getFriends();
        Set<User> commonFriends = new HashSet<>();

        usersFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userService::getUserById)
                .forEach(commonFriends::add);
        log.info("Список общих друзей с id: {} и id: {} успешно составлен", userId, otherId);
        return commonFriends;
    }

}
