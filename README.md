# java-filmorate

Template repository for Filmorate project.

## ER-diagram
---
```mermaid
erDiagram
    USER || --o{ FRIENDSHIP : "has friendship, make friendship"
    USER || --o{ LIKES : "make likes"
    USER {
    user_id integer PK
    user_email varchar(202)
    user_login varchar(55)
    user_name varchar(202)
    user_birthday timestamp
    }
    FRIENDSHIP {
    friendship_id integer PK    
    user_id integer FK
    friend_id integer FK
    friendship_status varchar(50)
    }
    FILM || --o{ FILM_GENRE : "has genre"
    FILM || --o{ LIKES : "has likes"
    FILM || --o{ RATING : "has rating"
    FILM {
    film_id integer PK
    film_name varchar(202)
    film_description varchar(500)
    film_releaseDate timestamp
    film_duration integer
    rating_id integer
    }
    FILM_GENRE || --o{ GENRES : "has genres"
    FILM_GENRE {
    film_genre_id integer PK
    film_id integer FK
    genre_id integer
    }
    GENRES {
    genre_id integer FK
    genre_name varchar(202)
    }
    RATING {
    rating_id integer FK
    rating_name varchar(202)
    }
    LIKES {
    film_id integer FK
    user_id integer FK
    }
```
---
## Examples of SQL query:<br>
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
       

