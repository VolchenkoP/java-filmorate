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
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("Фильм с ID: {} успешно добавлен", film.getId());
        return film;
    }

    public Film update(Film newFilm) {
        try {
            if (films.containsKey(newFilm.getId())) {
                films.put(newFilm.getId(), newFilm);
                log.debug("Фильм с ID: {} успешно обновлен", newFilm.getId());
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
