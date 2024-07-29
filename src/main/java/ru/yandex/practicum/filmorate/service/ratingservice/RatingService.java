package ru.yandex.practicum.filmorate.service.ratingservice;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface RatingService {
    List<Mpa> getAllRatings();

    Mpa getRatingById(int ratingId);
}
