-- Перестроение индексов (только существующих)

-- Перестраиваем только те индексы, которые существуют
DO $$ 
DECLARE
    index_record RECORD;
BEGIN
    FOR index_record IN 
        SELECT indexname 
        FROM pg_indexes 
        WHERE schemaname = 'public' 
        AND indexname LIKE 'idx_%'
    LOOP
        BEGIN
            EXECUTE 'REINDEX INDEX CONCURRENTLY ' || index_record.indexname;
            RAISE NOTICE 'Перестроен индекс: %', index_record.indexname;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Не удалось перестроить индекс: %', index_record.indexname;
        END;
    END LOOP;
END $$;

-- Обновление статистики только для пользовательских таблиц
ANALYZE authors, books, users, book_loans, genres;