package ru.yandex.practicum.filmorate.storage.dao;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageDBTest {
    private final GenreStorage genreStorage;

    @Test
    public void getGenreByIdShouldSuccessfullyGetGenreFromDBTest() {
        final Genre dbGenre = genreStorage.getGenreById(1);
        assertThat(dbGenre).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void getAllGenresShouldSuccessfullyGetAllGenresFromDBTest() {
        final List<Genre> genres = genreStorage.getAllGenres();
        assertEquals(6, genres.size());
    }

}
