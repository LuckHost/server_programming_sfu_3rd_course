#!/bin/bash

# Скрипт установки БД на чистой Arch Linux

echo "Установка PostgreSQL..."
sudo pacman -S --noconfirm postgresql postgresql-libs

echo "Инициализация БД..."
sudo -u postgres initdb -D /var/lib/postgres/data --locale=en_US.UTF-8
sudo systemctl enable postgresql.service
sudo systemctl start postgresql.service

echo "Создание пользователя и БД..."
sudo -u postgres psql -c "CREATE USER lab_user WITH PASSWORD 'lab_password';"
sudo -u postgres psql -c "CREATE DATABASE lab_db OWNER lab_user;"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE lab_db TO lab_user;"

echo "Импорт схемы и данных..."
psql -h localhost -U lab_user -d lab_db -f schema/01_create_tables.sql
psql -h localhost -U lab_user -d lab_db -f data/01_base_data.sql
psql -h localhost -U lab_user -d lab_db -f schema/03_add_constraints.sql
psql -h localhost -U lab_user -d lab_db -f data/02_generate_test_data.sql

echo "Готово! База данных настроена."