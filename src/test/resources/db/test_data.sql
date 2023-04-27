INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('Spa', 'Spa gift certificate', 150.0, 90, '2023-04-26 16:27:40.0156', '2023-04-26 16:27:40.0156'),
       ('Massage', 'Massage gift certificate', 75.0, 180, '2023-04-27 11:44:13.0863', '2023-04-27 11:44:13.0863'),
       ('Book', 'Book gift certificate', 50.0, 120, '2023-04-27 12:15:10.0654', '2023-04-27 12:15:10.0654');

INSERT INTO tag (name)
VALUES ('health'),
       ('beauty'),
       ('happiness'),
       ('massage'),
       ('book');

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 4),
       (3, 3),
       (3, 5);