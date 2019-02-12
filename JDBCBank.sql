drop table users;

create table users 
(
username varchar2(255) not null unique,
user_id number primary key not null,
password varchar2(255), 
permissions number(3),
funds number
);

CREATE SEQUENCE id_numbers
START WITH 10
INCREMENT BY 1;

insert into users (username, user_id, password, permissions) values  ('temp', 1, 'temp', '0');
insert into users (username, user_id, password, permissions, funds) values  ('root', 2, 'notpassword', '2', 0);
insert into users (username, user_id, password, permissions, funds) values  ('steve', 3, 'password', '1', 0);
insert into users (username, user_id, password, permissions, funds) values  ('kevin', 4, 'hello', '1', 0);
insert into users (username, user_id, password, permissions, funds) values  ('reginald', 5, 'goodbye', '1', 0);

select * from users