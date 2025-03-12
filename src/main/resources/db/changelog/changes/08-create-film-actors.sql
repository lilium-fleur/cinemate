--liquibase formatted sql


--changeset fleur:1
CREATE TABLE film_actors
(
    film_id  BIGINT REFERENCES films (id) ON DELETE CASCADE  NOT NULL,
    actor_id BIGINT REFERENCES actors (id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (film_id, actor_id)
);
--rollback DROP TABLE actor_films