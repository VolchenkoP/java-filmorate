package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Component
@Primary
public class LikeStorageDB implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addLike(int filmId, int userId) {
        String sql = "select * from Likes where user_id = ? and film_id = ?";
        SqlRowSet existLike = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        if (!existLike.next()) {
            String setLike = "insert into Likes (user_id, film_id) VALUES (?, ?) ";
            jdbcTemplate.update(setLike, userId, filmId);
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        log.info(String.valueOf(sqlRowSet.next()));
        return sqlRowSet.next();
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        String deleteLike = "delete from Likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(deleteLike, filmId, userId);
        return true;
    }

    @Override
    public boolean setLikes(Film film) {
        Set<Integer> existingUsers = new HashSet<>();
        String selectExistingUsersSql = "SELECT DISTINCT user_id FROM Likes WHERE film_id = ?";
        SqlRowSet usersResultSet = jdbcTemplate.queryForRowSet(selectExistingUsersSql, film.getId());
        while (usersResultSet.next()) {
            existingUsers.add(usersResultSet.getInt("user_id"));
        }

        List<String> insertQueries = new ArrayList<>();
        for (Integer userId : film.getLikes()) {
            if (!existingUsers.contains(userId)) {
                String insertQuery = "INSERT INTO Likes (user_id, film_id) VALUES (?, ?)";
                insertQueries.add(insertQuery);
            }
        }

        try {
            jdbcTemplate.batchUpdate(insertQueries.toArray(new String[0]));
        } catch (Exception e) {
            throw new ValidationException("Произошла ошибка при обновлении лайков у фильма с id: " + film.getId());
        }

        return true;
    }
}
