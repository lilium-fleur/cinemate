-- liquibase formatted sql


--changeset fleur:1
CREATE TABLE user_collections
(
    user_id       BIGINT REFERENCES users (id) ON DELETE CASCADE       NOT NULL,
    collection_id BIGINT REFERENCES collections (id) ON DELETE CASCADE NOT NULL,
    added_at      TIMESTAMPTZ                                          NOT NULL,
    PRIMARY KEY (user_id, collection_id)

);
--rollback DROP TABLE user_collections