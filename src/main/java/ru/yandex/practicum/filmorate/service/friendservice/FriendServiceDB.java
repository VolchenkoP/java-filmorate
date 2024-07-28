package ru.yandex.practicum.filmorate.service.friendservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.userservice.UserService;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
@Primary
public class FriendServiceDB implements FriendService {
    private final UserService userService;
    private final FriendStorage friendStorage;


    @Override
    public void addToFriends(int supposedUserId, int supposedFriendId) {
        User user = userService.getStoredUser(supposedUserId);
        User friend = userService.getStoredUser(supposedFriendId);
        friendStorage.addToFriends(user.getId(), friend.getId());
    }

    @Override
    public void deleteFromFriends(int supposedUserId, int supposedFriendId) {
        try {
            User user = userService.getStoredUser(supposedUserId);
            User friend = userService.getStoredUser(supposedFriendId);
            friendStorage.deleteFromFriends(user.getId(), friend.getId());
        } catch (NotFoundException e) {
            log.error("Ошибка при удалении друга: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Неизвестная ошибка при удалении друзей: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Неизвестная ошибка при удалении друзей", e);
        }
    }

    @Override
    public Set<User> findAllFriends(int supposedUserId) {
        try {
            User user = userService.getStoredUser(supposedUserId);
            Set<User> friends = new HashSet<>();
            for (Integer id : user.getFriends()) {
                friends.add(userService.getUserById(id));
            }
            return friends;
        } catch (NotFoundException e) {
            log.error("Ошибка при получении друзей: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Неизвестная ошибка при получении друзей: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Неизвестная ошибка при получении друзей", e);
        }
    }

    @Override
    public Set<User> getAllCommonFriends(int supposedUserId, int supposedOtherId) {
        User user = userService.getStoredUser(supposedUserId);
        User otherUser = userService.getStoredUser(supposedOtherId);
        Set<User> commonFriends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                commonFriends.add(userService.getUserById(id));
            }
        }
        return commonFriends;
    }
}
