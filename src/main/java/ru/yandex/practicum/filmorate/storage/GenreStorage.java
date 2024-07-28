package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreStorage {

    boolean addFilmGenres(int filmId, Set<Genre> genres);

    boolean deleteFilmGenres(int filmId);

    Set<Genre> getGenresByFilmId(int filmId);

    Set<Genre> getAllGenres();

    Genre getGenreById(int genreId);

}
