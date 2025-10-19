-- Генерация дополнительных авторов (500 записей)
INSERT INTO authors (name, birth_date, country)
SELECT 
    'Автор ' || generate_series,
    DATE '1900-01-01' + (random() * 40000)::integer,
    CASE (random() * 9)::integer
        WHEN 0 THEN 'Россия'
        WHEN 1 THEN 'США' 
        WHEN 2 THEN 'Великобритания'
        WHEN 3 THEN 'Франция'
        WHEN 4 THEN 'Германия'
        WHEN 5 THEN 'Япония'
        WHEN 6 THEN 'Китай'
        WHEN 7 THEN 'Италия'
        WHEN 8 THEN 'Испания'
        ELSE 'Канада'
    END
FROM generate_series(11, 510);

-- Генерация дополнительных пользователей (2000 записей)
INSERT INTO users (email, first_name, last_name, registration_date)
SELECT 
    'user' || generate_series || '@example.com',
    CASE (random() * 9)::integer
        WHEN 0 THEN 'Александр'
        WHEN 1 THEN 'Екатерина'
        WHEN 2 THEN 'Михаил'
        WHEN 3 THEN 'Анна'
        WHEN 4 THEN 'Дмитрий'
        WHEN 5 THEN 'Ольга'
        WHEN 6 THEN 'Сергей'
        WHEN 7 THEN 'Ирина'
        WHEN 8 THEN 'Андрей'
        ELSE 'Наталья'
    END,
    CASE (random() * 9)::integer
        WHEN 0 THEN 'Иванов'
        WHEN 1 THEN 'Петров'
        WHEN 2 THEN 'Сидоров'
        WHEN 3 THEN 'Кузнецов'
        WHEN 4 THEN 'Смирнов'
        WHEN 5 THEN 'Попов'
        WHEN 6 THEN 'Лебедев'
        WHEN 7 THEN 'Козлов'
        WHEN 8 THEN 'Новиков'
        ELSE 'Морозов'
    END,
    CURRENT_DATE - (random() * 365 * 3)::integer
FROM generate_series(11, 2010);

-- Генерация дополнительных книг (5000 записей)
INSERT INTO books (title, isbn, publication_year, author_id, genre_id)
SELECT 
    'Книга ' || generate_series || ' - ' || substr(md5(random()::text), 1, 8),
    '978-' || (random() * 899 + 100)::integer || '-' || 
    (random() * 8999 + 1000)::integer || '-' || 
    (random() * 89 + 10)::integer || '-' || 
    (random() * 9 + 1)::integer,
    (random() * 300 + 1700)::integer,
    (random() * 509 + 1)::integer,  -- author_id
    (random() * 9 + 1)::integer     -- genre_id
FROM generate_series(11, 5010);

-- Генерация выдачи книг (3000 записей) - АЛЬТЕРНАТИВНЫЙ ВАРИАНТ
INSERT INTO book_loans (book_id, user_id, loan_date, due_date, return_date)
WITH loan_dates AS (
    SELECT 
        generate_series as id,
        CURRENT_DATE - (random() * 365)::integer as loan_date
    FROM generate_series(1, 3000)
)
SELECT 
    (random() * 5009 + 1)::integer as book_id,
    (random() * 2009 + 1)::integer as user_id,
    loan_date,
    loan_date + 30 as due_date,  -- гарантированно через 30 дней после loan_date
    CASE 
        WHEN random() > 0.3 THEN 
            loan_date + (random() * 25 + 5)::integer  -- возврат через 5-30 дней
        ELSE NULL
    END as return_date
FROM loan_dates;