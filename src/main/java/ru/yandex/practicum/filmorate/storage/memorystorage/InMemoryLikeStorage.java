package ru.yandex.practicum.filmorate.storage.memorystorage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class InMemoryLikeStorage implements LikeStorage {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public boolean addLike(int filmId, int userId) {
        if (userStorage.getUserById(userId) == null) {
            log.error("Ошибка при выставлении лайка фильму с id: {} от юзера с id {}", filmId, userId);
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            List<Integer> likesByFilm = film.getLikes();

            if (likesByFilm.contains(userId)) {
                log.error("Этим юзером лайк уже поставлен");
                throw new IllegalArgumentException("Невозможно поставить лайк фильму с id: " + filmId + " повторно");
            }
            log.info("Фильму с id: {} поставлен лайк от юзера с id: {}", filmId, userId);
            likesByFilm.add(userId);
            return true;
        } else {
            log.error("Ошибка при выставлении лайка фильму с id: {}", filmId);
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            List<Integer> likesByFilm = film.getLikes();
            if (!likesByFilm.contains(userId)) {
                log.error("Ошибка при удалении лайка у фильма с id: {} от юзера с id: {}", filmId, userId);
                throw new NotFoundException("Вы не ставили лайк фильму с id: " + filmId);
            }
            log.info("У фильма с id: {} отозван лайк от юзера с id: {}", filmId, userId);
            likesByFilm.remove(userId);
            return true;
        } else {
            log.error("Ошибка при удалении лайка у фильма с id: {}", filmId);
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
    }

    @Override
    public boolean setLikes(Film film) {
        Film filmFromMemory = filmStorage.getFilmById(film.getId());
        if (filmFromMemory != null) {
            filmFromMemory.setLikes(film.getLikes());
        } else {
            throw new NotFoundException("Фильм с id: " + film.getId() + "для добавления лайков не найден");
        }
        return false;
    }

}
