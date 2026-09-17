# Architecture & Design Notes

## Roles
Parent manages children and requests. LSA manages profile, availability and assigned students. Admin monitors users, matching, sessions and audit logs.

## Matching flow
Parent creates a matching request -> admin/system evaluates specialization, location, experience and availability -> match record is created -> session is scheduled.

## Data integrity
Transactional data is normalized around users, parents, students, LSA profiles, availability, requests, matches and sessions. Foreign keys reject references to missing parent/student/LSA records. `students.predecessor_id` is a self-referencing lineage lock.

## Persistence strategy
PostgreSQL is the system of record for transactional relational data. For a future high-scale deployment, analytical reporting can be replicated to a warehouse and high-volume time-series/session telemetry can use a dedicated time-series store; the current MVP intentionally keeps operational data in PostgreSQL.
