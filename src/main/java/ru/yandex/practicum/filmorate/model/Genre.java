package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private int id;
    @Size(max = 200)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Genre))
            return false;
        Genre genre = (Genre) o;
        return getId() == genre.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
