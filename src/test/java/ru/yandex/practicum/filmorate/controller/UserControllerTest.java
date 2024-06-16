package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController = new UserController();
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    User defaultUser() {
        User user = User.builder()
                .id(null)
                .name("defaultUser")
                .email("default@ya.ru")
                .login("defaultLogin")
                .birthday(LocalDate.of(1991, 4, 11))
                .build();
        return user;
    }

    @Test
    void findAllShouldSuccessfullyCreateAndFindAllTest() {
        final User firstUser = defaultUser();
        final User secondUser = defaultUser();
        secondUser.setEmail("another@ya.ru");
        userController.create(firstUser);
        userController.create(secondUser);
        final List<User> users = userController.findAll();
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void updateUserShouldSuccessfullyCreateAndUpdateFilmTest() {
        userController.create(defaultUser());

        final User userForUpdate = defaultUser();
        userForUpdate.setName("CheckUpdateName");
        userForUpdate.setId(1L);
        userController.update(userForUpdate);

        assertEquals(userForUpdate.getName(), userController.findAll().getFirst().getName());
    }

    @Test
    void createUserWithReplaceNameToLoginShouldSuccessfullyCreateUserTest() {
        User user = defaultUser();
        user.setName(null);
        userController.create(user);

        assertEquals("defaultLogin", userController.findAll().getFirst().getName());
    }

    @Test
    void createUserWithFutureBirthdayShouldUnsuccessfullyCreateUser() {
        User user = defaultUser();
        user.setBirthday(LocalDate.of(8888, 12, 12));

        final Set<ConstraintViolation<User>> violationSet = validator.validate(user);

        assertFalse(violationSet.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"@test.ru", "test"})
    void createUserWithWrongEmailShouldUnSuccessfullyCreateUser(String email) {
        User user = defaultUser();
        user.setEmail(email);

        final Set<ConstraintViolation<User>> violationSet = validator.validate(user);

        assertFalse(violationSet.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "log in"})
    void createUserWithWrongLoginShouldUnSuccessfullyCreateUser(String login) {
        User user = defaultUser();
        user.setEmail(login);

        final Set<ConstraintViolation<User>> violationSet = validator.validate(user);

        assertFalse(violationSet.isEmpty());
    }
}