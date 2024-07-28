package ru.yandex.practicum.filmorate.storage.memorystorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        film.setLikes(new ArrayList<>());
        films.put(film.getId(), film);
        log.info("Фильм с ID: {} успешно добавлен", film.getId());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            log.info("Фильм с ID: {} успешно обновлен", newFilm.getId());
            return newFilm;
        } else {
            log.error("Ошибка при обновлении фильма с id: {}", newFilm.getId());
            throw new NotFoundException("Фильм с ID: " + newFilm.getId() + " не найден");
        }
    }

    @Override
    public boolean delete(Film film) {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            log.info("Фильм с ID: {} успешно удален", film.getId());
            return true;
        } else {
            log.error("Ошибка при удалении фильма с id: {}", film.getId());
            throw new NotFoundException("Фильм с ID: " + film.getId() + " не найден");
        }
    }

    @Override
    public Film getFilmById(int filmId) {
        if (films.containsKey(filmId)) {
            log.info("Фильм с ID: {} успешно найден", filmId);
            return films.get(filmId);
        } else {
            log.error("Ошибка при получении фильма с id: {}", filmId);
            throw new NotFoundException("Film с id: " + filmId + " не найден");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return List.of();
    }

    private int getNextId() {
        int currentId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentId;
    }

}
