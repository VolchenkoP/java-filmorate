package ru.yandex.practicum.filmorate.storage.filmstorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        film.setLikes(new HashSet<>());
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
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            log.info("Фильм с ID: {} успешно найден", id);
            return films.get(id);
        } else {
            log.error("Ошибка при получении фильма с id: {}", id);
            throw new NotFoundException("Film с id: " + id + " не найден");
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
