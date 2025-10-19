-- Перестроение всех индексов (после создания всех)

REINDEX INDEX CONCURRENTLY idx_book_loans_return_date_due_date;
REINDEX INDEX CONCURRENTLY idx_book_loans_due_date;
REINDEX INDEX CONCURRENTLY idx_genres_name;
REINDEX INDEX CONCURRENTLY idx_users_registration_date;
REINDEX INDEX CONCURRENTLY idx_books_publication_year;
REINDEX INDEX CONCURRENTLY idx_book_loans_status;
REINDEX INDEX CONCURRENTLY idx_books_search;
REINDEX INDEX CONCURRENTLY idx_books_author_id;
REINDEX INDEX CONCURRENTLY idx_books_genre_id;
REINDEX INDEX CONCURRENTLY idx_book_loans_book_id;
REINDEX INDEX CONCURRENTLY idx_book_loans_user_id;

-- Обновление статистики
ANALYZE authors, books, users, book_loans, genres;