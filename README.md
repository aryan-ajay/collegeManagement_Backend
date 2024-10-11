# Student, Faculty, and Administrator Management System - Backend

This is the backend for the management system, built using Java and Spring Boot. The backend provides a secure RESTful API for user authentication, profile management, and system operations. PostgreSQL is used as the database for persistent storage of user data and system records.

## Key Features

- **User Authentication & Authorization**: Secure JWT-based authentication for login and role-based authorization (Student, Faculty, Administrator).
- **Student Management**: CRUD operations for student profiles, enabling the administrator to add, update, or delete records.
- **Faculty Management**: Faculty members can manage their class lists and update their profiles, ensuring real-time changes are reflected.
- **Admin Tools**: Administrators can perform operations on student and faculty data and monitor system performance with visual analytics.
- **Data Visualization**: Provides administrators with insights into system data using graphical charts, including trends in student enrollment and faculty workloads.

## Tech Stack

- **Backend**: Java, Spring Boot
- **Database**: PostgreSQL
- **Security**: JWT for authentication, role-based access control for secure data handling
- **Data Persistence**: Spring Data JPA (for managing database transactions)
- **API Documentation**: RESTful API endpoints for CRUD operations and role-specific tasks

## API Endpoints

### Authentication
- **POST** `/login` - Handles user login and generates JWT tokens.
- **POST** `/register` - Handles new user registration (Admin only).

### Student Operations
- **GET** `/students/{id}` - Fetch a specific student’s profile.
- **POST** `/students` - Add a new student record (Admin only).
- **PUT** `/students/{id}` - Update an existing student’s record.
- **DELETE** `/students/{id}` - Delete a student’s record (Admin only).

### Faculty Operations
- **GET** `/faculty/{id}` - Retrieve faculty details.
- **PUT** `/faculty/{id}` - Update faculty profile (Faculty only).
- **GET** `/faculty/students` - Get the list of students in the faculty member’s courses.

### Admin Dashboard
- **GET** `/admin/dashboard` - Get data for visual analytics (e.g., student trends, course loads).

## Installation Instructions

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/student-faculty-admin-backend.git
    ```

2. **Navigate to the project directory**:
    ```bash
    cd student-faculty-admin-backend
    ```

3. **Set up PostgreSQL**:
    - Create a PostgreSQL database named `studentmanagement`, and update `application.properties` with your database credentials:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/studentmanagement
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update
    ```

## Project Highlights

- **Secure Access**: JWT-based token authentication ensures that only authorized users can access sensitive areas of the system.
- **RESTful Architecture**: Clean, scalable APIs for interacting with the system data.
- **Data Integrity**: Real-time updates to student and faculty records, with immediate reflection on the frontend.
- **Analytics**: The administrator dashboard provides a visual representation of key metrics and trends in the system.
