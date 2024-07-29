package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    @NotBlank
    @Email
    @Size(max = 200)
    private String email;
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы")
    private String login;
    @Size(max = 200)
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
