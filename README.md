## Общее описание

В этом задании вам нужно разработать сайт по покупке билетов в кинотеатр (один кинотеатр, а не сеть, для простоты). Подобные ресурсы имеют много логики. Ваша задача написать только:

Регистрацию/Вход;
Вывод киносеансов и фильмов;
Покупку билетов.

## Техническое задание

Для реализации нужно использовать: Spring Boot, Thymeleaf, Bootstrap, Liquibase, Sql2o, PostgreSQL (зависимости аналогичны проекту job4j_dreamjob).

Представления:

Главная страница. По аналогии с job4j_dreamjob выводите общую информацию о ресурсе;
Расписание. Выводите сеансы и связанные с ними фильмы. При выборе конкретного сеанса пользователь переходит на страницу покупки билета;
Кинотека. Выводите список фильмов;
Страница покупки билета. Выводите информацию о сеансе и фильм. Также 2 выпадающих списка - один для указания ряда, другой для указания места, и кнопки "Купить", "Отменить";
Страница с результатом успешной покупки билета. Выводите, сообщение пользователю, например, "Вы успешно приобрели билет на такое место ...";
Страница с результатом неудачной покупки билета (билет уже купили). Выводите, сообщение пользователю, например, "Не удалось приобрести билет на заданное место. Вероятно оно уже занято. Перейдите на страницу бронирования билетов и попробуйте снова.". Реализовать подобный функционал нужно аналогично регистрации пользователя;
Страница регистрации. Аналогично job4j_dreamjob;
Страница вход. Аналогично job4j_dreamjob.
Навигационная панель:

Лого. При клике на него выполняется переход на главную страницу;
Расписание. Выводите сеансы и связанные с ними фильмы;
Кинотека. Выводите список фильмов, которые показываются в кинотеатре;
Регистрация/Вход. Если пользователь не вошел в систему;
Имя пользователя/Выйти. Если пользователь вошел в систему.
Разделение прав:

Все пользователю имеют право просматривать информацию на сайте;
Только зарегистрированные пользователю могут покупать билеты. Если пользователь не зарегистрирован и нажимает на кнопку "Купить билет", то его перебрасывает на страницу входа.
Схема БД:


```postgresql
CREATE TABLE files (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    path VARCHAR NOT NULL UNIQUE
);

CREATE TABLE genres (
    id SERIAL PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE films (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    "year" INT NOT NULL,
    genre_id INT REFERENCES genres (id) NOT NULL,
    minimal_age INT NOT NULL,
    duration_in_minutes INT NOT NULL,
    file_id INT REFERENCES files (id) NOT NULL
);

CREATE TABLE halls (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    row_count INT NOT NULL,
    place_count INT NOT NULL,
    description VARCHAR NOT NULL
);

CREATE TABLE film_sessions (
    id SERIAL PRIMARY KEY,
    film_id INT REFERENCES films (id) NOT NULL,
    halls_id INT REFERENCES halls (id) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    price INT NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR NOT NULL,
    email VARCHAR unique NOT NULL,
    password VARCHAR NOT NULL
);

CREATE TABLE tickets (
    id SERIAL PRIMARY KEY,
    session_id INT REFERENCES film_sessions (id) NOT NULL,
    row_number INT NOT NULL,
    place_number INT NOT NULL,
    user_id INT NOT NULL,
    UNIQUE (session_id, row_number, place_number)
);
```
Нюансы:

Все таблицы кроме таблицы tickets и users заполняются с помощью скриптов SQL. Подобные данные заполняются администратором, а по заданию панель администратора нам писать НЕ нужно;
Таблица users имеет ограничение уникальности для email, поэтому при создании пользователя нужно возвращать Optional<User>;
Таблица tickets имеет ограничение уникальности unique (session_id, row_number, place_number). При создании билета нужно аналогично возвращать Optional<Ticket>. 