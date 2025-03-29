-- liquibase formatted sql


--changeset fleur:1
CREATE TABLE pending_delete(
    record_id BIGINT NOT NULL ,
    entity_name VARCHAR NOT NULL,
    PRIMARY KEY (record_id, entity_name)
);
--rollback DROP TABLE pending_delete