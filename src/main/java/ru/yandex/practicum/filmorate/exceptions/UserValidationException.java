package ru.yandex.practicum.filmorate.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserValidationException extends ConstraintViolationException {
    public UserValidationException(String message, Set<ConstraintViolation<User>> violations) {
        super(message, violations);
    }
}
