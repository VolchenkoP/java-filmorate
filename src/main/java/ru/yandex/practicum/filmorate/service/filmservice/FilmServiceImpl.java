package ru.yandex.practicum.filmorate.service.filmservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.userstorage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film updateFilm(Film newFilm) throws NotFoundException {
        return filmStorage.update(newFilm);
    }

    @Override
    public List<Film> findAllFilms() {
        return filmStorage.findAll();
    }

    @Override
    public void addLike(long id, long userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }
        Film film = filmStorage.getFilmById(id);
        if (film != null) {
            Set<Long> likesByFilm = film.getLikes();

            if (likesByFilm.contains(userId)) {
                throw new NotFoundException("Невозможно поставить лайк фильму с id: " + id + " повторно");
            }
            log.info("Фильму с id: {} поставлен лайк от юзера с id: {}", id, userId);
            likesByFilm.add(userId);
        } else {
            throw new NotFoundException("Фильм с id: " + id + " не найден");
        }
    }

    @Override
    public void deleteLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        if (film != null) {
            Set<Long> likesByFilm = film.getLikes();

            if (!likesByFilm.contains(userId)) {
                throw new NotFoundException("Вы не ставили лайк фильму с id: " + id);
            }
            log.info("У фильма с id: {} отозван лайк от юзера с id: {}", id, userId);
            likesByFilm.remove(userId);
        } else {
            throw new NotFoundException("Фильм с id: " + id + " не найден");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> films;
        films = filmStorage.findAll().stream()
                .filter(film -> film.getLikes() != null)
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
        if (!films.isEmpty()) {
            log.info("Список фильмов по рейтингу успешно создан");
            return films;
        } else {
            throw new NotFoundException("Фильмы не найдены");
        }
    }

}
