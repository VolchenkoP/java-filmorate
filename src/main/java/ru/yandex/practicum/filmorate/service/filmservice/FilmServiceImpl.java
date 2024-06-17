package ru.yandex.practicum.filmorate.service.filmservice;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmStorage;

import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    public FilmServiceImpl(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film createFilm(Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException, NotFoundException {
        return filmStorage.update(film);
    }

    @Override
    public List<Film> findAllFilms() {
        return filmStorage.findAll();
    }
}
