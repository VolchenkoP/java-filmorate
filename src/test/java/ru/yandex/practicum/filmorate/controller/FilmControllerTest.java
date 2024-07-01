package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.filmservice.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmstorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.userstorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.userstorage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final FilmServiceImpl filmService = new FilmServiceImpl(filmStorage, userStorage);
    private final FilmController controller = new FilmController(filmService);
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    Film defaultFilm() {
        Film film = Film.builder()
                .id(null)
                .likes(new HashSet<>())
                .name("Default")
                .description("Desc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120L)
                .build();
        return film;
    }

    @Test
    void findAllShouldSuccessfullyCreateAndFindAllTest() {
        controller.create(defaultFilm());
        controller.create(defaultFilm());
        final List<Film> films = controller.findAll();
        assertNotNull(films);
        assertEquals(2, films.size());
    }

    @Test
    void updateFilmShouldSuccessfullyCreateAndUpdateFilmTest() {
        controller.create(defaultFilm());

        final Film filmForUpdate = defaultFilm();
        filmForUpdate.setName("CheckUpdateName");
        filmForUpdate.setId(1L);
        controller.update(filmForUpdate);

        assertEquals(filmForUpdate.getName(), controller.findAll().getFirst().getName());
    }

    @Test
    void createFilmWithDurationMoreThen200CharAtShouldUnSuccessfullyCreateFilmTest() {
        final Film film = defaultFilm();
        film.setDescription("abc".repeat(200));
        final Set<ConstraintViolation<Film>> violationSet = validator.validate(film);

        assertFalse(violationSet.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void createFilmWithNegativeDurationShouldUnsuccessfullyCreateFilmTest(Long duration) {
        final Film film = defaultFilm();
        film.setDuration(duration);
        final Set<ConstraintViolation<Film>> violationSet = validator.validate(film);

        assertFalse(violationSet.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void createFilmWithEmptyNameShouldUnsuccessfullyCreateFilmTest(String name) {
        final Film film = defaultFilm();
        film.setName(name);
        final Set<ConstraintViolation<Film>> violationSet = validator.validate(film);
        assertFalse(violationSet.isEmpty());
    }
}