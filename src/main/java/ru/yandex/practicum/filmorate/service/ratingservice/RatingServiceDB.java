package ru.yandex.practicum.filmorate.service.ratingservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RatingServiceDB implements RatingService {
    private final RatingStorage ratingStorage;

    @Override
    public List<Mpa> getAllRatings() {
        return new ArrayList<>(ratingStorage.getAllMpa());
    }

    @Override
    public Mpa getRatingById(int ratingId) {
        try {
            Mpa mpa = ratingStorage.getMpaById(ratingId);
            if (mpa == null) {
                throw new NotFoundException("Rating с id " + ratingId + " не найден");
            }
            return mpa;
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный идентификатор: " + ratingId, e);
        }
    }

}
