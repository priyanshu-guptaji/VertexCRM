-- Multi-Tenant CRM Database Initialization Script

-- Create database and user
CREATE DATABASE vertexcrm;
CREATE USER crm_user WITH PASSWORD 'crm_password';
GRANT ALL PRIVILEGES ON DATABASE vertexcrm TO crm_user;

-- Connect to the database
\c vertexcrm;

-- Grant schema privileges
GRANT ALL ON SCHEMA public TO crm_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO crm_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO crm_user;

-- The tables will be created automatically by Hibernate
-- when the Spring Boot application starts with ddl-auto: update

-- Default roles will be inserted by the DataInitializationService
-- when the application starts for the first time
