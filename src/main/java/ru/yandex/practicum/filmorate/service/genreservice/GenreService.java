package ru.yandex.practicum.filmorate.service.genreservice;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAllGenres();

    List<Genre> getFilmGenres(int filmId);

    Genre getGenre(int genreId);

    void deleteFilmGenres(int filmId);

    void addFilmGenres(int filmId, List<Genre> genres);
}
