package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
@Primary
public class GenreStorageDB implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void deleteFilmGenres(int filmId) {
        String deleteOldGenres = "delete from Film_genre where film_id = ?";
        jdbcTemplate.update(deleteOldGenres, filmId);
    }

    @Override
    public void addFilmGenres(int filmId, Set<Genre> genres) {
        List<Object[]> batchArgs = genres.stream()
                .map(genre -> new Object[]{filmId, genre.getId()})
                .toList();

        jdbcTemplate.batchUpdate(
                "INSERT INTO Film_genre (film_id, genre_id) VALUES (?, ?) ON CONFLICT DO NOTHING",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, (Integer) batchArgs.get(i)[0]);
                        ps.setInt(2, (Integer) batchArgs.get(i)[1]);
                    }

                    @Override
                    public int getBatchSize() {
                        return batchArgs.size();
                    }
                });
    }

    @Override
    public Set<Genre> getGenresByFilmId(int filmId) {
        String sqlGenre = "select g.genre_id, name from Genre as g"
                + " INNER JOIN Film_genre as fg on g.genre_id = fg.genre_id where fg.film_id = ?";
        return new LinkedHashSet<>(jdbcTemplate.query(sqlGenre, this::makeGenre, filmId));
    }

    @Override
    public Set<Genre> getAllGenres() {
        String sqlGenre = "select genre_id, name from Genre ORDER BY genre_id";
        return new LinkedHashSet<>(jdbcTemplate.query(sqlGenre, this::makeGenre));
    }

    @Override
    public Genre getGenreById(int genreId) {
        String sqlGenre = "select * from Genre where genre_id = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlGenre, this::makeGenre, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с идентификатором " + genreId + " не зарегистрирован!");
        }
        return genre;
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"), resultSet.getString("name"));
    }
}
