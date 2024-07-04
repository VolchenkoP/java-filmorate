package ru.yandex.practicum.filmorate.service.friendService;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendService {

    List<User> findAllFriends(long id);

    List<User> getAllCommonFriends(long id, long otherId);

    void addToFriends(long id, long friendId);

    void deleteFromFriends(long id, long friendId);

}
