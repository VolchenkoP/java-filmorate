package ru.yandex.practicum.filmorate.storage.dao;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingStorageDBTest {
    private final RatingStorage storage;

    @Test
    public void getMpaByIdShouldSuccessfullyGetMpaFromDBTest() {
        final Mpa dbMpa = storage.getMpaById(1);
        assertThat(dbMpa).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void getAllMpaShouldSuccessfullyGetMpaFromDBTest() {
        final List<Mpa> allMpa = storage.getAllMpa();
        assertEquals(5, allMpa.size());
    }
}
