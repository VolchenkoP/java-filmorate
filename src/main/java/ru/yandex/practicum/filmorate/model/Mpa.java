package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mpa {
    private int id;
    @Size(max = 10)
    private String name;
    @Size(max = 200)
    private String description;
}
