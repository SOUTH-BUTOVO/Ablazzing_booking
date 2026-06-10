insert into client (id, name, email)
values (1, 'Oleg', 'test1@mail.ru'),
       (2, 'Olga', 'test2@mail.ru'),
       (3, 'Ivan', 'test3@mail.ru');

insert into apartment (id, city, street, house, corpus, room_count)
values (1, 'Tula', 'Java', '7', '1A', 'ONE'),
       (2, 'Moscow', 'Kotlin', '9', '2B', 'TWO'),
       (3, 'Moscow', 'C++', '5', '3C', 'THREE');

insert into advert (id, price, is_active, apartment_id, description)
values (1, '10', true, 1, 'Один'),
       (2, '20', true, 2, 'Два'),
       (3, '30', true, 3, 'Три');

insert into booking (id, start_date, end_date, total_price, client_id, apartment_id, advert_id)
values (1, '2026.06.10', '2026.06.11', '100', 1, 1, 1),
       (2, '2026.06.11', '2026.06.13', '200', 2, 2, 2),
       (3, '2026.06.14', '2026.06.17', '300', 3, 3, 3);