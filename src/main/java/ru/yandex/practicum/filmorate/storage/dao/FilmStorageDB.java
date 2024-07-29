package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.*;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Component
@Primary
public class FilmStorageDB implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    @Override
    public Film getFilmById(int filmId) {

        String sqlFilm = "select * from Film as f"
                + " INNER JOIN RatingMPA as r on f.rating_id = r.rating_id"
                + " where f.film_id = ?";
        Film film = jdbcTemplate.queryForObject(sqlFilm, (rs, rowNum) -> makeFilm(rs), filmId);
        if (film == null) {
            log.error("Фильм с id: {} не зарегистрирован", filmId);
            throw new NotFoundException("Фильм с идентификатором " + filmId + " не зарегистрирован!");
        } else {
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return film;
        }
    }

    @Override
    public List<Film> findAll() {
        String sql = "select * from Film as f"
                + " INNER JOIN RatingMPA as r on f.rating_id = r.rating_id ";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> makeFilm(resultSet));
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into Film (name, description, releaseDate, duration, rating_id)"
                + " VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setLong(4, film.getDuration());
            preparedStatement.setInt(5, Math.toIntExact(film.getMpa().getId()));
            return preparedStatement;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        if (!film.getGenres().isEmpty()) {
            genreStorage.addFilmGenres(film.getId(), film.getGenres());
        }
        return getFilmById(id);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE Film SET name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        genreStorage.deleteFilmGenres(film.getId());
        if (!film.getGenres().isEmpty()) {
            genreStorage.addFilmGenres(film.getId(), film.getGenres());
        }
        return getFilmById(film.getId());
    }

    @Override
    public boolean delete(Film film) {
        String sqlQuery = "delete from Film where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return true;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlMostPopular = "select count(l.like_id) as likeRate,"
                + " f.film_id, f.name, f.description, f.releaseDate, f.duration,"
                + " r.rating_id, r.name, r.description from Film as f"
                + " left join Likes as l on l.film_id = f.film_id"
                + " inner join RatingMPA as r on r.rating_id = f.rating_id GROUP BY f.film_id"
                + " ORDER BY likeRate desc limit ?";
        return jdbcTemplate.query(sqlMostPopular, (rs, rowNum) -> makeFilm(rs), count);
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        int filmId = resultSet.getInt("film_id");
        return new Film(filmId,
                resultSet.getString("name"),
                resultSet.getString("description"),
                Objects.requireNonNull(resultSet.getDate("releaseDate")).toLocalDate(),
                resultSet.getLong("duration"),
                new Mpa(resultSet.getInt("RatingMPA.rating_id"),
                        resultSet.getString("RatingMPA.name"),
                        resultSet.getString("RatingMPA.description")),
                genreStorage.getGenresByFilmId(filmId));
    }

}
