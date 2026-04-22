# TaskFlow ⚙️

A production-ready **Job Scheduling REST API** built with Spring Boot 3, Quartz Scheduler, JWT Authentication, and MySQL. Schedule recurring email tasks using cron expressions, secured behind stateless JWT-based auth.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.3.5 |
| Scheduler | Quartz Scheduler (JDBC store) |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Database | MySQL 8+ |
| ORM | Spring Data JPA / Hibernate |
| Build | Maven |
| Java | Java 17 |

---

## Features

- JWT registration & login with BCrypt password hashing
- Schedule jobs using Quartz cron expressions (persisted to DB)
- Pause, resume, and delete scheduled jobs at runtime
- List all scheduled jobs with their current status
- Global exception handling with structured JSON error responses
- Stateless, token-based authentication — no sessions

---

## Project Structure

```
src/main/java/com/taskflow/TaskFlow/
├── config/
│   └── SecurityConfig.java          # Spring Security + JWT filter chain
├── controller/
│   ├── AuthController.java          # /auth/register, /auth/login
│   └── JobController.java           # /jobs/* endpoints
├── dto/
│   ├── AuthRequest.java             # Login/register request body
│   └── JobRequest.java              # Job creation request body
├── entity/
│   ├── ScheduledJob.java            # Job metadata entity
│   ├── ScheduledJobStatus.java      # SCHEDULED / PAUSED / DELETED
│   └── User.java                    # User entity
├── exception/
│   └── GlobalExceptionHandler.java  # Centralized error responses
├── repository/
│   ├── JobRepository.java
│   └── UserRepository.java
├── scheduler/
│   └── EmailJob.java                # Quartz Job implementation
├── security/
│   ├── CustomUserDetailsService.java
│   ├── JwtFilter.java               # JWT validation filter
│   └── JwtUtil.java                 # Token generation & validation
├── service/
│   ├── AuthService.java
│   └── JobService.java              # Quartz scheduling logic
└── TaskFlowApplication.java
```

---

## Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8+

---

## Setup & Run

### 1. Clone the repository

```bash
git clone https://github.com/AleTejaswini/TaskFlow.git
cd taskflow
```

### 2. Create the database

```sql
CREATE DATABASE scheduler_db;
```

### 3. Configure `application.properties`

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/scheduler_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

jwt.secret=YOUR_SECRET_KEY_MIN_32_CHARACTERS_LONG
jwt.expiration=86400000
```

> ⚠️ The JWT secret must be **at least 32 characters** (256 bits) for HS256 to work.

### 4. Run the application

```bash
mvn spring-boot:run
```

The server starts on `http://localhost:8080`. Hibernate will auto-create all tables including Quartz's own schema on first run.

---

## API Reference

### Authentication

#### Register
```http
POST /auth/register
Content-Type: application/json

{
  "username": "john",
  "password": "secret123"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "john",
  "password": "secret123"
}
```
Returns a JWT token string. Use it in the `Authorization` header for all job endpoints.

---

### Jobs (require `Authorization: Bearer <token>`)

#### Create a job
```http
POST /jobs/create
Content-Type: application/json
Authorization: Bearer <token>

{
  "name": "dailyReport",
  "group": "emailGroup",
  "cron": "0 0 9 * * ?",
  "email": "john@example.com"
}
```

#### List all jobs
```http
GET /jobs/list
Authorization: Bearer <token>
```

#### Pause a job
```http
PUT /jobs/pause/{jobName}/{jobGroup}
Authorization: Bearer <token>
```

#### Resume a job
```http
PUT /jobs/resume/{jobName}/{jobGroup}
Authorization: Bearer <token>
```

#### Delete a job
```http
DELETE /jobs/delete/{jobName}/{jobGroup}
Authorization: Bearer <token>
```

---

## Cron Expression Reference

Quartz uses a **7-field cron** format: `seconds minutes hours day-of-month month day-of-week [year]`

| Expression | Schedule |
|---|---|
| `0 0 9 * * ?` | Every day at 9:00 AM |
| `0 0/30 * * * ?` | Every 30 minutes |
| `0 0 18 ? * MON-FRI` | Weekdays at 6:00 PM |
| `0 0 0 1 * ?` | 1st of every month at midnight |
| `0 0 12 ? * SUN` | Every Sunday at noon |
| `0 */5 * * * ?` | Every 5 minutes |

> Use `?` for either day-of-month **or** day-of-week — not both.

---

## Error Responses

All errors return a structured JSON body:

```json
{
  "timestamp": "2025-04-20T10:30:00",
  "status": 400,
  "error": "Username already taken: john"
}
```

---

## Environment Variables (recommended for production)

Instead of hardcoding credentials in `application.properties`, use environment variables:

```properties
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
```

Then set them before running:

```bash
export DB_PASSWORD=yourpassword
export JWT_SECRET=yourverylongsecretkeyhere
mvn spring-boot:run
```

---

## Developer

Ale Tejaswini
