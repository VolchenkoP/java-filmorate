package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.filmservice.FilmService;
import ru.yandex.practicum.filmorate.service.filmservice.FilmServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Получение списка всех фильмов");
        return filmService.findAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Добавление нового фильма");
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Обновление фильма с ID {}", newFilm.getId());
        return filmService.updateFilm(newFilm);
    }
}
