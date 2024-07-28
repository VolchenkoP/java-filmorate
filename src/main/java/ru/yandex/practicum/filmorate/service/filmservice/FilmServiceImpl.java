package ru.yandex.practicum.filmorate.service.filmservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Override
    public Film createFilm(Film film) {
        try {
            Film validFilm = filmStorage.create(film);
            log.info("Создан объект {} с идентификатором {}", Film.class.getSimpleName(), validFilm.getId());
            return validFilm;
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка при создании фильма: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при создании фильма: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Непредвиденная ошибка при создании фильма", e);
        }
    }

    @Override
    public Film updateFilm(Film newFilm) throws NotFoundException {
        try {
            Film validFilm = filmStorage.getFilmById(newFilm.getId());
            if (validFilm != null) {
                log.info("Обновлен фильм {} с идентификатором {}", Film.class.getSimpleName(), validFilm.getId());
                return filmStorage.update(newFilm);
            } else {
                String messageError = "Этот имейл уже используется";
                log.error("Этот имейл уже используется");
                throw new NotFoundException(messageError);
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка при обновлении фильма: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при обновлении фильма: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Непредвиденная ошибка при создании фильма", e);
        }
    }

    @Override
    public List<Film> findAllFilms() {
        return filmStorage.findAll();
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
            log.error("Ошибка при получении списка фильма по пулярности");
            throw new NotFoundException("Фильмы не найдены");
        }
    }

    @Override
    public Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @Override
    public Film getStoredFilm(int filmId) {
        if (filmId == Integer.MIN_VALUE) {
            throw new ValidationException("Не удалось распознать идентификатор фильма: " + "значение " + filmId);
        }
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с идентификатором " + filmId + " не зарегистрирован!");
        }
        return film;
    }

}
