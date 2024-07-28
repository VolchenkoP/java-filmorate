package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.controller.validators.FilmValidator.ReleaseDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Film.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @PositiveOrZero
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    private Long duration;
    @NotNull
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();
    private List<Integer> likes = new ArrayList<>();

    public boolean addLike(Integer userId) {
        return likes.add(userId);
    }

    public boolean deleteLike(Integer userId) {
        return likes.remove(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Film))
            return false;
        Film film = (Film) o;
        return getId() == film.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}