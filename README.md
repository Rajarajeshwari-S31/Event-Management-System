# Smart Event Management System

A full-stack, enterprise-grade **Smart Event Management System** built with **Java 17/21**, **Spring Boot 3**, **Spring Data JPA / Hibernate**, **MySQL**, and a responsive **Bootstrap 5** frontend. The application provides end-to-end event discovery, seat booking with real-time capacity management, role-based dashboards, and complete administrative lifecycle control.

---

## 📌 Project Overview

The Smart Event Management System is designed to streamline event scheduling, attendee registrations, and capacity tracking. It eliminates overbooking through automated seat quota controls and enables attendees to discover events, register with one click, and manage cancellations. Organizers and Administrators gain complete operational oversight with real-time analytics, attendee rosters, and event authoring tools.

---

## 🚀 Technologies Used

* **Backend Framework:** Spring Boot 3.2.0 (Java 17 / 21)
* **REST APIs:** Spring MVC (`@RestController`, `@RequestMapping`, `@CrossOrigin`)
* **Data Persistence:** Spring Data JPA, Hibernate ORM
* **Database:** MySQL 8.0
* **Frontend:** Responsive HTML5, CSS3, JavaScript (Fetch API)
* **UI Framework:** Bootstrap 5.3 & Bootstrap Icons
* **Build & Dependency Management:** Apache Maven 3.9+ / Maven Wrapper (`mvnw`, `mvnw.cmd`)

---

## ✨ Main Features

* **Real-time Seat & Capacity Tracking:** Dynamic calculation of booked vs. available seats. Automated safeguards prevent overbooking when capacity reaches zero (`Sold Out`).
* **Instant Cancellation & Seat Restoration:** Attendees can cancel bookings, automatically restoring available seats back to the event pool via `@Transactional` safety.
* **Search & Multi-Filter Engine:** Query events dynamically by title keyword, category (Technology, Music, Workshop, Business, Gaming, Health), and venue/city.
* **Dual Role Dashboards:** Distinct tailored interfaces for standard Attendees and platform Administrators.
* **Pre-seeded Demonstration Data:** `CommandLineRunner` automatically seeds starter users, events, and registrations on initial startup for instant testing and evaluation.

---

## 👥 User and Admin Roles

### 1. Attendee (User)
* **Account Registration & Login:** Create an account with name, email, password, and role. Duplicate emails are strictly blocked.
* **Event Discovery:** Search and browse upcoming events with badges highlighting remaining seats.
* **Event Details:** View comprehensive information, venue, schedule, and live capacity progress bars.
* **Ticket Booking:** Register with one click; duplicate registrations for the same event are prevented.
* **My Registrations:** View personal ticket history with booking IDs, timestamps, and status (`CONFIRMED` / `CANCELLED`).
* **Self-Service Cancellation:** Cancel bookings anytime to release reserved seats.

### 2. Administrator (Admin)
* **Executive Metrics Dashboard:** Monitor live counters for Total Events, Total Registrations, and Registered Users.
* **Full Event CRUD:** Create new events, modify details and capacity allocations, or delete events.
* **Attendee Tracking:** View detailed attendee rosters (name, email, booking status) via dedicated modal views for any event.
* **Capacity Management:** Monitor quota allocations across all hosted events.

---

## 🔐 Authentication & Security

* **Implementation:** The application utilizes clean, lightweight **Role-Based Authentication** backed by REST endpoints (`/api/users/login` and `/api/users/register`) and browser session management (`localStorage`).
* **JWT Note:** In accordance with the design goal of keeping the codebase clear, reliable, and straightforward for freshers to explain during technical interviews, token-heavy JWT overhead was avoided in favor of direct session-state validation with CORS-enabled REST endpoints.
* **Data Protection:** Passwords are never returned in public API payloads.

---

## 🗄️ Database Details

The application utilizes **MySQL 8.0** with **Hibernate automatic schema generation** (`ddl-auto=update`):

* **`users` Table:** Stores attendee and admin credentials (`id`, `name`, `email` [UNIQUE], `password`, `role`).
* **`events` Table:** Stores event details (`id`, `title`, `description`, `category`, `date`, `location`, `capacity`, `available_seats`).
* **`registrations` Table:** Maps attendee bookings (`id`, `user_id`, `event_id`, `registration_date`, `status`).

