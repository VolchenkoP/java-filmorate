package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Component
@AllArgsConstructor
@Primary
public class FriendStorageDB implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Integer> findAllFriendsId(int userId) {
        String sqlGetFriends = "select friend_id from Friendship where user_id = ?";
        return jdbcTemplate.queryForList(sqlGetFriends, Integer.class, userId);
        // переделать под юзеров
    }

    @Override
    public boolean addToFriends(int userId, int friendId) {
        boolean friendAccepted;
        String sqlGetReversFriend = "select * from Friendship where user_id = ? and friend_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGetReversFriend, friendId, userId);
        friendAccepted = sqlRowSet.next();
        String sqlSetFriend = "insert into Friendship (user_id, friend_id, status) VALUES (?,?,?)";
        jdbcTemplate.update(sqlSetFriend, userId, friendId, friendAccepted);
        if (friendAccepted) {
            String sqlSetStatus = "update Friendship set status = true where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlSetStatus, friendId, userId);
        }
        return true;
    }

    @Override
    public boolean deleteFromFriends(int userId, int friendId) {
        String sqlDeleteFriend = "delete from Friendship where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlDeleteFriend, userId, friendId);
        String sqlSetStatus = "update Friendship set status = false where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlSetStatus, friendId, userId);
        return true;
    }
}
