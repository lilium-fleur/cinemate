-- liquibase formatted sql

--changeset fleur:1
CREATE TABLE user_similarities
(
    user1_id   BIGINT REFERENCES users (id) NOT NULL,
    user2_id   BIGINT REFERENCES users (id) NOT NULL,
    similarity DOUBLE PRECISION             NOT NULL,
    PRIMARY KEY (user1_id, user2_id)
)
--rollback DROP TABLE user_similarities