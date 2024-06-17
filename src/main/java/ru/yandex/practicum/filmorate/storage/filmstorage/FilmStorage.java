package ru.yandex.practicum.filmorate.storage.filmstorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public List<Film> findAll() {
        log.info("Получение списка всех фильмов");
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
        log.info("Добавление нового фильма");
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film newFilm) {
        log.info("Обновление фильма с ID {}", newFilm.getId());
        try {
            if (films.containsKey(newFilm.getId())) {
                films.put(newFilm.getId(), newFilm);
                return newFilm;
            } else {
                throw new NotFoundException("Фильм с ID: " + newFilm.getId() + " не найден");
            }
        } catch (ValidationException | NotFoundException e) {
            log.error("Ошибка при обновлении фильма", e);
            throw e;
        }
    }

    private long getNextId() {
        long currentId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentId;
    }
}
