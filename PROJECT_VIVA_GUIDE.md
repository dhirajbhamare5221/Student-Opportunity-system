# 🎓 Student Opportunity Discovery Platform
## College Project / Viva Defense Guide

Welcome to the definitive guide for your 3rd-year Computer Engineering project! This document is designed to help you explain the architecture, tech stack, and key design decisions to your professors during your project presentation or viva.

---

## 1. Project Overview
**What is it?**
A centralized, intelligent platform designed to help computer engineering students discover internships, hackathons, and certifications. 

**Why did we build it?**
Students often miss out on career-defining opportunities because information is scattered across different websites. This platform aggregates those opportunities and uses a **Recommendation Engine** to match students with roles that fit their specific skills.

---

## 2. Technology Stack (The "MERN/MEAN" Alternative)
Instead of a standard Node.js project, we chose an Enterprise-grade stack to demonstrate industry readiness:

### Backend
- **Java 17**: The core programming language.
- **Spring Boot 3**: The framework used to rapidly build the REST APIs. It provides an embedded Tomcat server.
- **Spring Data JPA & Hibernate**: Used for ORM (Object-Relational Mapping). Instead of writing raw SQL queries, we write Java code, and Hibernate translates it into SQL securely (preventing SQL injection).
- **H2 Database**: An in-memory/file-based database used for rapid local development. (Easily swappable to MySQL for production).

### Frontend
- **HTML5 & CSS3**: Custom-built styling using modern paradigms like CSS Grid, Flexbox, and Glassmorphism (blur effects). No heavy libraries like Bootstrap were used, demonstrating raw frontend competency.
- **Vanilla JavaScript**: Used for DOM manipulation, handling state, and making asynchronous `fetch()` API calls to the Spring Boot backend.

---

## 3. Core Architecture (MVC Pattern)

If a professor asks "What architecture did you use?", you can confidently say:
> *"I used a layered Model-View-Controller (MVC) architecture separated into REST APIs and a standalone frontend."*

Here is how a request flows through the backend:
1. **Controller Layer** (`OpportunityController.java`): Receives the HTTP request from the browser (e.g., `GET /api/opportunities`). It validates inputs and handles routing.
2. **Service Layer** (`OpportunityService.java`): Contains the core "Business Logic". This is where data is processed, algorithms (like the Recommendation Engine) are run, and errors are handled.
3. **Repository Layer** (`OpportunityRepository.java`): Interfaces with the database. We used Spring Data JPA, which auto-generates SQL queries based on method names (e.g., `findByTitleContainingIgnoreCase`).
4. **Model Layer** (`Opportunity.java`): The actual Java Classes (Entities) that represent tables in the database.

---

## 4. Key Features & How to Explain Them

### A. The Recommendation Engine
**How it works:**
The engine uses a deterministic scoring algorithm. When a student requests their "For You" feed:
1. The backend fetches the student's `Skills` (e.g., Java, Spring Boot) and `Preferred Categories` (e.g., Internships).
2. It loops through all active opportunities.
3. It awards **+5 points** if the category matches.
4. It awards **+3 points** for *each* required skill that matches the student's skills.
5. It sorts the list from highest score to lowest score and sends it to the frontend.

### B. The Bookmarking System
**How it works:**
We implemented a **Many-To-Many** relationship concept.
- A `Bookmark` entity acts as a junction table between a `Student` and an `Opportunity`.
- *Key Defense Point*: We added a `@UniqueConstraint(columnNames = {"student_id", "opportunity_id"})` on the database table. This ensures that even if there is a network glitch, a student can never have duplicate bookmarks of the same opportunity. Database-level constraints guarantee data integrity.

### C. DTO Pattern (Data Transfer Objects)
**How it works:**
If a professor asks why we didn't just return the Database Entities directly to the browser, explain the DTO Pattern:
- Returning raw Entities can expose sensitive database details (like passwords or internal IDs) and cause infinite recursion errors in JSON (due to Bidirectional relationships).
- Instead, we map our Entities into `OpportunityDTO.java` objects before sending them to the frontend. This provides a secure, clean, and exact payload.

### D. Security & Authentication
**How it works:**
- We implemented **BCrypt Password Hashing**.
- When a user registers, their plaintext password (e.g., `password123`) is passed through a one-way mathematical hashing algorithm before being saved to the database.
- *Key Defense Point*: Even if a hacker dumps the database, they cannot see the users' actual passwords.

---

## 5. Potential Future Enhancements (For the Conclusion)
If asked "What would you add if you had more time?", you can suggest:
1. **JWT (JSON Web Tokens)**: For robust, stateless authentication across different servers.
2. **Email Notifications**: Integrating Spring Mail to email students when a new opportunity matching their skills is posted.
3. **Web Scraping / API Integrations**: Automatically pulling hackathons from platforms like Devpost or internships from LinkedIn.

---
**Good luck with your presentation! You have built a highly structured, scalable, and professional platform!** 🚀
