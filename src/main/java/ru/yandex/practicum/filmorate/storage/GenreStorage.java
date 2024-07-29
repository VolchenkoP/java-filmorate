package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreStorage {

    void addFilmGenres(int filmId, Set<Genre> genres);

    void deleteFilmGenres(int filmId);

    Set<Genre> getGenresByFilmId(int filmId);

    Set<Genre> getAllGenres();

    Genre getGenreById(int genreId);

}
