#!/bin/bash

echo "=== ВЫПОЛНЕНИЕ ПРАКТИЧЕСКОЙ РАБОТЫ №3 ==="

# Временно убираем UNIQUE constraint с ISBN чтобы избежать конфликтов
echo "0. Временное отключение уникальности ISBN..."
psql -h localhost -U lab_user -d lab_db -c "ALTER TABLE books DROP CONSTRAINT IF EXISTS books_isbn_key;" 2>/dev/null || true

# Генерация массовых данных
echo "1. Генерация 500,000+ записей..."
psql -h localhost -U lab_user -d lab_db -f database/performance/01_generate_mass_data.sql

# Восстанавливаем UNIQUE constraint (опционально)
# echo "Восстановление уникальности ISBN..."
# psql -h localhost -U lab_user -d lab_db -c "ALTER TABLE books ADD CONSTRAINT books_isbn_unique UNIQUE (isbn);" 2>/dev/null || true

# Запуск производительного тестирования
echo "2. Запуск тестирования производительности..."
chmod +x database/performance/03_run_performance_test.sh
./database/performance/03_run_performance_test.sh

echo "3. Генерация отчета..."
psql -h localhost -U lab_user -d lab_db -c "
SELECT 
    schemaname,
    relname as tablename,
    attname as column_name,
    idx_scan as index_scans,
    idx_tup_read as tuples_read,
    idx_tup_fetch as tuples_fetched
FROM pg_stat_user_indexes 
JOIN pg_index USING (indexrelid)
JOIN pg_attribute ON attrelid = indrelid AND attnum = ANY(indkey)
ORDER BY idx_scan DESC;
" > database/performance/results/index_usage.txt

# Статистика по таблицам
psql -h localhost -U lab_user -d lab_db -c "
SELECT 
    schemaname,
    relname as table_name,
    n_live_tup as row_count,
    pg_size_pretty(pg_total_relation_size(relid)) as total_size,
    pg_size_pretty(pg_relation_size(relid)) as table_size,
    pg_size_pretty(pg_total_relation_size(relid) - pg_relation_size(relid)) as index_size
FROM pg_stat_user_tables 
ORDER BY n_live_tup DESC;
" > database/performance/results/table_stats.txt

echo "=== ПЗ №3 ВЫПОЛНЕНО ==="
echo "Результаты в database/performance/results/"