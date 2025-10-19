-- Генерация массовых данных (500,000+ записей)

-- 1. Дополнительные авторы (10,000)
INSERT INTO authors (name, birth_date, country)
SELECT 
    'Автор ' || (1000 + generate_series) || ' ' || substr(md5(random()::text), 1, 6),
    DATE '1950-01-01' + (random() * 20000)::integer,
    CASE (random() * 9)::integer
        WHEN 0 THEN 'Россия' WHEN 1 THEN 'США' WHEN 2 THEN 'Великобритания'
        WHEN 3 THEN 'Франция' WHEN 4 THEN 'Германия' WHEN 5 THEN 'Япония'
        WHEN 6 THEN 'Китай' WHEN 7 THEN 'Италия' WHEN 8 THEN 'Испания'
        ELSE 'Канада'
    END
FROM generate_series(1, 10000)
ON CONFLICT DO NOTHING;

-- 2. Дополнительные пользователи (100,000)
INSERT INTO users (email, first_name, last_name, registration_date)
SELECT 
    'user' || (10000 + generate_series) || '@library.com',
    CASE (random() * 14)::integer
        WHEN 0 THEN 'Александр' WHEN 1 THEN 'Екатерина' WHEN 2 THEN 'Михаил'
        WHEN 3 THEN 'Анна' WHEN 4 THEN 'Дмитрий' WHEN 5 THEN 'Ольга'
        WHEN 6 THEN 'Сергей' WHEN 7 THEN 'Ирина' WHEN 8 THEN 'Андрей'
        WHEN 9 THEN 'Наталья' WHEN 10 THEN 'Владимир' WHEN 11 THEN 'Елена'
        WHEN 12 THEN 'Алексей' WHEN 13 THEN 'Мария' ELSE 'Юлия'
    END,
    CASE (random() * 14)::integer
        WHEN 0 THEN 'Иванов' WHEN 1 THEN 'Петров' WHEN 2 THEN 'Сидоров'
        WHEN 3 THEN 'Кузнецов' WHEN 4 THEN 'Смирнов' WHEN 5 THEN 'Попов'
        WHEN 6 THEN 'Лебедев' WHEN 7 THEN 'Козлов' WHEN 8 THEN 'Новиков'
        WHEN 9 THEN 'Морозов' WHEN 10 THEN 'Волков' WHEN 11 THEN 'Алексеев'
        WHEN 12 THEN 'Семенов' WHEN 13 THEN 'Егоров' ELSE 'Павлов'
    END,
    CURRENT_DATE - (random() * 365 * 5)::integer
FROM generate_series(1, 100000)
ON CONFLICT DO NOTHING;

-- 3. Дополнительные книги (300,000) - ИСПРАВЛЕННАЯ ВЕРСИЯ
INSERT INTO books (title, isbn, publication_year, author_id, genre_id)
WITH author_ids AS (
    SELECT id FROM authors ORDER BY id
),
total_authors AS (
    SELECT COUNT(*) as count FROM author_ids
)
SELECT 
    'Книга ' || generate_series || ': ' || 
    CASE (random() * 9)::integer
        WHEN 0 THEN 'Путешествие' WHEN 1 THEN 'Тайна' WHEN 2 THEN 'История'
        WHEN 3 THEN 'Наука' WHEN 4 THEN 'Искусство' WHEN 5 THEN 'Философия'
        WHEN 6 THEN 'Технологии' WHEN 7 THEN 'Природа' WHEN 8 THEN 'Культура'
        ELSE 'Общество'
    END || ' ' || substr(md5(random()::text), 1, 8),
    'TEMP-' || generate_series || '-' || substr(md5(random()::text), 1, 6),
    (random() * 100 + 1920)::integer,
    (SELECT id FROM author_ids OFFSET (random() * (SELECT count FROM total_authors))::integer LIMIT 1),
    (random() * 9 + 1)::integer
FROM generate_series(1, 300000)
ON CONFLICT DO NOTHING;

-- 4. Дополнительные выдачи книг (100,000) - ИСПРАВЛЕННАЯ ВЕРСИЯ
INSERT INTO book_loans (book_id, user_id, loan_date, due_date, return_date)
WITH book_ids AS (
    SELECT id FROM books ORDER BY id
),
user_ids AS (
    SELECT id FROM users ORDER BY id  
),
total_books AS (
    SELECT COUNT(*) as count FROM book_ids
),
total_users AS (
    SELECT COUNT(*) as count FROM user_ids
),
loan_data AS (
    SELECT 
        generate_series as id,
        (SELECT id FROM book_ids OFFSET (random() * (SELECT count FROM total_books))::integer LIMIT 1) as book_id,
        (SELECT id FROM user_ids OFFSET (random() * (SELECT count FROM total_users))::integer LIMIT 1) as user_id,
        CURRENT_DATE - (random() * 365 * 3)::integer as loan_date
    FROM generate_series(1, 100000)
)
SELECT 
    book_id,
    user_id,
    loan_date,
    loan_date + 30 as due_date,
    CASE 
        WHEN random() > 0.4 THEN 
            loan_date + (random() * 25 + 5)::integer
        ELSE NULL
    END as return_date
FROM loan_data
ON CONFLICT DO NOTHING;

-- Проверка общего количества записей
SELECT 
    'Авторы: ' || COUNT(*) as count FROM authors
UNION ALL
SELECT 'Пользователи: ' || COUNT(*) FROM users
UNION ALL
SELECT 'Книги: ' || COUNT(*) FROM books
UNION ALL
SELECT 'Выдачи: ' || COUNT(*) FROM book_loans;