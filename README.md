# Todo Web App

Ứng dụng quản lý công việc (Todo) gồm 3 module: `todolist-api`, `todolist-fe`, `todolist-database`.

## Tech Stack

### Backend (`todolist-api`)

- Java 21
- Spring Boot 3.4
- Spring Web, Spring Security, Spring Data JPA
- PostgreSQL Driver
- Lombok, MapStruct
- SpringDoc OpenAPI (Swagger UI)
- Maven

### Frontend (`todolist-fe`)

- React 19, TypeScript
- Vite
- Redux Toolkit, React Router
- Material UI, Tailwind CSS
- Lucide React

### Database (`todolist-database`)

- PostgreSQL 16 (Docker)
- Flyway (migration)

## Chạy local

### Yêu cầu

- Docker
- Java 21
- Maven
- Node.js
- [Flyway CLI](https://documentation.red-gate.com/fd/command-line-184127404.html)

### 1. Database

```bash
cd todolist-database
docker compose up -d
```

Tạo database:

```bash
# Windows
dev-workspace\create-database.bat

# macOS / Linux
./dev-workspace/create-database.sh
```

Chạy migration:

```bash
flyway -configFiles=flyway.conf,env/flyway.local.conf migrate
```

PostgreSQL chạy tại `localhost:3307`, database `db_todo`, user `postgres`, password `123456`.

### 2. Backend

```bash
cd todolist-api
./mvnw spring-boot:run
```

API chạy tại `http://localhost:8080/api/v1`. Swagger UI: `http://localhost:8080/swagger-ui.html`.

#### Chạy unit test

```bash
./mvnw test
```

### 3. Frontend

```bash
cd todolist-fe
npm install
npm run dev
```

Frontend chạy tại `http://localhost:5173`. File `.env` đã cấu hình `VITE_SERVER_URI=http://localhost:8080/api/v1`.
