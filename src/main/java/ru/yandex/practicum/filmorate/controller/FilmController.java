package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.filmservice.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        log.info("Получение списка всех фильмов");
        return filmService.findAllFilms();
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получение списка {} популярных фильмов", count);
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable Integer id) {
        log.info("Получен запрос GET к эндпоинту: /films/{}", id);
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос POST. Данные тела запроса: {}", film);
        try {
            Film validFilm = filmService.createFilm(film);
            log.info("Создан объект {} с идентификатором {}", Film.class.getSimpleName(), validFilm.getId());
            return validFilm;
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка при создании фильма: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при создании фильма: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Непредвиденная ошибка при создании фильма", e);
        }
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Обновление фильма с ID {}", newFilm.getId());
        return filmService.updateFilm(newFilm);
    }

}
