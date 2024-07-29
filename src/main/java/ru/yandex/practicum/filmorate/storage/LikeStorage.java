package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {

    void addLike(int filmIid, int userId);

    void deleteLike(int filmId, int userId);

}
