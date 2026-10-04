@echo off
title DeenOne Local Offline PHP Server
cls
echo =====================================================================
echo              DEEN ONE (দীন ওয়ান) - LOCAL OFFLINE SERVER
echo =====================================================================
echo.
echo [INFO] Starting PHP Built-in Server on 0.0.0.0:8000...
echo.
echo Server accessible from:
echo   - Local PC:            http://127.0.0.1:8000/api/
echo   - Android Emulator:    http://10.0.2.2:8000/api/
echo   - Local Wi-Fi (Phone): http://192.168.100.251:8000/api/
echo.
echo Press Ctrl+C to stop the server at any time.
echo =====================================================================
echo.
C:\xampp\php\php.exe -S 0.0.0.0:8000 -t "%~dp0"
pause
