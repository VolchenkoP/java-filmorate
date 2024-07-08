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
    USER_FRIENDS || --o{ STATUS_FRIENDSHIP : has
    USER_FRIENDS {
    Long user_id PK, FK
    Long friend_id FK
    Long status_id FK
    }
    STATUS_FRIENDSHIP {
    Long status_id PK, FK
    Enum status
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
    Long rating_id
    }
    FILM_GENRE || --o{ GENRES : has
    FILM_GENRE {
    Long film_id PK, FK
    Long genre_id FK
    }
    GENRES {
    Long genre_id PK, FK
    Enum genre
    }
    RATING {
    Long rating_id PK, FK
    Enum rating
    }
    LIKES {
    Long film_id PK, FK
    Long user_id
    }
```
