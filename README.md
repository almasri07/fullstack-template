# Fullstack Template

Wiederverwendbares Template für Web-Anwendungen mit Spring Boot, React TypeScript und PostgreSQL.

## Stack

| Layer | Technologie |
|-------|------------|
| Backend | Spring Boot 3.2, Java 21 |
| Frontend | React 18, TypeScript, Vite |
| Datenbank | PostgreSQL 15 |
| Auth | Spring Security + JWT |
| DB-Migrationen | Flyway |
| State Management | Zustand |
| Container | Docker + Docker Compose |
| CI/CD | GitHub Actions |

## Schnellstart (lokal)

```bash
# 1. Repository klonen / als Template verwenden
git clone <your-repo-url>
cd <project-name>

# 2. Umgebungsvariablen setzen
cp .env.example .env
# .env anpassen (Passwörter, JWT Secret)

# 3. Alles starten
docker compose up --build
```

| Service | URL |
|---------|-----|
| Frontend | http://localhost:5173 |
| Backend API | http://localhost:8080/api |
| PostgreSQL | localhost:5432 |

## Ohne Docker (Entwicklung)

```bash
# PostgreSQL lokal starten (z.B. via Homebrew oder direkt)

# Backend
cd backend
./mvnw spring-boot:run

# Frontend (neues Terminal)
cd frontend
npm install
npm run dev
```

## Für neues Projekt anpassen

1. `com.template` → dein Package-Name umbenennen (IntelliJ: Refactor → Rename)
2. `appdb` in `.env.example` → dein DB-Name
3. `VITE_API_URL` in `.env.example` anpassen
4. `README.md` aktualisieren

## Projektstruktur

```
├── backend/
│   ├── src/main/java/com/template/
│   │   ├── config/         # Security, Exception Handler
│   │   ├── controller/     # REST Controller
│   │   ├── dto/            # Request/Response DTOs
│   │   ├── entity/         # JPA Entities
│   │   ├── repository/     # Spring Data Repositories
│   │   ├── security/       # JWT Filter & Service
│   │   └── service/        # Business Logic
│   └── src/main/resources/
│       ├── db/migration/   # Flyway SQL Migrationen
│       └── application.yml
├── frontend/
│   └── src/
│       ├── components/     # Wiederverwendbare UI-Komponenten
│       ├── pages/          # Seiten
│       ├── services/       # API-Aufrufe
│       ├── store/          # Zustand State Management
│       └── types/          # TypeScript Types
├── .github/workflows/      # CI/CD Pipeline
├── docker-compose.yml
└── .env.example
```

## API-Endpunkte

| Methode | Endpunkt | Beschreibung | Auth |
|---------|----------|--------------|------|
| POST | `/api/auth/register` | Registrierung | Nein |
| POST | `/api/auth/login` | Login | Nein |

## Neue Flyway-Migration hinzufügen

```sql
-- Datei: backend/src/main/resources/db/migration/V2__add_products.sql
CREATE TABLE products (
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    price NUMERIC(10,2) NOT NULL
);
```

Naming-Konvention: `V{nummer}__{beschreibung}.sql`
