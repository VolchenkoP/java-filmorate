package ru.yandex.practicum.filmorate.service.likeService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.userstorage.UserStorage;

import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public void addLike(long id, long userId) {
        if (userStorage.getUserById(userId) == null) {
            log.error("Ошибка при выставлении лайка фильму с id: {} от юзера с id {}", id, userId);
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }
        Film film = filmStorage.getFilmById(id);
        if (film != null) {
            Set<Long> likesByFilm = film.getLikes();

            if (likesByFilm.contains(userId)) {
                log.error("Этим юзером лайк уже поставлен");
                throw new IllegalArgumentException("Невозможно поставить лайк фильму с id: " + id + " повторно");
            }
            log.info("Фильму с id: {} поставлен лайк от юзера с id: {}", id, userId);
            likesByFilm.add(userId);
        } else {
            log.error("Ошибка при выставлении лайка фильму с id: {}", id);
            throw new NotFoundException("Фильм с id: " + id + " не найден");
        }
    }

    @Override
    public void deleteLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        if (film != null) {
            Set<Long> likesByFilm = film.getLikes();

            if (!likesByFilm.contains(userId)) {
                log.error("Ошибка при удалении лайка у фильма с id: {} от юзера с id: {}", id, userId);
                throw new NotFoundException("Вы не ставили лайк фильму с id: " + id);
            }
            log.info("У фильма с id: {} отозван лайк от юзера с id: {}", id, userId);
            likesByFilm.remove(userId);
        } else {
            log.error("Ошибка при удалении лайка у фильма с id: {}", id);
            throw new NotFoundException("Фильм с id: " + id + " не найден");
        }
    }
}
