# School ERP System

A production-ready School ERP (Enterprise Resource Planning) system built with:

**Backend:** Java 17 · Spring Boot 3 · Spring Security 6 · OAuth2 (Google) · JWT · Spring Data JPA · MySQL · Lombok · Maven  
**Frontend:** React Native · Expo · Expo Router · NativeWind · Axios · Zustand · TanStack Query

---

## Project Structure

```
School_ERP/
├── backend/                        # Spring Boot application
│   ├── src/main/java/com/schoolerp/
│   │   ├── config/                 # SecurityConfig, OpenApiConfig, AuditConfig, WebConfig
│   │   ├── controller/             # REST controllers
│   │   ├── dto/
│   │   │   ├── request/            # Request DTOs
│   │   │   └── response/           # Response DTOs
│   │   ├── entity/                 # JPA entities
│   │   ├── enums/                  # Role, Gender, AttendanceStatus, ExamType, FeeStatus, etc.
│   │   ├── exception/              # GlobalExceptionHandler + custom exceptions
│   │   ├── repository/             # Spring Data JPA repositories
│   │   ├── security/               # JWT provider, filters, OAuth2 handlers
│   │   └── service/                # Business logic services
│   └── src/main/resources/
│       ├── application.yml
│       ├── application-dev.yml
│       └── application-prod.yml
│
└── frontend/                       # Expo/React Native application
    ├── app/
    │   ├── (auth)/                 # Login screen
    │   └── (app)/                  # Protected app screens
    │       ├── admin/              # Admin dashboard & student management
    │       ├── teacher/            # Teacher dashboard
    │       ├── student/            # Student dashboard
    │       ├── parent/             # Parent dashboard
    │       ├── attendance/         # Attendance screens
    │       ├── exams/              # Exam & marks screens
    │       ├── fees/               # Fee management screens
    │       ├── timetable/          # Timetable screens
    │       └── notifications/      # Notification screens
    ├── components/                 # Reusable UI components
    ├── constants/                  # App constants, colors, routes
    ├── hooks/                      # Custom React hooks
    ├── services/                   # Axios API service layer
    ├── store/                      # Zustand state management
    └── types/                      # TypeScript type definitions
```

---

## Backend Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+

### Configuration

1. Create MySQL database:
```sql
CREATE DATABASE school_erp_dev;
```

2. Set environment variables (or update `application-dev.yml`):
```bash
export DB_USERNAME=root
export DB_PASSWORD=yourpassword
export GOOGLE_CLIENT_ID=your-google-client-id
export GOOGLE_CLIENT_SECRET=your-google-client-secret
export JWT_SECRET=your-256-bit-secret-key-minimum-32-chars
```

3. Google OAuth2 setup:
   - Go to [Google Cloud Console](https://console.cloud.google.com)
   - Create OAuth2 credentials
   - Add authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`
   - Set `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET`

### Run Backend
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Backend starts at `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Frontend Setup

### Prerequisites
- Node.js 18+
- npm or yarn
- Expo CLI: `npm install -g expo-cli`

### Install & Run
```bash
cd frontend
npm install
cp .env.local .env
# Edit .env and set EXPO_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
npm start
```

- **Web:** Press `w` in the Expo terminal
- **Android:** Press `a` (requires Android Studio)
- **iOS:** Press `i` (requires Xcode on macOS)

---

## API Endpoints Summary

| Module | Endpoint | Methods |
|--------|----------|---------|
| Auth | `/api/v1/auth/refresh` | POST |
| Students | `/api/v1/students` | GET, POST, PUT, DELETE |
| Attendance | `/api/v1/attendance/mark` | POST |
| Attendance | `/api/v1/attendance/student/{id}` | GET |
| Exams | `/api/v1/exams` | GET, POST |
| Marks | `/api/v1/exams/marks` | POST |
| Fees | `/api/v1/fees` | GET, POST |
| Fees | `/api/v1/fees/{id}/pay` | POST |
| Timetable | `/api/v1/timetable` | GET, POST, DELETE |
| Notifications | `/api/v1/notifications` | GET, POST, PATCH |
| Admin | `/api/v1/admin/users` | GET, PATCH, DELETE |
| Files | `/api/v1/files/upload/*` | POST |

Full API documentation available at `/swagger-ui.html` after starting the backend.

---

## Security Flow

```
User → Google OAuth2 Login
     → Spring Security validates OAuth2 token
     → User upserted in MySQL (first login → create record)
     → JWT Access Token (24h) + Refresh Token (7d) issued
     → Frontend stores tokens in SecureStore
     → All API calls: Authorization: Bearer <token>
     → On 401 → auto-refresh using refresh token
     → Role-based access: ADMIN | TEACHER | STUDENT | PARENT
```

---

## Database Entities

- **User** — core identity (linked to Google OAuth2)
- **Student** — student profile linked to User
- **Teacher** — teacher profile linked to User
- **Parent** — parent profile linked to User
- **ClassRoom** — class with teacher, students, subjects
- **Subject** — subject with teachers and classrooms (M2M)
- **Attendance** — daily attendance per student per subject
- **Exam** — exam details per subject per classroom
- **Marks** — student marks per exam
- **Fees** — fee records with payment tracking
- **Timetable** — weekly schedule per classroom
- **Notification** — in-app notifications per user

---

## Roles & Permissions

| Feature | ADMIN | TEACHER | STUDENT | PARENT |
|---------|-------|---------|---------|--------|
| Manage Users | ✅ | ❌ | ❌ | ❌ |
| Manage Students | ✅ | ❌ | ❌ | ❌ |
| Mark Attendance | ✅ | ✅ | ❌ | ❌ |
| View Attendance | ✅ | ✅ | ✅ (own) | ✅ (child) |
| Create Exams | ✅ | ✅ | ❌ | ❌ |
| Enter Marks | ✅ | ✅ | ❌ | ❌ |
| View Marks | ✅ | ✅ | ✅ (own) | ✅ (child) |
| Manage Fees | ✅ | ❌ | ❌ | ❌ |
| View Fees | ✅ | ❌ | ✅ (own) | ✅ (child) |
| Manage Timetable | ✅ | ❌ | ❌ | ❌ |
| Send Notifications | ✅ | ❌ | ❌ | ❌ |

---

## Production Deployment

1. Set `spring.profiles.active=prod` in environment
2. Set `DATABASE_URL`, `DB_USERNAME`, `DB_PASSWORD` env vars
3. Set `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `JWT_SECRET`
4. Build: `mvn clean package -DskipTests`
5. Run: `java -jar target/school-erp-backend-1.0.0.jar`
