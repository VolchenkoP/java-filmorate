package ru.yandex.practicum.filmorate.service.filmservice;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film createFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;

    List<Film> findAllFilms();

    List<Film> getPopularFilms(int count);

    Film getFilmById(int filmId);

    Film getStoredFilm(int filmId);
}
