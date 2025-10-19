-- CROSS JOIN всех таблиц (демонстрационный запрос)
SELECT 
    b.title as book_title,
    a.name as author_name,
    g.name as genre_name,
    u.first_name as user_first_name,
    u.last_name as user_last_name,
    bl.loan_date,
    bl.due_date
FROM books b
CROSS JOIN authors a
CROSS JOIN genres g  
CROSS JOIN users u
CROSS JOIN book_loans bl
LIMIT 10;