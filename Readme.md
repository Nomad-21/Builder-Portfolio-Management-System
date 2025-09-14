# Builder Portfolio Management System
## Technical Documentation & Project Walkthrough

---

**Version:** 1.0-SNAPSHOT  
**Architecture:** MVC Pattern with Repository Layer  
**Date:** December 2024

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Database Design](#database-design)
3. [Core Functionality Walkthrough](#core-functionality-walkthrough)
4. [User Roles & Permissions](#user-roles--permissions)
5. [Key Features Implementation](#key-features-implementation)
6. [GitHub Repository Information](#github-repository-information)
7. [Installation & Setup Instructions](#installation--setup-instructions)
8. [Running the Application](#running-the-application)
9. [Conclusion](#conclusion)

---

## Project Overview

The **Builder Portfolio Management System** is a comprehensive Java-based console application designed to manage construction projects, tasks, payments, and document workflows. The system serves three distinct user types: Administrators, Builders, and Clients, each with specific roles and capabilities.

### Key Objectives
- Streamline construction project management
- Facilitate communication between builders and clients
- Track project progress and financial transactions
- Manage project documentation and task scheduling
- Provide visual project timelines through Gantt charts



### Project Structure
```
src/main/java/tech/zeta/
├── constants/          # Enums for status and roles
├── controller/         # Business logic controllers
├── model/             # Data models/entities
├── repository/        # Data access layer
├── service/           # Business services
├── ui/               # User interface components
└── util/             # Utility classes
```

### Design Patterns Used
1. **MVC Pattern:** Separation of concerns between UI, business logic, and data
2. **Repository Pattern:** Data access abstraction
3. **Service Layer Pattern:** Business logic encapsulation
4. **Factory Pattern:** Object creation management

---

## Database Design

### Entity Relationship Overview

The system uses a relational database with the following core entities:

#### 1. User Table
```sql
CREATE TABLE "User" (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('ADMIN','BUILDER','CLIENT')) NOT NULL
);
```

#### 2. Project Table
```sql
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
```

#### 3. Task Table
```sql
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
```

#### 4. Payment Table
```sql
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
```

#### 5. Document Table
```sql
CREATE TABLE Document (
    document_id SERIAL PRIMARY KEY,
    file_path VARCHAR(255) NOT NULL,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    project_id INT NOT NULL,
    CONSTRAINT fk_document_project FOREIGN KEY (project_id) REFERENCES Project(project_id)
);
```

#### 6. Client_Project Mapping Table
```sql
CREATE TABLE Client_Project (
    mapping_id SERIAL PRIMARY KEY,
    client_id INT NOT NULL,
    project_id INT NOT NULL,
    CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES "User"(user_id),
    CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES Project(project_id),
    UNIQUE (client_id, project_id)
);
```

---

## Core Functionality Walkthrough

### 1. Authentication System

#### Login Process
```java
public boolean login(String username, String password) {
    Param<String, String> param = new Param<>("name", username);
    List<User> userList = userRepository.getUserBy(param);
    if(userList.isEmpty()){
        return false;
    }
    User user = userList.getFirst();
    if (user != null && verifyPassword(password,user.getPassword())) {
        loggedInUser = user;
        return true;
    }
    return false;
}
```

**Key Features:**
- SHA-256 password hashing for security
- Role-based authentication
- Session management for logged-in users

#### Registration Process
- New users can register as BUILDERS or CLIENTS
- ADMIN accounts must be created by existing administrators
- Email uniqueness validation
- Password hashing before storage

### 2. Role-Based Dashboard System

The system implements a sophisticated role-based access control:

#### Admin Dashboard
- **User Management:** Create, view, and delete users
- **Project Management:** Create, view, and delete projects
- **Payment Verification:** Approve or reject payment requests
- **Project Association:** Link builders and clients to projects

#### Builder Dashboard
- **Project Management:** View and update assigned projects
- **Task Management:** Create, edit, and track project tasks
- **Document Upload:** Upload project-related documents
- **Payment Verification:** Verify client payments
- **Gantt Chart Visualization:** View project timelines

#### Client Dashboard
- **Project Viewing:** View assigned projects and progress
- **Payment Management:** Make payments and upload proof
- **Payment History:** Track payment status

### 3. Project Management Workflow

#### Project Creation Process
1. **Admin/Builder creates project** with basic details
2. **Task breakdown** - Define individual tasks with timelines
3. **Client association** - Link clients to the project
4. **Status tracking** - Monitor project progress

#### Task Management
- Each project contains multiple tasks
- Tasks have start/end dates and progress tracking (0-100%)
- Automatic project status updates based on task completion
- Gantt chart visualization for timeline management

### 4. Payment Management System

#### Payment Flow
1. **Client initiates payment** with amount and proof
2. **Payment status tracking** (PENDING → APPROVED/REJECTED)
3. **Builder verification** of payment proof
4. **Admin oversight** for payment approval

#### Financial Tracking
- Budget vs. actual spending comparison
- Payment history and status tracking
- Proof document management

### 5. Document Management

#### Document Upload Process
- **File path storage** in database
- **Project association** for organization
- **Timestamp tracking** for audit trails
- **Builder-specific uploads** for project documentation

---

## User Roles & Permissions

### Administrator Role
**Capabilities:**
- Full system access
- User account management
- Project creation and deletion
- Payment verification and approval
- System-wide project oversight

**UI Features:**
- User creation/deletion interface
- Project management dashboard
- Payment verification panel
- Project-user association tools

### Builder Role
**Capabilities:**
- Project management for assigned projects
- Task creation and progress tracking
- Document upload and management
- Payment verification
- Gantt chart visualization

**UI Features:**
- Project overview dashboard
- Task management interface
- Document upload system
- Payment verification panel
- Visual timeline charts

### Client Role
**Capabilities:**
- View assigned projects
- Track project progress
- Make payments
- Upload payment proof

**UI Features:**
- Project status dashboard
- Payment interface
- Payment history viewer

---

## Key Features Implementation

### 1. Gantt Chart Visualization

The system includes a sophisticated Gantt chart utility:

```java
public static void printGanttChart(List<Task> tasks) {
    // Dynamic scaling based on project duration
    String scale;
    if (totalDays <= 60) {
        scale = "DAY";
    } else if (totalDays <= 365) {
        scale = "WEEK";
    } else {
        scale = "MONTH";
    }
    
    // Visual representation with progress indicators
    // ██ : Completed, # : Pending
}
```

**Features:**
- Automatic timeline scaling (day/week/month)
- Progress visualization
- Task dependency representation
- Color-coded completion status

### 2. Password Security

```java
private static String hashPassword(String password) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        // Convert to hex string
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Error hashing password", e);
    }
}
```

### 3. Database Connection Management

The system uses a centralized database utility:
- Connection pooling
- SQL injection prevention
- Transaction management
- Error handling and logging

### 4. Model Filling Utility

```java
public static Project fillProject(Project currentProject, Project updatedProject) {
    // Intelligent field updating
    // Preserves existing data while updating changed fields
}
```

---

## GitHub Repository Information

### Repository Details
- **Repository Name:** Builder-Portfolio-Management-System
- **Primary Language:** Java
- **License:** [To be specified by repository owner]
- **Main Branch:** main
- **Latest Version:** 1.0-SNAPSHOT

### Repository Structure
```
Builder-Portfolio-Management-System/
├── src/
│   ├── main/
│   │   ├── java/tech/zeta/
│   │   └── resources/
│   └── test/
├── target/
├── pom.xml
└── README.md
```

### Key Files
- `pom.xml` - Maven configuration and dependencies
- `Tables.sql` - Database schema definition
- `application.properties` - Database configuration
- `Main.java` - Application entry point

### Dependencies
- PostgreSQL JDBC Driver (42.7.7)
- JUnit Jupiter (5.10.0)
- Mockito (5.12.0)
- JaCoCo Maven Plugin (0.8.12)

---

## Installation & Setup Instructions

### Prerequisites

#### 1. Java Development Environment
- **Java 21** or higher
- **Maven 3.6+** for dependency management
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

#### 2. Database Setup
- **PostgreSQL 12+** installed and running
- Database server accessible on localhost:5432
- Administrative access to create databases

#### 3. System Requirements
- **RAM:** Minimum 4GB, Recommended 8GB
- **Storage:** At least 1GB free space
- **OS:** Windows 10+, macOS 10.15+, or Linux

### Installation Steps

#### Step 1: Clone the Repository
```bash
git clone https://github.com/Nomad-21/Builder-Portfolio-Management-System
cd Builder-Portfolio-Management-System
```

#### Step 2: Database Configuration

1. **Start PostgreSQL service:**
   ```bash
   # Windows
   net start postgresql-x64-12
   
   # macOS (using Homebrew)
   brew services start postgresql
   
   # Linux
   sudo systemctl start postgresql
   ```

2. **Create database:**
   ```sql
   CREATE DATABASE builderportfoliomangement;
   ```

3. **Run schema script:**
   ```bash
   psql -U postgres -d builderportfoliomangement -f src/main/resources/Tables.sql
   ```

#### Step 3: Configure Application Properties

Edit `src/main/resources/application.properties`:
```properties
db_class_name = org.postgresql.Driver
db_database_url = jdbc:postgresql://localhost:5432
db_username = postgres
db_password = [your_postgres_password]
db_dataBaseName = builderportfoliomangement
```

#### Step 4: Maven Dependencies

```bash
mvn clean install
```

This will:
- Download all required dependencies
- Compile the source code
- Run unit tests
- Generate code coverage reports

---

## Running the Application

### Method 1: Command Line Execution

```bash
# Navigate to project directory
cd Builder-Portfolio-Management-System

# Compile and run
mvn compile exec:java -Dexec.mainClass="tech.zeta.Main"
```

### Method 2: IDE Execution

1. **Import project** into your IDE
2. **Configure database connection** in application.properties
3. **Run Main.java** as a Java application

### Method 3: JAR Execution

```bash
# Create executable JAR
mvn clean package

# Run the JAR
java -jar target/BuilderPortfolioManagementSystem-1.0-SNAPSHOT.jar
```

### Application Startup Process

1. **Database Connection** - Establishes connection to PostgreSQL
2. **Authentication UI** - Presents login/signup options
3. **Role-based Dashboard** - Redirects to appropriate user interface
4. **Session Management** - Maintains user session until logout

### Sample User Workflow

#### For First-Time Setup:
1. **Register as Builder/Client** or **Login as Admin**
2. **Create users** (if Admin)
3. **Create projects** and associate users
4. **Start project management** workflow

#### For Regular Usage:
1. **Login** with credentials
2. **Access role-specific dashboard**
3. **Perform authorized operations**
4. **Logout** when finished

---


## Conclusion

The Builder Portfolio Management System represents a comprehensive solution for construction project management, featuring:

- **Robust Architecture** with clear separation of concerns
- **Role-based Access Control** for secure multi-user operations
- **Comprehensive Project Management** with task tracking and Gantt charts
- **Financial Management** with payment tracking and verification
- **Document Management** for project-related files
- **High Code Quality** with extensive testing and coverage

---

