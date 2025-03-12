--liquibase formatted sql


--changeset fleur:1
CREATE TABLE film_collection_items
(
    film_collection_id BIGINT REFERENCES film_collections (id) NOT NULL,
    film_id            BIGINT REFERENCES films (id)            NOT NULL,
    added_at           TIMESTAMPTZ                             NOT NULL,
    position           INT                                     NOT NULL,
    PRIMARY KEY (film_collection_id, film_id)
);
--rollback DROP TABLE film_collection_items