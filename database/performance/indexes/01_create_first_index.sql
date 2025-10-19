-- Создание только первого индекса для поэтапного тестирования
CREATE INDEX IF NOT EXISTS idx_book_loans_return_date_due_date 
ON book_loans(return_date, due_date) WHERE return_date IS NULL;