-- liquibase formatted sql


--changeset fleur:1
CREATE TABLE film_genres
(
    film_id  BIGINT REFERENCES films (id) ON DELETE CASCADE  NOT NULL,
    genre_id BIGINT REFERENCES genres (id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (film_id, genre_id)
);

--rollback DROP TABLE films_genres