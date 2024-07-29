package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendStorage {

    Set<User> findAllFriends(int userId);

    Set<User> getAllCommonFriends(int userId, int friendId);

    void addToFriends(int id, int friendId);

    void deleteFromFriends(int id, int friendId);
}
