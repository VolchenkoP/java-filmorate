package ru.yandex.practicum.filmorate.service.likeservice;

public interface LikeService {

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);
}
