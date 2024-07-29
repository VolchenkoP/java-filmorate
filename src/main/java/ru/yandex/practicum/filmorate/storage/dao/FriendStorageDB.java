package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
@Primary
public class FriendStorageDB implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<User> findAllFriends(int userId) {
        List<User> users = jdbcTemplate.query("SELECT * FROM Users u JOIN Friendship f ON u.user_id = f.friend_id "
                        + "WHERE f.user_id = ?", (rs, rowNum) ->
                        new User(rs.getInt("user_id"),
                                rs.getString("email"),
                                rs.getString("login"),
                                rs.getString("name"),
                                rs.getDate("birthday").toLocalDate()),
                userId);
        return new HashSet<>(users);
    }

    @Override
    public Set<User> getAllCommonFriends(int userId, int otherUserId) {
        List<User> users = jdbcTemplate.query("SELECT DISTINCT u.* FROM Users u JOIN Friendship f "
                        + "ON u.user_id = f.friend_id WHERE f.user_id = ? AND u.user_id "
                        + "IN (SELECT f.friend_id FROM Friendship f WHERE f.user_id = ?)",
                (rs, rowNum) -> new User(rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate()),
                userId, otherUserId);
        return new HashSet<>(users);
    }

    @Override
    public void addToFriends(int userId, int friendId) {
        jdbcTemplate.update("INSERT INTO Friendship (user_id, friend_id, status) "
                + "VALUES (?, ?, true)", userId, friendId);
    }


    @Override
    public void deleteFromFriends(int userId, int friendId) {
        jdbcTemplate.update("DELETE FROM Friendship WHERE user_id = ? AND friend_id = ?", userId, friendId);
    }
}
