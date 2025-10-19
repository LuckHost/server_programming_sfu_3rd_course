#!/bin/bash

# Скрипт для выполнения нагрузочного тестирования

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
RESULTS_DIR="$SCRIPT_DIR/results"
LOG_FILE="$RESULTS_DIR/performance_test_$(date +%Y%m%d_%H%M%S).log"

mkdir -p "$RESULTS_DIR"

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1" | tee -a "$LOG_FILE"
}

run_query() {
    local test_name="$1"
    local output_file="$RESULTS_DIR/${test_name}_$(date +%H%M%S).json"
    
    log "Выполнение теста: $test_name"
    
    # Выполняем запрос и сохраняем план выполнения
    psql -h localhost -U lab_user -d lab_db -f "$SCRIPT_DIR/queries/test_query.sql" -o "$output_file" 2>&1 | tee -a "$LOG_FILE"
    
    # Извлекаем время выполнения из EXPLAIN ANALYZE
    local execution_time=$(grep "Execution Time" "$output_file" | tail -1 | awk '{print $3}')
    
    if [ -n "$execution_time" ]; then
        log "Время выполнения $test_name: ${execution_time}ms"
        echo "$test_name,${execution_time}" >> "$RESULTS_DIR/execution_times.csv"
    fi
    
    # Сохраняем план запроса
    if [ -f "$output_file" ]; then
        cp "$output_file" "$RESULTS_DIR/${test_name}_plan.json"
    fi
}

# Создаем файл для записи результатов
echo "test_name,execution_time_ms" > "$RESULTS_DIR/execution_times.csv"

log "=== НАЧАЛО ТЕСТИРОВАНИЯ ПРОИЗВОДИТЕЛЬНОСТИ ==="

# Удаляем существующие индексы для чистого теста
log "=== УДАЛЕНИЕ СУЩЕСТВУЮЩИХ ИНДЕКСОВ ==="
psql -h localhost -U lab_user -d lab_db -f "$SCRIPT_DIR/indexes/03_drop_indexes.sql" 2>&1 | tee -a "$LOG_FILE"

# Тест 1: Без индексов
log "=== ТЕСТ 1: ЗАПРОС БЕЗ ИНДЕКСОВ ==="
run_query "no_indexes"

# Создаем первый индекс
log "=== СОЗДАНИЕ ПЕРВОГО ИНДЕКСА ==="
psql -h localhost -U lab_user -d lab_db -f "$SCRIPT_DIR/indexes/01_create_first_index.sql" 2>&1 | tee -a "$LOG_FILE"

# Тест 2: С одним индексом
log "=== ТЕСТ 2: С ОДНИМ ИНДЕКСОМ ==="
run_query "one_index"

# Перестраиваем существующие индексы
log "=== ПЕРЕСТРОЙКА ИНДЕКСОВ ==="
# Сначала обычные индексы
psql -h localhost -U lab_user -d lab_db -f "$SCRIPT_DIR/indexes/02_rebuild_indexes.sql" 2>&1 | tee -a "$LOG_FILE"
# Затем частичные индексы (если нужно)
psql -h localhost -U lab_user -d lab_db -f "$SCRIPT_DIR/indexes/02_rebuild_partial_indexes.sql" 2>&1 | tee -a "$LOG_FILE"

# Тест 3: После перестроения индексов
log "=== ТЕСТ 3: ПОСЛЕ ПЕРЕСТРОЙКИ ИНДЕКСОВ ==="
run_query "rebuilt_indexes"

# Создаем все индексы
log "=== СОЗДАНИЕ ВСЕХ ИНДЕКСОВ ==="
psql -h localhost -U lab_user -d lab_db -f "$SCRIPT_DIR/indexes/01_create_indexes.sql" 2>&1 | tee -a "$LOG_FILE"

# Тест 4: Со всеми индексами
log "=== ТЕСТ 4: СО ВСЕМИ ИНДЕКСАМИ ==="
run_query "all_indexes"

# Финальная перестройка ВСЕХ индексов
log "=== ФИНАЛЬНАЯ ПЕРЕСТРОЙКА ИНДЕКСОВ ==="
psql -h localhost -U lab_user -d lab_db -f "$SCRIPT_DIR/indexes/02_rebuild_all_indexes.sql" 2>&1 | tee -a "$LOG_FILE"

# Тест 5: После финальной перестройки
log "=== ТЕСТ 5: ПОСЛЕ ФИНАЛЬНОЙ ПЕРЕСТРОЙКИ ==="
run_query "final_rebuilt"

# Финальная статистика
log "=== ФИНАЛЬНАЯ СТАТИСТИКА ИНДЕКСОВ ==="
psql -h localhost -U lab_user -d lab_db -c "
SELECT 
    schemaname,
    relname as tablename,
    indexrelname as index_name,
    idx_scan as index_scans,
    idx_tup_read as tuples_read,
    idx_tup_fetch as tuples_fetched
FROM pg_stat_user_indexes 
ORDER BY idx_scan DESC;
" > "$RESULTS_DIR/final_index_stats.txt"

log "=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ==="
log "Результаты сохранены в: $RESULTS_DIR"