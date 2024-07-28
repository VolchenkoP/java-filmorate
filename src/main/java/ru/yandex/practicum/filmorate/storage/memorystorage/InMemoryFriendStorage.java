package ru.yandex.practicum.filmorate.storage.memorystorage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class InMemoryFriendStorage implements FriendStorage {
    private final UserStorage storage;

    @Override
    public List<Integer> findAllFriendsId(int id) {
        if (storage.getAllUsers().stream().noneMatch(user -> user.getId() == id)) {
            log.error("Ошибка при поиске юзера");
            throw new NotFoundException("Юзер с ID: " + id + " не найден");
        }
        return storage.getUserById(id).getFriends();
    }

    @Override
    public boolean addToFriends(int id, int friendId) {
        if (storage.getAllUsers().stream().anyMatch(user -> user.getId() == id)) {
            if (storage.getAllUsers().stream().anyMatch(user -> user.getId() == friendId)) {
                if (id != friendId) {
                    if (storage.getUserById(id).getFriends().contains(friendId) ||
                            storage.getUserById(friendId).getFriends().contains(id)) {
                        log.warn("Ошибка при добавления в друзья");
                        throw new IllegalArgumentException("Введенные для добавления из друзей Id уже друзья");
                    } else {
                        storage.getUserById(id).getFriends().add(friendId);
                        storage.getUserById(friendId).getFriends().add(id);
                        log.info("Добавление прошло успешно");
                        return true;
                    }
                } else {
                    log.warn("Введенные для добавления в друзья Id совпадают");
                    throw new IllegalArgumentException();
                }
            } else {
                log.error("Ошибка добавления в друзья при поиске юзера с id: {}", friendId);
                throw new NotFoundException("Юзер с ID: " + friendId + " не найден");
            }
        } else {
            log.error("Ошибка при добавлении в друзья при поиске юзера с id: {}", id);
            throw new NotFoundException("Юзер с ID: " + id + " не найден");
        }
    }

    @Override
    public boolean deleteFromFriends(int id, int friendId) {
        if (storage.getAllUsers().stream().anyMatch(user -> user.getId() == id)) {
            if (storage.getAllUsers().stream().anyMatch(user -> user.getId() == friendId)) {
                if (id != friendId) {
                    storage.getUserById(id).getFriends().remove(friendId);
                    storage.getUserById(friendId).getFriends().remove(id);
                    log.info("Удаление прошло успешно");
                    return true;
                } else {
                    log.warn("Введенные для удаления из друзей Id совпадают");
                    throw new IllegalArgumentException();
                }
            } else {
                log.error("Ошибка при удалении из друзей при поиске пользователя с id: {}", friendId);
                throw new NotFoundException("Юзер с ID: " + friendId + " не найден");
            }
        } else {
            log.error("Ошибка при удалении из друзей при поиске юзера с id: {}", id);
            throw new NotFoundException("Юзер с ID: " + id + " не найден");
        }
    }
}
