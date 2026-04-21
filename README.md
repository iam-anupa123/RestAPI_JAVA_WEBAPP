## 1. Project Overview
The Smart Campus API is a RESTful web service designed to manage campus infrastructure elements, specifically Rooms, Sensors, and environmental Readings. The system emphasizes professional software architecture, utilizing JAX-RS for the web layer and a thread-safe, in-memory DAO pattern for data management.

## 2. Key Technical Features
* **Architectural Mastery (HATEOAS):** The API implements a Discovery Endpoint at the root URL (`/api/v1/`), providing a resource map to allow clients to navigate the API dynamically without hardcoded URLs.
* **Separation of Concerns:** The project follows a strict layered architecture:
    * **Resource Layer:** Handles HTTP requests and response mapping using Jersey.
    * **DAO Layer:** Manages data persistence using a Generic DAO pattern.
    * **Model Layer:** Defines POJOs for data structures.
* **Thread-Safe In-Memory Storage:** Data is stored using `ConcurrentHashMap` to ensure integrity during concurrent API calls.
* **Global Error Handling:** A centralized `GlobalExceptionMapper` intercepts runtime errors, returning structured JSON error messages and appropriate HTTP status codes (404, 409, 422).

## 3. Technology Stack
* **Language:** Java 11
* **Framework:** JAX-RS (Jersey 2.32)
* **Build Tool:** Maven
* **Server:** Apache Tomcat 9.0
* **JSON Provider:** Jackson

## 4. API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/` | Discovery Map (HATEOAS Root) |
| `POST` | `/api/v1/rooms` | Create a new Room resource |
| `GET` | `/api/v1/rooms` | Retrieve all Rooms |
| `DELETE` | `/api/v1/rooms/{id}` | Remove Room (Safety Logic enabled) |
| `POST` | `/api/v1/sensors` | Add Sensor (Validated against Room ID) |
| `POST` | `/api/v1/sensors/{id}/readings` | Add sensor reading (Updates parent sensor) |

## 5. Setup and Running Instructions
1. **Import:** Open the project in NetBeans or a Maven-compatible IDE.
2. **Build:** Execute `Clean and Build` to download dependencies from the `pom.xml`.
3. **Deploy:** Run the project on an Apache Tomcat 9.0 instance.
4. **Base URL:** The API is accessible at `http://localhost:8080/SmartCampusAPI/api/v1/`.

## 6. Demonstration of Constraints (Safety Logic)
To verify the system's robust validation, test the following scenarios in Postman:
* **Relational Integrity:** Attempting to `DELETE` a Room that contains Sensors will return a **409 Conflict**.
* **Input Validation:** Attempting to `POST` a Sensor to a non-existent Room ID will return a **422 Unprocessable Entity**.
* **Discovery:** A `GET` request to the root will return the HATEOAS collection map.
