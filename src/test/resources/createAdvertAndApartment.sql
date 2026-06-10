insert into apartment (id, city, street, house, corpus, room_count)
values (1, 'Tula', 'Java', '7', '1A', 'ONE'),
       (2, 'Moscow', 'Kotlin', '9', '2B', 'TWO'),
       (3, 'Moscow', 'C++', '5', '3C', 'THREE');

insert into advert (id, price, is_active, apartment_id, description)
values (1, '10', true, 1, 'Один'),
       (2, '20', true, 2, 'Два'),
       (3, '30', true, 3, 'Три');
