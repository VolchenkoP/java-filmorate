package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.controller.validators.FilmValidator.ReleaseDate;

import java.time.LocalDate;

/**
 * Film.
 */
@Builder
@Data
public class Film {
    private Long id;
    @NotBlank
    @NotNull
    private String name;
    @Size(max = 200)
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    private Long duration;
}