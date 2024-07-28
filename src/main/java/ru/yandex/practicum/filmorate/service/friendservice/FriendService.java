package ru.yandex.practicum.filmorate.service.friendservice;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendService {

    Set<User> findAllFriends(int id);

    Set<User> getAllCommonFriends(int id, int otherId);

    void addToFriends(int id, int friendId);

    void deleteFromFriends(int id, int friendId);

}
