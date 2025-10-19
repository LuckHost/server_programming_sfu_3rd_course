#!/bin/bash

pg_dump -h localhost -U lab_user -d lab_db -F c -b -v -f "lab_db_backup_$(date +%Y%m%d_%H%M%S).dump"