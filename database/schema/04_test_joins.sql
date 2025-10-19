-- INNER JOIN: книги с авторами и жанрами
SELECT 
    b.title,
    a.name as author,
    g.name as genre,
    b.publication_year
FROM books b
INNER JOIN authors a ON b.author_id = a.id
INNER JOIN genres g ON b.genre_id = g.id;

-- LEFT JOIN: все книги, даже если нет выдачи
SELECT 
    b.title,
    a.name as author,
    bl.loan_date,
    u.first_name,
    u.last_name
FROM books b
LEFT JOIN book_loans bl ON b.id = bl.book_id
LEFT JOIN users u ON bl.user_id = u.id
LEFT JOIN authors a ON b.author_id = a.id;

-- RIGHT JOIN: все пользователи, даже если у них нет выданных книг
SELECT 
    u.first_name,
    u.last_name,
    b.title,
    bl.loan_date
FROM book_loans bl
RIGHT JOIN users u ON bl.user_id = u.id
LEFT JOIN books b ON bl.book_id = b.id;

-- FULL JOIN: все книги и все выдачи
SELECT 
    b.title,
    bl.loan_date,
    u.first_name,
    u.last_name
FROM books b
FULL JOIN book_loans bl ON b.id = bl.book_id
FULL JOIN users u ON bl.user_id = u.id;