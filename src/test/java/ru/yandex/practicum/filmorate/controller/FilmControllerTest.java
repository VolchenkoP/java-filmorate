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

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController controller = new FilmController();
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    Film defaultFilm() {
        Film film = Film.builder()
                .id(null)
                .name("Default")
                .description("Desc")
                .releaseDate(LocalDate.of(2000,1,1))
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
        assertEquals(2,films.size());
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
        Film film = defaultFilm();
        film.setDescription("abc".repeat(200));
        final Set<ConstraintViolation<Film>> violationSet = validator.validate(film);
        assertFalse(violationSet.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void createFilmWithNegativeDurationShouldUnsuccessfullyCreateFilmTest(Long duration) {
        Film film = defaultFilm();
        film.setDuration(duration);
        final Set<ConstraintViolation<Film>> violationSet = validator.validate(film);
        assertFalse(violationSet.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void createFilmWithEmptyNameShouldUnsuccessfullyCreateFilmTest(String name) {
        Film film = defaultFilm();
        film.setName(name);
        final Set<ConstraintViolation<Film>> violationSet = validator.validate(film);
        assertFalse(violationSet.isEmpty());
    }
}