@echo off
title Event Management System - Runner
color 0A

echo ================================================================
echo          Starting Event Management System (EventPulse)
echo ================================================================
echo.

:: 1. Check if MySQL is running
echo [1/3] Checking MySQL Service...
sc query MySQL12 | find "RUNNING" >nul
if %ERRORLEVEL% EQU 0 (
    echo [OK] MySQL Service is running.
) else (
    sc query MySQL80 | find "RUNNING" >nul
    if %ERRORLEVEL% EQU 0 (
        echo [OK] MySQL Service is running.
    ) else (
        echo [INFO] Attempting to start MySQL service if stopped...
        net start MySQL12 >nul 2>&1
        net start MySQL80 >nul 2>&1
    )
)

:: 2. Check if JAR exists, else build it
echo [2/3] Checking application package...
if not exist "target\event-management-system-1.0.0.jar" (
    echo [INFO] JAR not found. Compiling project with Maven...
    call mvnw.cmd package -DskipTests
)

:: 3. Run Application
echo.
echo [3/3] Launching Event Management System on http://localhost:8080
echo.
echo Press Ctrl + C anytime in this window to stop the server.
echo.
echo Open your browser at: http://localhost:8080
echo ================================================================
echo.

java -jar target\event-management-system-1.0.0.jar

pause
