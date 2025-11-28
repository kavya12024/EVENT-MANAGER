@echo off
REM College Event Management System - Compile Script
REM This script compiles all Java files for Windows

echo.
echo ====================================================
echo College Event Management System - Java Compiler
echo ====================================================
echo.

REM Set the path to MySQL JDBC driver
REM CHANGE THIS PATH TO YOUR JDBC DRIVER LOCATION
set JDBC_PATH=C:\lib\mysql-connector-j-8.0.33.jar

echo Checking for JDBC driver at: %JDBC_PATH%
if not exist "%JDBC_PATH%" (
    echo ERROR: MySQL JDBC driver not found!
    echo Please download mysql-connector-j-8.x.x.jar from:
    echo https://dev.mysql.com/downloads/connector/j/
    echo.
    echo Then update JDBC_PATH in this script.
    pause
    exit /b 1
)

echo Found JDBC driver. Starting compilation...
echo.

cd src

echo [1/3] Compiling Database Connection...
javac -cp "%JDBC_PATH%" db/DBConnection.java
if errorlevel 1 (
    echo ERROR: Failed to compile DBConnection.java
    pause
    exit /b 1
)

echo [2/3] Compiling Model Classes...
javac -cp "%JDBC_PATH%" models/*.java
if errorlevel 1 (
    echo ERROR: Failed to compile models
    pause
    exit /b 1
)

echo [3/3] Compiling UI Classes...
javac -cp ".;%JDBC_PATH%" ui/*.java
if errorlevel 1 (
    echo ERROR: Failed to compile UI classes
    pause
    exit /b 1
)

echo.
echo Compiling Main entry point...
javac -cp ".;%JDBC_PATH%" Main.java
if errorlevel 1 (
    echo ERROR: Failed to compile Main.java
    pause
    exit /b 1
)

cd ..

echo.
echo ====================================================
echo Compilation completed successfully!
echo ====================================================
echo.
echo To run the application, use:
echo java -cp ".;%JDBC_PATH%" Main
echo.
pause
