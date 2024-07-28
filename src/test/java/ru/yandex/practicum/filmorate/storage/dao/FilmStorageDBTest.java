package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageDBTest {
    private final FilmStorage filmStorage;


    @Test
    public void getFilmByIdShouldSuccessfullyCreateAndGetFilmTest() {
        final Film film = new Film(1, "first", "first description",
                LocalDate.now().minusYears(8), 90L,
                new Mpa(1, "o", "o"), new LinkedHashSet<>(), new ArrayList<>());
        filmStorage.create(film);

        final Film dbFilm = filmStorage.getFilmById(1);

        assertThat(dbFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void getAllFilmsShouldSuccessfullyCreateAndFindTwoFilmsTest() {
        final Film first = new Film(2, "first", "first description",
                LocalDate.now().minusYears(8), 90L,
                new Mpa(1, "o", "o"), new LinkedHashSet<>(), new ArrayList<>());
        Film second = new Film(3, "second", "second description",
                LocalDate.now().minusYears(15), 100L,
                new Mpa(3, "o", "o"), new LinkedHashSet<>(), new ArrayList<>());
        List<Film> filmsBeforeSave = filmStorage.findAll();
        filmStorage.create(first);
        filmStorage.create(second);

        List<Film> filmsAfterSave = filmStorage.findAll();

        assertEquals(filmsBeforeSave.size() + 2, filmsAfterSave.size());
    }

    @Test
    void updateFilmShouldSuccessfullyCreateChangeNameAndUpdateFilmTest() {
        final Film first = new Film(4, "first", "first description",
                LocalDate.now().minusYears(8), 90L,
                new Mpa(1, "o", "o"), new LinkedHashSet<>(), new ArrayList<>());
        final Film added = filmStorage.create(first);
        added.setName("update");
        filmStorage.update(added);

        final Film updateFilm = filmStorage.getFilmById(added.getId());

        assertThat(updateFilm).hasFieldOrPropertyWithValue("name", "update");
    }

    @Test
    void deleteFilmShouldSuccessfullyCreateTwoFilmsAndDeleteOneTest() {
        final Film first = new Film(5, "first", "first description",
                LocalDate.now().minusYears(8), 90L,
                new Mpa(1, "o", "o"), new LinkedHashSet<>(), new ArrayList<>());
        final Film second = new Film(6, "second", "second description",
                LocalDate.now().minusYears(15), 100L,
                new Mpa(3, "o", "o"), new LinkedHashSet<>(), new ArrayList<>());
        final Film addedFirst = filmStorage.create(first);
        filmStorage.create(second);

        final List<Film> beforeDelete = filmStorage.findAll();
        filmStorage.delete(addedFirst);

        final List<Film> afterDelete = filmStorage.findAll();

        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }

    @Test
    void compareFilmsAfterSaveShouldSuccessfullyCreateAndCompareTwoFilmsTest() {
        final Film first = new Film(7, "first", "first description",
                LocalDate.now().minusYears(8), 90L,
                new Mpa(1, "o", "o"), new LinkedHashSet<>(), new ArrayList<>());
        final Film afterSave = filmStorage.create(first);

        assertEquals(first, afterSave, "Фильмы не равны");
    }
}


