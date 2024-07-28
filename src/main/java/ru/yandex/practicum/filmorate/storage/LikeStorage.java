package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface LikeStorage {

    boolean addLike(int filmIid, int userId);

    boolean deleteLike(int filmId, int userId);

    boolean setLikes(Film film);

}
