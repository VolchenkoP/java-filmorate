package ru.yandex.practicum.filmorate.service.genreservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    @Override
    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    @Override
    public List<Genre> getFilmGenres(int filmId) {
        return genreStorage.getGenresByFilmId(filmId);
    }

    @Override
    public Genre getGenre(int genreId) {
        try {
            Genre genre = genreStorage.getGenreById(genreId);
            if (genre == null) {
                throw new NotFoundException("Жанр с id " + genreId + " не найден");
            }
            return genre;
        } catch (NumberFormatException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный идентификатор: " + genreId, e);
        }
    }

    @Override
    public void deleteFilmGenres(int filmId) {
        genreStorage.deleteFilmGenres(filmId);
    }

    @Override
    public void addFilmGenres(int filmId, List<Genre> genres) {
        genreStorage.addFilmGenres(filmId, genres);
    }

}
