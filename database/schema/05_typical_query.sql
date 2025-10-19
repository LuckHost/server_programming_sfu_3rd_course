-- Типовой запрос: поиск просроченных книг с информацией о пользователях и книгах
SELECT 
    u.first_name || ' ' || u.last_name AS user_name,
    u.email,
    b.title AS book_title,
    a.name AS author_name,
    bl.loan_date,
    bl.due_date,
    (CURRENT_DATE - bl.due_date) AS days_overdue
FROM book_loans bl
INNER JOIN users u ON bl.user_id = u.id
INNER JOIN books b ON bl.book_id = b.id
INNER JOIN authors a ON b.author_id = a.id
WHERE bl.return_date IS NULL 
    AND bl.due_date < CURRENT_DATE
    AND u.registration_date > '2023-01-01'
ORDER BY days_overdue DESC;