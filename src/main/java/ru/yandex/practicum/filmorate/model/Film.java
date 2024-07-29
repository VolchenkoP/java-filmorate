package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.controller.validators.FilmValidator.ReleaseDate;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

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