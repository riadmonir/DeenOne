@echo off
echo ===================================================
echo DeenOne Database Exporter (Pure UTF-8 No BOM)
echo ===================================================
C:\xampp\mysql\bin\mysqldump.exe -u root --default-character-set=utf8mb4 --add-drop-table --skip-lock-tables --result-file="%~dp0deenone_db.sql" deenonet_db
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Database exported to server_backend/deenone_db.sql
) else (
    echo [ERROR] Export failed!
)
pause
