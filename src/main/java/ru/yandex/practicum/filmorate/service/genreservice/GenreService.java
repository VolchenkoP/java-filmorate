package ru.yandex.practicum.filmorate.service.genreservice;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreService {
    Set<Genre> getAllGenres();

    Set<Genre> getFilmGenres(int filmId);

    Genre getGenre(int genreId);

    void deleteFilmGenres(int filmId);

    void addFilmGenres(int filmId, Set<Genre> genres);
}
