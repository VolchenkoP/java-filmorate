package ru.yandex.practicum.filmorate.storage.dao;


import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageDBTest {
    private final UserStorage userStorage;


    @Test
    public void getUserByIdShouldSuccessfullyCreateAndGetUSerTest() {
        final User user = new User(1, "correct.email@mail.ru", "correct_login", "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        userStorage.create(user);

        final User user2 = userStorage.getUserById(1);

        AssertionsForClassTypes.assertThat(user2).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void getAllUsersShouldSuccessfullyCreateAndFindTwoUsersTest() {
        final User first = new User(2, "cor.email@mail.ru", "correct_login", "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        final User second = new User(3, "corre.email@mail.ru", "correct_login", "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        final List<User> usersBeforeSave = userStorage.getAllUsers();
        userStorage.create(first);
        userStorage.create(second);

        final List<User> usersAfterSave = userStorage.getAllUsers();

        assertEquals(usersBeforeSave.size() + 2, usersAfterSave.size());
    }

    @Test
    void updateUSerShouldSuccessfullyCreateChangeNameAndUpdateUserTest() {
        final User userTest = new User(4, "correc.email@mail.ru", "correct_login", "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        final User added = userStorage.create(userTest);
        added.setName("update");
        userStorage.update(added);

        final User updateUser = userStorage.getUserById(added.getId());

        AssertionsForClassTypes.assertThat(updateUser).hasFieldOrPropertyWithValue("name", "update");
    }

    @Test
    void deleteUserShouldSuccessfullyCreateTwoUserAndDeleteOneTest() {
        final User first = new User(5, "cor2.email@mail.ru", "correct_login", "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        final User second = new User(6, "corre3.email@mail.ru", "correct_login", "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        final User addedFirst = userStorage.create(first);
        userStorage.create(second);

        final List<User> beforeDelete = userStorage.getAllUsers();
        userStorage.delete(addedFirst);

        final List<User> afterDelete = userStorage.getAllUsers();

        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }

    @Test
    void compareUsersAfterSaveShouldUnSuccessfullyCreateAndCompareTwoUsersTest() {
        final User testUser = new User(7, "corre30.email@mail.ru", "correct_login", "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        final User afterSave = userStorage.create(testUser);

        assertNotEquals(testUser, afterSave, "Юзеры равны");
    }

}
