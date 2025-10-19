-- Создание индексов для оптимизации

-- 1. Индексы для условий WHERE
CREATE INDEX IF NOT EXISTS idx_book_loans_return_date ON book_loans(return_date) WHERE return_date IS NULL;
CREATE INDEX IF NOT EXISTS idx_book_loans_due_date ON book_loans(due_date);
CREATE INDEX IF NOT EXISTS idx_genres_name ON genres(name);
CREATE INDEX IF NOT EXISTS idx_users_registration_date ON users(registration_date);
CREATE INDEX IF NOT EXISTS idx_books_publication_year ON books(publication_year);

-- 2. Составные индексы для часто используемых комбинаций
CREATE INDEX IF NOT EXISTS idx_book_loans_status ON book_loans(return_date, due_date);
CREATE INDEX IF NOT EXISTS idx_books_search ON books(publication_year, genre_id);

-- 3. Индексы для JOIN полей
CREATE INDEX IF NOT EXISTS idx_books_author_id ON books(author_id);
CREATE INDEX IF NOT EXISTS idx_books_genre_id ON books(genre_id);
CREATE INDEX IF NOT EXISTS idx_book_loans_book_id ON book_loans(book_id);
CREATE INDEX IF NOT EXISTS idx_book_loans_user_id ON book_loans(user_id);