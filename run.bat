@echo off
REM College Event Management System - Run Script
REM This script runs the application

echo.
echo ====================================================
echo College Event Management System - Launcher
echo ====================================================
echo.

REM Set the path to MySQL JDBC driver
REM CHANGE THIS PATH TO YOUR JDBC DRIVER LOCATION
set JDBC_PATH=C:\lib\mysql-connector-j-8.0.33.jar

echo Checking for JDBC driver...
if not exist "%JDBC_PATH%" (
    echo ERROR: MySQL JDBC driver not found at %JDBC_PATH%
    echo Please update JDBC_PATH in this script.
    pause
    exit /b 1
)

echo Checking database connection...
echo.

cd src

REM Run the application
echo Starting application...
java -cp ".;%JDBC_PATH%" Main

if errorlevel 1 (
    echo.
    echo ERROR: Application failed to start
    echo Check database connection and verify JDBC driver is present
    pause
)

cd ..
