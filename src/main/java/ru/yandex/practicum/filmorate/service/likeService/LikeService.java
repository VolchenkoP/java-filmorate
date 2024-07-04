package ru.yandex.practicum.filmorate.service.likeService;

public interface LikeService {

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);
}
