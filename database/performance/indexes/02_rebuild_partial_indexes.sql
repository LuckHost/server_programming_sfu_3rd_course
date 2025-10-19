-- Перестроение частичных индексов (требует обычного REINDEX)

-- Перестраиваем частичные индексы обычным REINDEX
DO $$ 
DECLARE
    index_record RECORD;
BEGIN
    FOR index_record IN 
        SELECT indexname, indexdef
        FROM pg_indexes 
        WHERE schemaname = 'public' 
        AND indexname LIKE 'idx_%'
        AND indexdef LIKE '%WHERE%'
    LOOP
        BEGIN
            EXECUTE 'REINDEX INDEX ' || index_record.indexname;
            RAISE NOTICE 'Перестроен частичный индекс: %', index_record.indexname;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Не удалось перестроить частичный индекс: %', index_record.indexname;
        END;
    END LOOP;
END $$;