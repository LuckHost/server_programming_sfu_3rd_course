-- Типовой запрос для оптимизации
-- Поиск просроченных книг определенного жанра у активных пользователей

EXPLAIN (ANALYZE, BUFFERS, FORMAT JSON)
SELECT 
    u.first_name || ' ' || u.last_name AS user_name,
    u.email,
    b.title AS book_title,
    a.name AS author_name,
    g.name AS genre_name,
    bl.loan_date,
    bl.due_date,
    (CURRENT_DATE - bl.due_date) AS days_overdue
FROM book_loans bl
INNER JOIN users u ON bl.user_id = u.id
INNER JOIN books b ON bl.book_id = b.id
INNER JOIN authors a ON b.author_id = a.id
INNER JOIN genres g ON b.genre_id = g.id
WHERE bl.return_date IS NULL 
    AND bl.due_date < CURRENT_DATE
    AND g.name = 'Фантастика'
    AND u.registration_date > '2022-01-01'
    AND b.publication_year > 2000
    AND (CURRENT_DATE - bl.due_date) > 30
ORDER BY days_overdue DESC
LIMIT 100;