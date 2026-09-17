# HabotConnect – Student–LSA Matching Platform

Full-stack interview project built with a React + Bootstrap frontend, a Spring Boot + JPA/Hibernate backend, and a PostgreSQL database. HabotConnect matches students with Learning Support Assistants (LSAs) based on availability and profile fit.

## Tech Stack
- **Frontend:** React, Bootstrap, Vite
- **Backend:** Spring Boot, Spring Security (JWT), JPA/Hibernate
- **Database:** PostgreSQL

## Features
- Role-based accounts: **Parent**, **LSA**, **Admin**
- JWT-based authentication and authorization
- CRUD operations for students, parents, LSA profiles, availability, and sessions
- Matching engine connecting students with suitable LSAs
- Audit logging for key actions

## Getting Started

### 1. Set up PostgreSQL
Create a database named `habotconnect`:

CREATE DATABASE habotconnect;

psql -U postgres -d habotconnect -f database/schema.sql
(Optional) Load sample data:

psql -U postgres -d habotconnect -f database/sample_data.sql
### 2. Run the Backend
Requirements: Java 17+, Maven 3.9+

Default credentials are `postgres` / `postgres` — override via environment variables if needed.
Run the schema:

cd backend
mvn spring-boot:run

Backend runs at: `http://localhost:8080`

Environment variables:
| Variable | Default |
|---|---|
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/habotconnect` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | `postgres` |

cd frontend
npm install
npm run dev

Frontend runs at: `http://localhost:5173`

## Demo Walkthrough
1. Go to the Register page and create an account as **PARENT**, **LSA**, or **ADMIN**.
2. Sign in — you'll land on a role-specific dashboard.
3. Use the CRUD forms to manage students, availability, matches, and sessions, all backed by the Spring Boot REST API.

## Architecture

### 3. Run the Frontend
Requirements: Node.js 18+

React (frontend) → REST API → Spring Boot Controller → JPA Repository → PostgreSQL

The schema includes a `predecessor_id` field on the `students` table with foreign-key constraints to enforce lineage/data integrity, as required by the project spec.

## Project Structure
HabotConnect/
├── backend/ # Spring Boot API
├── frontend/ # React app
├── database/ # SQL schema and sample data
└── docs/ # API and architecture docs


## Docs
- [API Documentation](docs/API.md)
- [Architecture Overview](docs/ARCHITECTURE.md)


  git add README.md
git commit -m "Update README"
git push

