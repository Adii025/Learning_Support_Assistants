
# HabotConnect Student–LSA Matching Platform

Full-stack interview project: React + Bootstrap frontend, Spring Boot + JPA/Hibernate backend, PostgreSQL database.

## Run PostgreSQL
Create database `habotconnect`. Default credentials are postgres/postgres; change environment variables if needed.

Run `database/schema.sql` in psql/pgAdmin.

## Backend
Requirements: Java 17+, Maven 3.9+.


cd backend
mvn spring-boot:run

Backend: http://localhost:8081

Environment variables:
- DATABASE_URL (default jdbc:postgresql://localhost:5432/habotconnect)
- DB_USERNAME (default postgres)
- DB_PASSWORD (default postgres)

## Frontend
Requirements: Node.js 18+.


cd frontend
npm install
npm run dev

Frontend: http://localhost:5173

## Demo
Open Register, create an account as PARENT, LSA or ADMIN, then sign in. The dashboard and role-specific navigation are available. CRUD forms connect to Spring Boot APIs.

## Architecture
React -> REST API -> Spring Boot Controller -> JPA Repository -> PostgreSQL.

The schema includes predecessor_id on students and foreign-key constraints to enforce lineage/integrity as required by the hiring project.

