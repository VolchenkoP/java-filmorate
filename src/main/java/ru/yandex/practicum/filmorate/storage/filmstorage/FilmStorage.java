package ru.yandex.practicum.filmorate.storage.filmstorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    public List<Film> findAll();

    public Film create(Film film);

    public Film update(Film newFilm);

    public Film getFilmById(Long id);

}
