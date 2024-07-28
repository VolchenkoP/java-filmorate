package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FriendStorage {

    List<Integer> findAllFriendsId(int id);

    boolean addToFriends(int id, int friendId);

    boolean deleteFromFriends(int id, int friendId);
}
