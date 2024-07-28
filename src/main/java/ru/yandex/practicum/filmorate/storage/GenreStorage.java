package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    boolean addFilmGenres(int filmId, List<Genre> genres);

    boolean deleteFilmGenres(int filmId);

    List<Genre> getGenresByFilmId(int filmId);

    List<Genre> getAllGenres();

    Genre getGenreById(int genreId);

}
