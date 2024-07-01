package ru.yandex.practicum.filmorate.service.filmservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film updateFilm(Film newFilm) throws NotFoundException {
        return filmStorage.update(newFilm);
    }

    @Override
    public List<Film> findAllFilms() {
        return filmStorage.findAll();
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> films;
        films = filmStorage.findAll().stream()
                .filter(film -> film.getLikes() != null)
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
        if (!films.isEmpty()) {
            log.info("Список фильмов по рейтингу успешно создан");
            return films;
        } else {
            log.error("Ошибка при получении списка фильма по пулярности");
            throw new NotFoundException("Фильмы не найдены");
        }
    }

}
