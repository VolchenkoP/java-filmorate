package ru.yandex.practicum.filmorate.service.filmservice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.genreservice.GenreService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
@Primary
public class FilmServiceDB implements FilmService {

    private final Validator validator;

    private final FilmStorage filmStorage;

    @Override
    public List<Film> findAllFilms() {
        return filmStorage.findAll();
    }

    @Override
    public Film createFilm(Film film) {
        try {
            validate(film);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return filmStorage.create(film);
    }

    @Override
    public Film updateFilm(Film film) {
        validate(film);
        return filmStorage.update(film);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    @Override
    public Film getFilmById(int filmId) {
        return getStoredFilm(filmId);
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

    private void validate(Film film) {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<Film> filmConstraintViolation : violations) {
                messageBuilder.append(filmConstraintViolation.getMessage());
            }
            throw new ValidationException("Ошибка валидации Фильма: " + messageBuilder, violations);
        }
    }

}
