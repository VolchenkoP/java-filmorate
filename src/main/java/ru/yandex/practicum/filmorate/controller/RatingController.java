package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.ratingservice.RatingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mpa")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public List<Mpa> findAll() {
        log.info("Получен запрос GET к эндпоинту: /mpa");
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public Mpa findRatings(@PathVariable Integer id) {
        log.info("Получен запрос GET к эндпоинту: /mpa/{}", id);
        return ratingService.getRatingById(id);
    }
}
