# Todo Web App

Ứng dụng quản lý công việc (Todo) gồm 3 module: `todolist-api`, `todolist-fe`, `todolist-database`.

🌐 **Link Live Demo:** [http://nam23211.id.vn/](http://nam23211.id.vn/)

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


## 🚀 Triển khai bằng Docker (Production)

### Yêu cầu
- Docker & Docker Compose đã cài đặt
- Port 80, 8080, 3307 còn trống

### Cách chạy nhanh

```bash
# Clone project
git clone 
cd TodoList

# Xóa .env dev (để Docker ARG hoạt động)
rm todolist-fe/.env

# Chạy toàn bộ hệ thống (Frontend, Backend, Database)
docker-compose up -d --build
```

### Sau khi deploy

Hệ thống sẽ chạy tại:
- **Frontend:** http://localhost (port 80)
- **Backend API:** http://localhost:8080/api/v1
- **Database:** localhost:3307 (internal only)
- **Swagger UI:** http://localhost:8080/swagger-ui.html

### Verify

```bash
# Check services
docker-compose ps

# Check logs
docker-compose logs backend
docker-compose logs frontend

# Test API
curl http://localhost:8080/api/v1/todos
```
