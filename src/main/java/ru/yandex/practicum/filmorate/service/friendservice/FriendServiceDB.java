package ru.yandex.practicum.filmorate.service.friendservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
@Primary
public class FriendServiceDB implements FriendService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;


    @Override
    public void addToFriends(int supposedUserId, int supposedFriendId) {
        User user = userStorage.getUserById(supposedUserId);
        User friend = userStorage.getUserById(supposedFriendId);
        friendStorage.addToFriends(user.getId(), friend.getId());
    }

    @Override
    public void deleteFromFriends(int supposedUserId, int supposedFriendId) {
        try {
            User user = userStorage.getUserById(supposedUserId);
            User friend = userStorage.getUserById(supposedFriendId);
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
        User user = userStorage.getUserById(supposedUserId);
        return friendStorage.findAllFriends(supposedUserId);
    }

    @Override
    public Set<User> getAllCommonFriends(int supposedUserId, int supposedOtherId) {
        User user = userStorage.getUserById(supposedUserId);
        return friendStorage.getAllCommonFriends(supposedUserId, supposedOtherId);
    }
}
