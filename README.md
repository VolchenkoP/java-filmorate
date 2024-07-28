# java-filmorate

Template repository for Filmorate project.

## ER-diagram
---

```mermaid
erDiagram
    USERS || --o{ FRIENDSHIP : "has friendship, make friendship"
    USERS || --o{ LIKES : "make likes"
    USERS {
    user_id integer PK
    email varchar(200)
    login varchar(50)
    name varchar(200)
    birthday date
    }
    FRIENDSHIP {
    friendship_id integer PK    
    user_id integer FK
    friend_id integer FK
    status bool
    }
    FILM || --o{ FILM_GENRE : "has genre"
    FILM || --o{ LIKES : "has likes"
    FILM || --o{ RATING : "has mpa"
    FILM {
    film_id integer PK
    name varchar(200)
    description varchar(200)
    releaseDate date
    duration integer
    rating_id integer FK
    }
    FILM_GENRE || --o{ GENRES : "has genres"
    FILM_GENRE {
    film_genre_id integer PK
    film_id integer FK
    genre_id integer FK
    }
    GENRES {
    genre_id integer FK
    name varchar(200)
    }
    RATINGMPA {
    rating_id integer FK
    name varchar(10)
    description varchar(200)
    }
    LIKES {
    like_id integer PK
    user_id integer FK
    film_id integer FK
    }
```

---

## Examples of SQL query:<br>

- Ex.1 (top 10 films by likes):<br>

```sql
 SELECT g.genre_id, 
        name 
 FROM Genre AS g
 INNER JOIN Film_genre AS fg on g.genre_id = fg.genre_id
 WHERE fg.film_id = 2;
```

- Ex.2 (Take id and login friends by user with id = 1):<br>

```sql
 SELECT u.user_id,
        u.login
 FROM Users AS u
 WHERE u.user_id IN (SELECT fr.friend_id
                     FROM Frendship AS fr
                     WHERE fr.user_id = 1);
```
       

