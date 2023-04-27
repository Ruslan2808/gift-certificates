CREATE TABLE IF NOT EXISTS gift_certificate (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    description      VARCHAR(255) NOT NULL,
    price            NUMERIC      NOT NULL,
    duration         INTEGER      NOT NULL,
    create_date      TIMESTAMP    NOT NULL,
    last_update_date TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS tag (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS gift_certificate_tag (
    gift_certificate_id BIGINT NOT NULL,
    tag_id              BIGINT NOT NULL,
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id),
    FOREIGN KEY (tag_id) REFERENCES tag (id)
);