---

## 📁 Project Structure

```
Event-Management-System/
├── pom.xml                                   # Maven dependencies & build plugins
├── mvnw / mvnw.cmd / .mvn/                   # Maven Wrapper scripts
├── run.bat                                   # 1-Click Windows execution script
├── README.md                                 # Project documentation
├── .gitignore                                # Excludes target/, IDE, logs, and secrets
└── src/
    ├── main/
    │   ├── java/com/ems/
    │   │   ├── EventManagementSystemApplication.java  # Application entrypoint
    │   │   ├── config/
    │   │   │   ├── CorsConfig.java            # Global CORS configuration
    │   │   │   └── DataInitializer.java       # Database seeder for demo data
    │   │   ├── controller/
    │   │   │   ├── UserController.java        # User authentication & profile APIs
    │   │   │   ├── EventController.java       # Event CRUD & search endpoints
    │   │   │   ├── RegistrationController.java# Booking & cancellation endpoints
    │   │   │   └── AdminController.java       # Attendee roster & stats endpoints
    │   │   ├── dto/
    │   │   │   ├── ApiResponse.java           # Standard API response wrapper
    │   │   │   ├── LoginRequest.java          # Login payload
    │   │   │   └── RegistrationRequest.java   # Booking payload
    │   │   ├── entity/
    │   │   │   ├── User.java                  # User JPA entity
    │   │   │   ├── Event.java                 # Event JPA entity
    │   │   │   └── Registration.java          # Registration JPA entity
    │   │   ├── repository/
    │   │   │   ├── UserRepository.java        # User data access
    │   │   │   ├── EventRepository.java       # Event queries & custom search
    │   │   │   └── RegistrationRepository.java# Booking lookups
    │   │   └── service/
    │   │       ├── UserService.java           # User business logic
    │   │       ├── EventService.java          # Event business logic
    │   │       └── RegistrationService.java   # Transactional seat management
    │   └── resources/
    │       ├── application.properties         # Database connection settings
    │       └── static/
    │           ├── css/style.css              # Custom styling
    │           ├── js/
    │           │   ├── auth.js                # Auth session & alert utilities
    │           │   └── navbar.js              # Role-aware dynamic navigation
    │           ├── index.html                 # Landing page & featured events
    │           ├── login.html                 # Sign-in with 1-click demo credentials
    │           ├── register.html              # Sign-up for users and admins
    │           ├── events.html                # Event explorer with multi-filtering
    │           ├── event-details.html         # Detailed event breakdown & booking
    │           ├── my-registrations.html      # Attendee ticket list & cancellation
    │           ├── admin-dashboard.html       # Admin metrics & event management
    │           └── add-event.html             # Event creation & editing form
    └── test/
        └── java/com/ems/
            └── EventManagementSystemApplicationTests.java # End-to-end integration tests
```

---

## 🏃 How to Run the Project

### Prerequisites
1. **Java Development Kit (JDK 17 or JDK 21)** installed and added to `PATH`.
2. **MySQL Server 8.0** running locally on port `3306`.
3. Create the database (or let Spring Boot create it automatically):
   ```sql
   CREATE DATABASE IF NOT EXISTS event_management_db;
   ```

### Option 1: 1-Click Batch Runner (Windows)
Double-click `run.bat` in the project root directory. It verifies MySQL and starts the server.

### Option 2: Using Maven Wrapper (All Platforms)
Open a terminal in the project root directory:

**Windows (PowerShell):**
```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

### Option 3: Running the Standalone JAR
```powershell
java -jar target/event-management-system-1.0.0.jar
```

### Accessing the Web Application
Open your browser and navigate to:
👉 **`http://localhost:8080`**

---

## 🔑 Pre-seeded Test Accounts

| Role | Email | Password | Pre-configured Privileges |
|---|---|---|---|
| **Administrator** | `admin@ems.com` | `admin123` | Full Admin Dashboard, Event CRUD, Attendee rosters |
| **Attendee (User)** | `john@example.com` | `user123` | Event browsing, booking, ticket cancellation |
| **Attendee 2** | `jane@example.com` | `user123` | Secondary attendee account for testing |

*(Quick-fill buttons are provided on the login page for immediate demonstration).*

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).
