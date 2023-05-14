ALTER SEQUENCE tags_id_seq RESTART WITH 1;
ALTER SEQUENCE gift_certificates_id_seq RESTART WITH 1;
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE orders_id_seq RESTART WITH 1;

INSERT INTO tags (name)
VALUES ('beauty'),
       ('relax'),
       ('massage');

INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date)
VALUES ('Beauty', 'Beauty gift certificate', 480.0, 365, '2023-05-06 19:41:14.325596', '2023-05-08 15:43:41.837196'),
       ('Spa', 'Spa gift certificate', 360.0, 365, '2023-05-06 19:42:08.515938', '2023-05-06 19:42:08.515938'),
       ('Massage', 'Massage gift certificate', 250.0, 180, '2023-05-06 19:41:31.707816', '2023-05-06 19:41:31.707816');

INSERT INTO gift_certificates_tags (gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 2),
       (3, 3);

INSERT INTO users (username, first_name, last_name, email)
VALUES ('ivan', 'Ivan', 'Ivanov', 'ivanov@mail.ru'),
       ('petr', 'Petr', 'Petrov', 'petrov@mail.ru');

INSERT INTO orders (price, date,  user_id, gift_certificate_id)
VALUES (480.0, '2023-05-08 15:30:30.146609', 1, 1),
       (480.0, '2023-05-08 15:30:50.186009', 1, 2),
       (250.0, '2023-05-13 14:41:39.383001', 1, 3),
       (480.0, '2023-05-08 15:32:30.910100', 2, 1),
       (360.0, '2023-05-13 10:27:28.186345', 2, 2);
