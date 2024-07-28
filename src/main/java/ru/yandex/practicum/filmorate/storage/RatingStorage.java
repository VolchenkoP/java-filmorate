package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface RatingStorage {

    List<Mpa> getAllMpa();

    Mpa getMpaById(int ratingId);

}
