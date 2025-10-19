-- Добавляем внешние ключи
ALTER TABLE books 
ADD CONSTRAINT fk_books_authors 
FOREIGN KEY (author_id) REFERENCES authors(id);

ALTER TABLE books 
ADD CONSTRAINT fk_books_genres 
FOREIGN KEY (genre_id) REFERENCES genres(id);

ALTER TABLE book_loans 
ADD CONSTRAINT fk_loans_books 
FOREIGN KEY (book_id) REFERENCES books(id);

ALTER TABLE book_loans 
ADD CONSTRAINT fk_loans_users 
FOREIGN KEY (user_id) REFERENCES users(id);

-- Добавляем ограничения (ОБНОВЛЕННАЯ ВЕРСИЯ)
ALTER TABLE book_loans 
ADD CONSTRAINT chk_due_date_after_loan 
CHECK (due_date >= loan_date);  -- изменили на >= вместо >

ALTER TABLE books 
ADD CONSTRAINT chk_publication_year 
CHECK (publication_year BETWEEN 1500 AND EXTRACT(YEAR FROM CURRENT_DATE));

-- Дополнительное ограничение для return_date
ALTER TABLE book_loans 
ADD CONSTRAINT chk_return_date_after_loan 
CHECK (return_date IS NULL OR return_date >= loan_date);