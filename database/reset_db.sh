#!/bin/bash

# Скрипт для пересоздания БД (если нужно очистить)

echo "Остановка подключений к БД..."
sudo -u postgres psql -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'lab_db';" 2>/dev/null || true

echo "Пересоздание БД..."
sudo -u postgres psql -c "DROP DATABASE IF EXISTS lab_db;"
sudo -u postgres psql -c "DROP USER IF EXISTS lab_user;"
sudo -u postgres psql -c "CREATE USER lab_user WITH PASSWORD 'lab_password';"
sudo -u postgres psql -c "CREATE DATABASE lab_db OWNER lab_user;"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE lab_db TO lab_user;"

echo "БД пересоздана. Запустите setup.sh для импорта данных."