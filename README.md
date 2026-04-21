
# Smart Campus: Sensor & Room Management API

#### **Author:** Anupa Vitharana
#### **UOW ID/IIT ID:** w2151915/20241759

# Technical Overview & Setup Guide
---

## Overview

The Smart Campus API is a RESTful web service designed to manage campus infrastructure, focusing on rooms and environmental sensors. It enables efficient monitoring, data collection, and system scalability for smart campus environments.

---

## API Design

### Architecture

* Built using Java 11 and JAX-RS (Jersey)
* Follows a layered architecture:

  * Controllers (Resources): Handle HTTP requests
  * DAOs: Manage data storage and retrieval

### Data Persistence

* Uses an in-memory ConcurrentHashMap
* Ensures thread safety and supports concurrent requests without race conditions

### HATEOAS (Hypermedia as the Engine of Application State) (Discoverability)

* Includes a root discovery endpoint
* Clients can dynamically navigate API endpoints using hypermedia links

### Sub-Resource Routing

* Implements Sub-Resource Locator pattern
* Supports nested endpoints like:

  ```
  /sensors/{id}/readings
  ```
* Maintains Single Responsibility Principle

### Error Handling

* Uses Exception Mappers and Filters
* Prevents stack trace exposure
* Returns proper HTTP status codes:

  * 404 Not Found
  * 409 Conflict
  * 415 Unsupported Media Type
  * 422 Unprocessable Entity
  * 500 Internal Server Error

---

## Setup and Installation Instructions (NetBeans Environment)

This project is configured for seamless deployment using the NetBeans IDE.

### Prerequisites
* **Java:** JDK 11 or higher
* **IDE:** Apache NetBeans
* **Server:** A configured web server in NetBeans (e.g., Apache Tomcat)

### Step-by-Step Build and Launch
1. **Open the Project:** Launch NetBeans, go to `File > Open Project`, and select the extracted project folder.
2. **Clean and Build:** Right-click the project in the *Projects* window and select **Clean and Build**. This is a crucial step to ensure Maven wipes any old cached files, downloads all required dependencies, and compiles a completely fresh `.war` file.
3. **Deploy the Server:** Right-click the project again and select **Run**. NetBeans will automatically package the `.war` file and deploy it to your configured server.
4. **Access the API:** Once the Output window shows the server has started, the API entry point is accessible at:
   `http://localhost:8080/SmartCampusAPI/api/v1/`

---

## API Usage Examples (cURL)

### 1. View API Discovery Endpoint

```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/
```

### 2. Create a New Room

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
     -H "Content-Type: application/json" \
     -d '{"id": "R1", "name": "Main Lab", "capacity": 30}'
```

### 3. Register a New Sensor

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
     -H "Content-Type: application/json" \
     -d '{"id": "S1", "type": "Temperature", "roomId": "R1"}'
```

### 4. Add a Sensor Reading

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/S1/readings \
     -H "Content-Type: application/json" \
     -d '{"value": 24.5}'
