@echo off
setlocal
cd /d "%~dp0"

echo.
echo ==========================================================
echo   Starting Rooted (Gardening Community)...
echo   Requires only Java 17+. No Tomcat/MySQL setup needed!
echo ==========================================================
echo.

call .\mvnw.cmd compile exec:java
if errorlevel 1 (
    echo.
    echo [Error] Failed to start Rooted. Make sure Java 17+ is installed and on PATH.
    pause
    exit /b 1
)
