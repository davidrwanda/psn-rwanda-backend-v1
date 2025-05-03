# PSN Rwanda API

API backend for PSN RWANDA Ltd - Notary and Professional Services website.

## Project Overview

This is a Spring Boot API backend for PSN RWANDA Ltd, a company providing legal activities (notary services), consultancy, leasing, real estate, and R&D services. The API supports both an admin panel and a user-facing booking system.

## Features

- User authentication with JWT
- Service management (CRUD operations)
- Booking system with phone number verification
- Admin dashboard with booking management
- Reports generation (CSV, PDF)
- SMS notifications (optional integration)
- Company information management

## Tech Stack

- Java 17
- Spring Boot 3.1.5
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL database
- Flyway for database migrations
- Lombok, MapStruct for boilerplate reduction
- SpringDoc OpenAPI for API documentation

## Architecture and Design Patterns

This project follows clean architecture principles and implements several design patterns:

1. **Factory Pattern**: Used in `ErrorResponseFactory` for standardized error handling.
2. **Template Method Pattern**: Implemented in `AbstractCrudService` for reusable CRUD operations.
3. **Builder Pattern**: Used with Lombok for object construction.
4. **Repository Pattern**: For data access abstraction.
5. **DTO Pattern**: For data transfer between layers.
6. **Dependency Injection**: For loose coupling between components.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

### Database Setup

1. Create a PostgreSQL database named `psn_rwanda`:

```sql
CREATE DATABASE psn_rwanda;
```

2. Update database credentials in `application.yml` if needed.

### Running the Application

1. Clone the repository:

```bash
git clone <repository-url>
cd PSN-Rwanda
```

2. Build the application:

```bash
mvn clean install
```

3. Run the application:

```bash
mvn spring-boot:run
```

The API will be available at http://localhost:8080/api

### API Documentation

Once the application is running, you can access the API documentation at:

- Swagger UI: http://localhost:8080/api/swagger-ui
- OpenAPI Spec: http://localhost:8080/api/docs

## API Endpoints

### Authentication

- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `POST /api/auth/refresh` - Refresh token

### Services

- `GET /api/services` - Get all services
- `GET /api/services/{id}` - Get service by ID
- `POST /api/services` - Create new service (Admin)
- `PUT /api/services/{id}` - Update service (Admin)
- `DELETE /api/services/{id}` - Delete service (Admin)
- `PATCH /api/services/{id}/toggle` - Toggle service visibility (Admin)

### Bookings

- `GET /api/bookings` - Get all bookings (Admin)
- `GET /api/bookings/{id}` - Get booking by ID (Admin)
- `POST /api/bookings` - Create booking (Public)
- `PATCH /api/bookings/{id}/status` - Update booking status (Admin)
- `DELETE /api/bookings/{id}` - Delete booking (Admin)

### Reports

- `GET /api/reports/bookings/export` - Export bookings (CSV/PDF)
- `GET /api/reports/dashboard` - Get dashboard statistics

### Company Information

- `GET /api/company` - Get company information
- `PUT /api/company` - Update company information (Admin)

## Security

The API is secured using JWT (JSON Web Tokens) for authentication. Protected endpoints require a valid token in the Authorization header.

```
Authorization: Bearer {token}
```

## Configuration

Application settings can be configured in `src/main/resources/application.yml`.

## Error Handling

The API uses standardized error responses:

```json
{
  "timestamp": "2023-10-30 12:34:56",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input",
  "path": "/api/bookings"
}
```

Validation errors include field-level details:

```json
{
  "timestamp": "2023-10-30 12:34:56",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/bookings",
  "fieldErrors": [
    {
      "field": "phoneNumber",
      "message": "Phone number is required",
      "rejectedValue": null
    }
  ]
}
```

## Testing

Run tests using Maven:

```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is proprietary and confidential. Unauthorized copying, transfer or use is strictly prohibited.

## Contact

PSN RWANDA Ltd - casybizy@gmail.com

---

© 2023 PSN RWANDA Ltd. All rights reserved. 