```

### 5. Filter Sensors by Type

```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=Temperature"
```

---

## Project Highlights

* RESTful API design best practices
* Clean separation of concerns
* Scalable and maintainable structure
* Developer-friendly testing with cURL

---




# Conceptual Report:”Smart Campus” Sensor & Room Management API
## Part 1: Service Architecture & Setup

### Task 1.1: Project & Application Configuration
**Question:** Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

**Answer:** By default, JAX-RS resource classes are request-scoped, meaning a new instance is instantiated for every incoming HTTP request and disposed of once the response is sent. Because the resource class itself is short-lived, all in-memory data structures must be maintained in external, long-lived objects like DAOs. This architectural decision necessitates the use of thread-safe collections, such as ConcurrentHashMap and CopyOnWriteArrayList, to manage data. Since multiple request threads access these shared data structures simultaneously, thread-safety is critical to prevent race conditions and ensure data consistency across concurrent operations.

### Task 1.2: The ”Discovery” Endpoint
**Question:** Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

**Answer:** Hypermedia is a hallmark of advanced RESTful design because it enables HATEOAS, allowing the API to become self-discoverable. By providing links within responses, the server guides the client on available actions, reducing the need for the client to have hardcoded knowledge of the URI structure. This benefits developers by lowering coupling; if the server's internal URI structure changes, a HATEOAS-compliant client can still navigate the API successfully by following the provided links, unlike static documentation which may become outdated and lead to broken integrations.

## Part 2: Room Management

### Task 2.1: Room Resource Implementation
**Question:** When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

**Answer:** Returning only IDs significantly reduces the network bandwidth consumed per request, which is beneficial for performance on limited networks. However, it increases client-side complexity and latency, as the client must perform a separate "GET" request for every ID to retrieve meaningful data (the n+1 problem). Conversely, returning full objects increases initial bandwidth usage but allows the client to process and display all data in a single round-trip, improving the overall user experience at the cost of larger payload sizes.

### Task 2.2: Room Deletion & Safety Logic
**Question:** Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

**Answer:** Yes, the DELETE operation is idempotent. In this implementation, the first successful DELETE request removes the room and returns a 204 No Content status. If the client sends the exact same request again, the server checks for the resource, finds it no longer exists, and returns a 404 Not Found. Although the HTTP status codes differ between the first and subsequent calls, the "side effect" on the server state is identical: the resource remains deleted. This satisfies the definition of idempotency, as multiple identical requests result in the same server state as a single request.

## Part 3: Sensor Operations & Linking

### Task 3.1: Sensor Resource & Integrity
**Question:** We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

**Answer:** If a client attempts to send data in a format other than JSON, JAX-RS will detect the mismatch between the "Content-Type" header of the request and the @Consumes annotation. The runtime will automatically block the request before it reaches the resource method and return an HTTP 415 Unsupported Media Type response. This ensures that the application logic only receives data it is specifically designed to parse, preventing internal processing errors or malformed data issues.

### Task 3.2: Filtered Retrieval & Search
**Question:** You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?

**Answer:** Using query parameters is superior for filtering because path parameters are intended to identify specific resources, whereas query parameters are designed to modify the presentation of a collection. A path-based approach like /sensors/type/CO2 creates a tough hierarchy that is difficult to extend. In contrast, query parameters allow for optional and combinable filters (e.g., ?type=CO2&status=active) without requiring the creation of complex, nested URL structures for every possible filter combination.

## Part 4: Deep Nesting with Sub - Resources

### Task 4.1: The Sub-Resource Locator Pattern
**Question:** Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?

**Answer:** The Sub-Resource Locator pattern promotes the "Single Responsibility Principle" by allowing the API to delegate logic to dedicated classes for specific sub-paths. This prevents "God Classes" where one controller manages dozens of unrelated endpoints. By delegating to a SensorReadingResource class, the code becomes more modular, readable, and maintainable. It allows developers to isolate the logic for readings without cluttering the main SensorResource, making it easier to manage large, complex API structures.

## Part 5: Advanced Error Handling, Exception Mapping & Logging

### Task 5.2: Dependency Validation (422 Unprocessable Entity)
**Question:** Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

**Answer:** HTTP 404 typically indicates that the requested URI itself does not exist. However, when the URI is correct and the JSON payload is parsable, but it contains logical errors such as a reference to a non-existent Room ID, HTTP returns 422 Unprocessable Entity, which is more accurate. It signals that the server understood the request but cannot process the instructions due to semantic errors in the provided data, rather than a simple failure to find an endpoint.

### Task 5.4: The Global Safety Net (500)
**Question:** From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

**Answer:** Exposing internal stack traces provides an attacker with a roadmap of the server's architecture. They can identify specific versions of libraries (e.g., Jersey, Jackson), class names, method names, and even the server's internal file paths. This information can be used to search for known CVEs (Common Vulnerabilities and Exposures) specific to those library versions or to understand the application's logic to find "injection points" for more targeted and damaging attacks.

### Task 5.5: API Request & Response Logging Filters
**Question:** Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

**Answer:** Using filters allows for centralized management of "cross-cutting concerns." If logging is manually inserted into every method, the code becomes repetitive (violating the DRY principle) and is prone to human error, such as forgetting to log a specific endpoint. A centralized filter ensures 100% coverage for all incoming requests and outgoing responses automatically. Furthermore, if the logging format needs to be changed, it only needs to be updated in one class rather than across the entire codebase.
