-- Multi-Tenant CRM Database Initialization Script

-- Create database and user
<<<<<<< HEAD
CREATE DATABASE vertexcrm;
CREATE USER crm_user WITH PASSWORD 'crm_password';
GRANT ALL PRIVILEGES ON DATABASE vertexcrm TO crm_user;

-- Connect to the database
\c vertexcrm;
=======
CREATE DATABASE crm_db;
CREATE USER crm_user WITH PASSWORD 'crm_password';
GRANT ALL PRIVILEGES ON DATABASE crm_db TO crm_user;

-- Connect to the database
\c crm_db;
>>>>>>> c3722ea63fb4401b3489db78259aed343a450c80

-- Grant schema privileges
GRANT ALL ON SCHEMA public TO crm_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO crm_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO crm_user;

-- The tables will be created automatically by Hibernate
-- when the Spring Boot application starts with ddl-auto: update

-- Default roles will be inserted by the DataInitializationService
-- when the application starts for the first time
