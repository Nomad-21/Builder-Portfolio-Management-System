-- Drop existing tables
DROP TABLE IF EXISTS Task CASCADE;
DROP TABLE IF EXISTS Payment CASCADE;
DROP TABLE IF EXISTS Document CASCADE;
DROP TABLE IF EXISTS Client_Project CASCADE;
DROP TABLE IF EXISTS Project CASCADE;
DROP TABLE IF EXISTS "User" CASCADE;


-- User Table
CREATE TABLE "User" (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('ADMIN','BUILDER','CLIENT')) NOT NULL
);


-- Project Table
CREATE TABLE Project (
    project_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    status VARCHAR(20) CHECK (status IN ('UPCOMING','IN_PROGRESS','COMPLETED')) NOT NULL,
    budget NUMERIC(12,2),
    actual_spend NUMERIC(12,2),
    start_date DATE,
    end_date DATE,
    builder_id INT NOT NULL,
    CONSTRAINT fk_builder FOREIGN KEY (builder_id) REFERENCES "User"(user_id)
);


-- Client_Project
CREATE TABLE Client_Project (
    mapping_id SERIAL PRIMARY KEY,
    client_id INT NOT NULL,
    project_id INT NOT NULL,
    CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES "User"(user_id),
    CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES Project(project_id),
    UNIQUE (client_id, project_id) -- prevent duplicates
);


-- Document Table
CREATE TABLE Document (
    document_id SERIAL PRIMARY KEY,
    file_path VARCHAR(255) NOT NULL,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    project_id INT NOT NULL,
    CONSTRAINT fk_document_project FOREIGN KEY (project_id) REFERENCES Project(project_id)
);


-- Payment Table
CREATE TABLE Payment (
    payment_id SERIAL PRIMARY KEY,
    amount NUMERIC(12,2) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) CHECK (status IN ('PENDING','APPROVED','REJECTED')) NOT NULL,
    proof_path VARCHAR(255),
    project_id INT NOT NULL,
    client_id INT NOT NULL,
    CONSTRAINT fk_payment_project FOREIGN KEY (project_id) REFERENCES Project(project_id),
    CONSTRAINT fk_payment_client FOREIGN KEY (client_id) REFERENCES "User"(user_id)
);



-- Task Table
CREATE TABLE Task (
    task_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    progress INT CHECK (progress BETWEEN 0 AND 100),
    project_id INT NOT NULL,
    CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES Project(project_id)
);


