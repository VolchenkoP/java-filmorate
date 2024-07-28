package ru.yandex.practicum.filmorate.service.likeservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.filmservice.FilmService;
import ru.yandex.practicum.filmorate.service.userservice.UserService;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Service
@Slf4j
@AllArgsConstructor
@Primary
public class LikeServiceDB implements LikeService {
    private final FilmService filmService;
    private final LikeStorage likeStorage;
    private final UserService userService;

    @Override
    public void addLike(int filmId, int userId) {
        Film film = filmService.getStoredFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        likeStorage.addLike(film.getId(), user.getId());
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        Film film = filmService.getStoredFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        likeStorage.deleteLike(film.getId(), user.getId());
    }
}
