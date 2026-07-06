@echo off
setlocal enabledelayedexpansion

:: Set working directory to the project root (database folder)
cd /d "%~dp0.."


:: ======================================
:: Create Database (via Docker)
:: ======================================
echo ======================================
echo Create Database
echo ======================================
echo.

:: Configuration (must match docker-compose.yaml)
set CONTAINER=db-todo
set DB_NAME=db_todo
set DB_USER=postgres
set DB_PASSWORD=123456

:: ======================================
:: Check Docker is running
:: ======================================
echo Checking Docker...
docker info >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker is not running.
    echo Please start Docker Desktop and try again.
    pause
    exit /b 1
)
echo    Docker is running.
echo.

:: ======================================
:: Check container is running
:: ======================================
echo Checking container "%CONTAINER%"...
docker ps --filter "name=%CONTAINER%" --filter "status=running" --format "{{.Names}}" > temp_container.txt 2>&1
set /p RUNNING=<temp_container.txt
del temp_container.txt 2>nul

if not "%RUNNING%"=="%CONTAINER%" (
    echo ERROR: Container "%CONTAINER%" is not running.
    echo Please run:  docker compose up -d
    pause
    exit /b 1
)
echo    Container "%CONTAINER%" is running.
echo.

:: ======================================
:: Check if database exists
:: ======================================
echo Checking if database "%DB_NAME%" exists...
docker exec -e PGPASSWORD=%DB_PASSWORD% %CONTAINER% psql -U %DB_USER% -d postgres -t -A -c "SELECT 1 FROM pg_database WHERE datname='%DB_NAME%';" > temp_check.txt 2>&1
set /p DB_EXISTS=<temp_check.txt
del temp_check.txt 2>nul

if "%DB_EXISTS%"=="1" (
    echo.
    echo WARNING: Database "%DB_NAME%" already exists!
    echo.
    echo What do you want to do?
    echo   1. Keep existing database ^(recommended^)
    echo   2. Drop and recreate ^(WARNING: DELETES ALL DATA^)
    echo   3. Cancel
    echo.
    set /p CHOICE="Enter choice (1-3): "

    if "!CHOICE!"=="1" (
        echo    Keeping existing database.
        goto :done
    )

    if "!CHOICE!"=="2" (
        echo.
        echo WARNING WARNING WARNING
        echo This will DELETE ALL DATA in "%DB_NAME%"!
        echo.
        set /p CONFIRM="Type 'YES' to confirm: "

        if /i "!CONFIRM!"=="YES" (
            echo Terminating active connections...
            docker exec -e PGPASSWORD=%DB_PASSWORD% %CONTAINER% psql -U %DB_USER% -d postgres -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '%DB_NAME%' AND pid <> pg_backend_pid();" >nul 2>&1

            echo Dropping database...
            docker exec -e PGPASSWORD=%DB_PASSWORD% %CONTAINER% psql -U %DB_USER% -d postgres -c "DROP DATABASE %DB_NAME%;" >nul 2>&1
            if errorlevel 1 (
                echo ERROR: Failed to drop database.
                pause
                exit /b 1
            )

            echo Creating fresh database...
            docker exec -e PGPASSWORD=%DB_PASSWORD% %CONTAINER% psql -U %DB_USER% -d postgres -c "CREATE DATABASE %DB_NAME%;" >nul 2>&1
            if errorlevel 1 (
                echo ERROR: Failed to create database.
                pause
                exit /b 1
            )

            echo    Database "%DB_NAME%" recreated successfully!
            goto :done
        ) else (
            echo Cancelled. Database kept unchanged.
            goto :done
        )
    )

    echo Cancelled.
    pause
    exit /b 0
)

:: ======================================
:: Database doesn't exist, create it
:: ======================================
echo Database "%DB_NAME%" does not exist. Creating...
docker exec -e PGPASSWORD=%DB_PASSWORD% %CONTAINER% psql -U %DB_USER% -d postgres -c "CREATE DATABASE %DB_NAME%;" >nul 2>&1

if errorlevel 1 (
    echo ERROR: Failed to create database.
    pause
    exit /b 1
)

echo    Database "%DB_NAME%" created successfully!

:done
echo.
echo ======================================
echo Database Info
echo ======================================
echo   Container : %CONTAINER%
echo   Database  : %DB_NAME%
echo   User      : %DB_USER%
echo   Port      : 3307 (host) -> 5432 (container)
echo.
echo ======================================
echo Next Steps
echo ======================================
echo   1. Run migrations: flyway migrate
echo   2. Check status:   flyway info
echo.
echo Your database is ready!
echo ======================================

pause
