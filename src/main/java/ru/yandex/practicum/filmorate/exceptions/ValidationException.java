package ru.yandex.practicum.filmorate.exceptions;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationException extends RuntimeException {

    // Конструктор для исключения с сообщением и набором нарушений
    public ValidationException(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message);
    }

    // Конструктор для исключения только с сообщением
    public ValidationException(String message) {
        this(message, null); // Передаем null вместо набора нарушений
    }
}
