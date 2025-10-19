#!/bin/bash

# Более надежный скрипт с проверками

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

check_file() {
    if [ ! -f "$1" ]; then
        log "ОШИБКА: Файл $1 не найден!"
        return 1
    fi
    return 0
}

log "Начало установки БД..."

# Проверяем PostgreSQL
if ! command -v psql &> /dev/null; then
    log "Установка PostgreSQL..."
    sudo pacman -S --noconfirm postgresql postgresql-libs
fi

# Управление службой PostgreSQL
if ! sudo systemctl is-active --quiet postgresql; then
    log "Запуск PostgreSQL..."
    
    # Проверяем, нужно ли инициализировать БД
    if [ ! -d "/var/lib/postgres/data" ] || [ -z "$(ls -A /var/lib/postgres/data)" ]; then
        log "Инициализация БД..."
        sudo -u postgres initdb -D /var/lib/postgres/data --locale=en_US.UTF-8
    else
        log "БД уже инициализирована"
    fi
    
    sudo systemctl enable postgresql.service
    sudo systemctl start postgresql.service
    sleep 5
else
    log "PostgreSQL уже запущен"
fi

log "Настройка пользователя и БД..."
sudo -u postgres psql -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'lab_db';" 2>/dev/null || true
sudo -u postgres psql -c "DROP DATABASE IF EXISTS lab_db;" 2>/dev/null || true
sudo -u postgres psql -c "DROP USER IF EXISTS lab_user;" 2>/dev/null || true
sudo -u postgres psql -c "CREATE USER lab_user WITH PASSWORD 'lab_password';"
sudo -u postgres psql -c "CREATE DATABASE lab_db OWNER lab_user;"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE lab_db TO lab_user;"

log "Импорт структуры БД..."

# Проверяем существование файлов
SQL_FILES=(
    "$SCRIPT_DIR/schema/01_create_tables.sql"
    "$SCRIPT_DIR/data/01_base_data.sql" 
    "$SCRIPT_DIR/schema/03_add_constraints.sql"
    "$SCRIPT_DIR/data/02_generate_test_data.sql"
)

for file in "${SQL_FILES[@]}"; do
    if check_file "$file"; then
        log "Выполнение: $(basename "$file")"
        psql -h localhost -U lab_user -d lab_db -f "$file"
    else
        log "Пропускаем: $(basename "$file")"
    fi
done

log "Проверка установки..."
psql -h localhost -U lab_user -d lab_db -c "
SELECT 
    'Таблицы: ' || COUNT(*) as table_count
FROM information_schema.tables 
WHERE table_schema = 'public'
UNION ALL
SELECT 
    'Книги: ' || COUNT(*) 
FROM books
UNION ALL
SELECT 
    'Пользователи: ' || COUNT(*) 
FROM users;
"

log "Готово! База данных настроена."
echo "Данные для подключения:"
echo "Хост: localhost"
echo "БД: lab_db" 
echo "Пользователь: lab_user"
echo "Пароль: lab_password"