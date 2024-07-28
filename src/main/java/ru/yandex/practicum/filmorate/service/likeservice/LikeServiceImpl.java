package ru.yandex.practicum.filmorate.service.likeservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Service
@Slf4j
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeStorage likeStorage;

    @Override
    public void addLike(int filmId, int userId) {
        likeStorage.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        likeStorage.deleteLike(filmId, userId);
    }

}
