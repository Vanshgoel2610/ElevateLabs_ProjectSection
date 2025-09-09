RESTful Bookstore API
This project is a RESTful backend API designed for managing books and authors. The API supports standard CRUD operations and includes advanced features like filtering, pagination, and sorting to efficiently handle a large volume of data. It is a robust and scalable service built on a modern Java stack, suitable for a wide range of web and mobile applications.

Technologies Used
Java 17: The core programming language.

Spring Boot 3.x: A framework for creating stand-alone, production-grade Spring-based applications.

Spring Data JPA: Simplifies data access and persistence operations with an ORM approach.

H2 Database: An in-memory relational database used for development and testing.

Maven: A build automation and project management tool.

Postman: Used for API testing and development.

SpringDoc OpenAPI: Automatically generates API documentation with a Swagger UI.


Of course. Here is a comprehensive README file for your Job Portal project, summarizing its features, setup instructions, and API endpoints.

You can copy and paste this content into a new file named README.md in the root directory of your project.

Job Portal API 🚀
A robust RESTful API for a job portal application built with Java and Spring Boot. This backend service provides functionalities for user authentication, role-based authorization, job management, and a complete application workflow, including a secure candidate referral system.

✨ Features
Authentication: Secure user registration and login using JSON Web Tokens (JWT).

Role-Based Authorization: Distinct roles for Applicant and Employer with specific permissions for each.

Job Management: Full CRUD (Create, Read, Update, Delete) operations for job postings, restricted to employers.

Application System: Endpoints for applicants to apply for jobs and view their application history and status.

Employer Dashboard: Endpoints for employers to view and manage applications for their specific job postings.

Secure Referral System: Allows employers to refer a candidate for a job by generating a secure, one-time-use link sent via email.

🛠️ Technologies Used
Backend: Java 21, Spring Boot 3

Security: Spring Security 6

Database: Spring Data JPA (Hibernate), MySQL

Authentication: JSON Web Tokens (JWT)

API Testing: Postman
