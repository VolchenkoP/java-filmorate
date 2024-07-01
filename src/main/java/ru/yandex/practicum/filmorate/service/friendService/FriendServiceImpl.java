package ru.yandex.practicum.filmorate.service.friendService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userstorage.UserStorage;

import java.util.*;

@Service
@Slf4j
public class FriendServiceImpl implements FriendService {
    private final UserStorage storage;

    public FriendServiceImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<User> findAllFriends(long id) {
        if (storage.findAll().stream().noneMatch(user -> user.getId() == id)) {
            log.error("Ошибка при поиске всех юзеров");
            throw new NotFoundException("Юзер с ID: " + id + " не найден");
        }
        Map<Long, User> allUsersMap = new HashMap<>();
        Set<Long> friendIds = storage.getUserById(id).getFriends();
        storage.findAll().forEach(user -> allUsersMap.put(user.getId(), user));
        log.info("Список всех друзей для юзера с id: {} успешно составлен", id);
        return friendIds.stream()
                .filter(allUsersMap::containsKey)
                .map(allUsersMap::get)
                .toList();
    }

    @Override
    public List<User> getAllCommonFriends(long id, long otherId) {
        if (storage.findAll().stream().noneMatch(user -> user.getId() == id)) {
            log.error("Ошибка при поиске общих друзей при поиске юзера с id: {}", id);
            throw new NotFoundException("Юзер с ID: " + id + " не найден");
        }
        if (storage.findAll().stream().noneMatch(user -> user.getId() == otherId)) {
            log.error("Ошибка при поиске общих друзей при поиске юзера с id: {}", otherId);
            throw new NotFoundException("Юзер с ID: " + otherId + " не найден");
        }
        if (id == otherId) {
            log.error("Введенные для вывода общего списка Id совпадают");
            throw new IllegalArgumentException();
        }

        Set<Long> usersFriends = storage.getUserById(id).getFriends();
        Set<Long> otherUserFriends = storage.getUserById(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();

        usersFriends.stream()
                .filter(otherUserFriends::contains)
                .map(storage::getUserById)
                .forEach(commonFriends::add);
        log.info("Список общих друзей с id: {} и id: {} успешно составлен", id, otherId);
        return commonFriends;
    }

    @Override
    public void addToFriends(long id, long friendId) {
        if (storage.findAll().stream().anyMatch(user -> user.getId() == id)) {
            if (storage.findAll().stream().anyMatch(user -> user.getId() == friendId)) {
                if (id != friendId) {
                    if (storage.getUserById(id).getFriends().contains(friendId) ||
                            storage.getUserById(friendId).getFriends().contains(id)) {
                        log.warn("Ошибка при добавления в друзья");
                        throw new IllegalArgumentException("Введенные для добавления из друзей Id уже друзья");
                    } else {
                        storage.getUserById(id).getFriends().add(friendId);
                        storage.getUserById(friendId).getFriends().add(id);
                        log.info("Добавление прошло успешно");
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
    public void deleteFromFriends(long id, long friendId) {
        if (storage.findAll().stream().anyMatch(user -> user.getId() == id)) {
            if (storage.findAll().stream().anyMatch(user -> user.getId() == friendId)) {
                if (id != friendId) {
                    storage.getUserById(id).getFriends().remove(friendId);
                    storage.getUserById(friendId).getFriends().remove(id);
                    log.info("Удаление прошло успешно");
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
