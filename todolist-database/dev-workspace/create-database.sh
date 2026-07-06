#!/bin/bash

# Configuration
CONTAINER="db-todo"
DB_NAME="db_todo"
DB_USER="postgres"
DB_PASSWORD="123456"

echo "======================================"
echo "Create E-commerce Database"
echo "======================================"

# Check Docker is running
echo "Checking Docker..."
if ! docker info > /dev/null 2>&1; then
    echo "ERROR: Docker is not running."
    exit 1
fi
echo "Docker is running."

# Check container is running
echo "Checking container '$CONTAINER'..."
RUNNING=$(docker ps --filter "name=$CONTAINER" --filter "status=running" --format "{{.Names}}")
if [ "$RUNNING" != "$CONTAINER" ]; then
    echo "ERROR: Container '$CONTAINER' is not running."
    echo "Please run: docker compose up -d"
    exit 1
fi
echo "Container '$CONTAINER' is running."

# Check if database exists
echo "Checking if database '$DB_NAME' exists..."
DB_EXISTS=$(docker exec -e PGPASSWORD=$DB_PASSWORD $CONTAINER psql -U $DB_USER -d postgres -t -A -c "SELECT 1 FROM pg_database WHERE datname='$DB_NAME';")

if [ "$DB_EXISTS" = "1" ]; then
    echo "WARNING: Database '$DB_NAME' already exists!"
    echo "1. Keep existing database (recommended)"
    echo "2. Drop and recreate (WARNING: DELETES ALL DATA)"
    echo "3. Cancel"
    read -p "Enter choice (1-3): " CHOICE

    if [ "$CHOICE" = "1" ]; then
        echo "Keeping existing database."
    elif [ "$CHOICE" = "2" ]; then
        read -p "Type 'YES' to confirm: " CONFIRM
        CONFIRM=$(echo "$CONFIRM" | tr -d '\r')
        if [ "$CONFIRM" = "YES" ]; then
            echo "Terminating active connections..."
            docker exec -e PGPASSWORD=$DB_PASSWORD $CONTAINER psql -U $DB_USER -d postgres -c \
                "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '$DB_NAME' AND pid <> pg_backend_pid();" > /dev/null 2>&1

            echo "Dropping database..."
            docker exec -e PGPASSWORD=$DB_PASSWORD $CONTAINER psql -U $DB_USER -d postgres -c "DROP DATABASE $DB_NAME;" > /dev/null 2>&1

            echo "Creating fresh database..."
            docker exec -e PGPASSWORD=$DB_PASSWORD $CONTAINER psql -U $DB_USER -d postgres -c "CREATE DATABASE $DB_NAME;" > /dev/null 2>&1
            echo "Database '$DB_NAME' recreated successfully!"
        else
            echo "Cancelled."
        fi
    else
        echo "Cancelled."
        exit 0
    fi
else
    echo "Database '$DB_NAME' does not exist. Creating..."
    docker exec -e PGPASSWORD=$DB_PASSWORD $CONTAINER psql -U $DB_USER -d postgres -c "CREATE DATABASE $DB_NAME;" > /dev/null 2>&1
    echo "Database '$DB_NAME' created successfully!"
fi

echo "======================================"
echo "Database Info"
echo "======================================"
echo "  Container : $CONTAINER"
echo "  Database  : $DB_NAME"
echo "  User      : $DB_USER"
echo "  Port      : 3307 (host) -> 5432 (container)"
echo "======================================"