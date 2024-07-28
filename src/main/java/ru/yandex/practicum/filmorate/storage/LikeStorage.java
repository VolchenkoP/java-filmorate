package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {

    boolean addLike(int filmIid, int userId);

    boolean deleteLike(int filmId, int userId);

}
