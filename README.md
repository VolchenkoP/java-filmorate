# java-filmorate

Template repository for Filmorate project.

## ER-diagram
---
```mermaid
erDiagram
    USER || --o{ USER_FRIENDS : "has, make"
    USER || --o{ LIKES : make
    USER {
    Long user_id PK, FK
    String name
    String email
    String login
    LocalDate birthday
    Set friends
    }
    USER_FRIENDS {
    Long user_id PK, FK
    Long friend_id FK
    String status
    }
    FILM || --o{ FILM_GENRE : has
    FILM || --o{ LIKES : has
    FILM || --o{ RATING : has
    FILM {
    Long film_id PK, FK
    String name
    String description
    LocalDate releaseDate
    Long duration
    Set likes
    HashSet genres
    Long rating_id
    }
    FILM_GENRE || --o{ GENRES : has
    FILM_GENRE {
    Long film_id PK, FK
    Long genre_id FK
    }
    GENRES {
    Long genre_id PK, FK
    String genre_name
    }
    RATING {
    Long rating_id PK, FK
    String rating_name
    }
    LIKES {
    Long film_id PK, FK
    Long user_id
    }
```
---
##Examples of SQL query:<br>
 - Ex.1 (top 10 films by likes):<br>
```sql
 SELECT name
 FROM film
 WHERE film_id IN (SELECT film_id
                   FROM likes
                   GROUP BY film_id
                   ORDER BY COUNT(user_id) DESC
                   LIMIT 10);
```

 - Ex.2 (Take id and login friends by user with id = 1):<br>
```sql
 SELECT u.user_id,
        u.login
 FROM user AS u
 WHERE u.user_id IN (SELECT uf.friend_id
                     FROM user_friends AS uf
                     WHERE uf.user_id = 1);
```
       

