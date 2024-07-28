package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.controller.validators.FilmValidator.ReleaseDate;

import java.time.LocalDate;
import java.util.*;

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
    @Size(max = 200)
    @NotBlank
    private String name;
    @Size(max = 200)
    @NotBlank
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    @NotNull
    private Long duration;
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres = new LinkedHashSet<>();
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