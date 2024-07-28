package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genreservice.GenreService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired(required = false)
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> findAll() {
        log.info("Получен запрос GET к эндпоинту: /genres");
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre findGenre(@PathVariable Integer id) {
        log.info("Получен запрос GET к эндпоинту: /genres/{}", id);
        try {
            return genreService.getGenre(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
