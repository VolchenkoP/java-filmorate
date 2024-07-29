package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
@Primary
public class RatingStorageDB implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        String sqlMpa = "select * from RatingMPA";
        return jdbcTemplate.query(sqlMpa, this::makeMpa);
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        String sqlMpa = "select * from RatingMPA where rating_id = ?";
        Mpa mpa;
        try {
            mpa = jdbcTemplate.queryForObject(sqlMpa, this::makeMpa, mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Возрастной рейтинг с идентификатором " + mpaId + " не зарегистрирован!");
        }
        return mpa;
    }

    private Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("rating_id"),
                resultSet.getString("name"),
                resultSet.getString("description"));
    }

}
