-- Подключаемся к БД как lab_user
-- psql -h localhost -U lab_user -d lab_db -f database/schema/01_create_tables.sql

-- Таблица жанров
CREATE TABLE genres (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

-- Таблица авторов
CREATE TABLE authors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    birth_date DATE,
    country VARCHAR(100)
);

-- Таблица пользователей
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    registration_date DATE NOT NULL DEFAULT CURRENT_DATE
);

-- Таблица книг
CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    publication_year INTEGER,
    author_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL
);

-- Таблица выдачи книг
CREATE TABLE book_loans (
    id BIGSERIAL PRIMARY KEY,
    book_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    loan_date DATE NOT NULL DEFAULT CURRENT_DATE,
    due_date DATE NOT NULL,
    return_date DATE
);