-- Перестроение индексов (только существующих)

-- Перестраиваем только те индексы, которые существуют
DO $$ 
DECLARE
    index_record RECORD;
BEGIN
    FOR index_record IN 
        SELECT indexname, indexdef
        FROM pg_indexes 
        WHERE schemaname = 'public' 
        AND indexname LIKE 'idx_%'
    LOOP
        BEGIN
            -- Для частичных индексов используем обычный REINDEX
            IF index_record.indexdef LIKE '%WHERE%' THEN
                EXECUTE 'REINDEX INDEX ' || index_record.indexname;
                RAISE NOTICE 'Перестроен частичный индекс: %', index_record.indexname;
            ELSE
                EXECUTE 'REINDEX INDEX CONCURRENTLY ' || index_record.indexname;
                RAISE NOTICE 'Перестроен индекс: %', index_record.indexname;
            END IF;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Не удалось перестроить индекс: % (%%)', index_record.indexname, SQLERRM;
        END;
    END LOOP;
END $$;

-- Обновление статистики только для пользовательских таблиц
ANALYZE authors, books, users, book_loans, genres;