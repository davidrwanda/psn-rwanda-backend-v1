# PSN Rwanda API Documentation

This document provides detailed information about the PSN Rwanda API endpoints, request/response formats, and examples.

## Base URL

Development: `http://localhost:8080`  
Production: `https://api.psnrwanda.com` (example)

## Authentication

All admin endpoints require authentication via a JWT token.

### Login

```
POST /api/auth/login
```

Request:
```json
{
  "username": "admin@psnrwanda.com",
  "password": "your_password"
}
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

### Refresh Token

```
POST /api/auth/refresh
```

Request:
- Header: `Authorization: Bearer [refresh_token]`

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

### Logout

```
POST /api/auth/logout
```

Request:
- Header: `Authorization: Bearer [access_token]`

Response:
```json
{
  "message": "Successfully logged out"
}
```

## Services Management

### Get All Services

```
GET /api/services
```

Response:
```json
{
  "data": [
    {
      "id": 1,
      "title": "Notary Service",
      "description": "Professional notary services for document authentication",
      "isActive": true,
      "imageUrl": "notary.jpg",
      "createdAt": "2023-06-15T10:00:00Z",
      "updatedAt": "2023-06-15T10:00:00Z"
    },
    {
      "id": 2,
      "title": "Legal Consultancy",
      "description": "Expert legal consultancy for personal and business needs",
      "isActive": true,
      "imageUrl": "legal.jpg",
      "createdAt": "2023-06-15T10:00:00Z",
      "updatedAt": "2023-06-15T10:00:00Z"
    }
  ],
  "totalItems": 2,
  "currentPage": 1,
  "totalPages": 1
}
```

### Get Service by ID

```
GET /api/services/{id}
```

Response:
```json
{
  "id": 1,
  "title": "Notary Service",
  "description": "Professional notary services for document authentication",
  "isActive": true,
  "imageUrl": "notary.jpg",
  "createdAt": "2023-06-15T10:00:00Z",
  "updatedAt": "2023-06-15T10:00:00Z"
}
```

### Create Service

```
POST /api/services
```

Request:
- Header: `Authorization: Bearer [access_token]`
- Content-Type: `application/json`

```json
{
  "title": "Notary Service",
  "description": "Professional notary services for document authentication",
  "isActive": true,
  "imageUrl": "notary.jpg"
}
```

Response:
```json
{
  "id": 1,
  "title": "Notary Service",
  "description": "Professional notary services for document authentication",
  "isActive": true,
  "imageUrl": "notary.jpg",
  "createdAt": "2023-06-15T10:00:00Z",
  "updatedAt": "2023-06-15T10:00:00Z"
}
```

### Update Service

```
PUT /api/services/{id}
```

Request:
- Header: `Authorization: Bearer [access_token]`
- Content-Type: `application/json`

```json
{
  "title": "Updated Notary Service",
  "description": "Professional notary services for document authentication and verification",
  "isActive": true,
  "imageUrl": "notary-updated.jpg"
}
```

Response:
```json
{
  "id": 1,
  "title": "Updated Notary Service",
  "description": "Professional notary services for document authentication and verification",
  "isActive": true,
  "imageUrl": "notary-updated.jpg",
  "createdAt": "2023-06-15T10:00:00Z",
  "updatedAt": "2023-06-15T11:00:00Z"
}
```

### Delete Service

```
DELETE /api/services/{id}
```

Request:
- Header: `Authorization: Bearer [access_token]`

Response:
```json
{
  "message": "Service successfully deleted"
}
```

### Toggle Service Visibility

```
PATCH /api/services/{id}/toggle
```

Request:
- Header: `Authorization: Bearer [access_token]`
- Content-Type: `application/json`

```json
{
  "isActive": false
}
```

Response:
```json
{
  "id": 1,
  "title": "Notary Service",
  "description": "Professional notary services for document authentication",
  "isActive": false,
  "imageUrl": "notary.jpg",
  "createdAt": "2023-06-15T10:00:00Z",
  "updatedAt": "2023-06-15T12:00:00Z"
}
```

## Booking Management

### Get All Bookings (Admin)

```
GET /api/bookings
```

Request:
- Header: `Authorization: Bearer [access_token]`

Query Parameters:
- `page` (optional): Page number (default: 1)
- `size` (optional): Items per page (default: 10)
- `status` (optional): Filter by status (PENDING, CONTACTED, COMPLETED, CANCELLED)

Response:
```json
{
  "data": [
    {
      "id": 1,
      "phoneNumber": "+250788123456",
      "serviceId": 1,
      "serviceName": "Notary Service",
      "status": "PENDING",
      "createdAt": "2023-06-15T10:00:00Z",
      "updatedAt": "2023-06-15T10:00:00Z"
    },
    {
      "id": 2,
      "phoneNumber": "+250788789012",
      "serviceId": 2,
      "serviceName": "Legal Consultancy",
      "status": "CONTACTED",
      "createdAt": "2023-06-15T11:00:00Z",
      "updatedAt": "2023-06-15T12:00:00Z"
    }
  ],
  "totalItems": 2,
  "currentPage": 1,
  "totalPages": 1
}
```

### Get Booking by ID (Admin)

```
GET /api/bookings/{id}
```

Request:
- Header: `Authorization: Bearer [access_token]`

Response:
```json
{
  "id": 1,
  "phoneNumber": "+250788123456",
  "serviceId": 1,
  "serviceName": "Notary Service",
  "status": "PENDING",
  "createdAt": "2023-06-15T10:00:00Z",
  "updatedAt": "2023-06-15T10:00:00Z"
}
```

### Create Booking (Public)

```
POST /api/bookings
```

Request:
- Content-Type: `application/json`

```json
{
  "phoneNumber": "+250788123456",
  "serviceId": 1
}
```

Response:
```json
{
  "id": 1,
  "phoneNumber": "+250788123456",
  "serviceId": 1,
  "serviceName": "Notary Service",
  "status": "PENDING",
  "createdAt": "2023-06-15T10:00:00Z",
  "updatedAt": "2023-06-15T10:00:00Z",
  "message": "Thank you! We'll contact you shortly."
}
```

### Update Booking Status (Admin)

```
PATCH /api/bookings/{id}/status
```

Request:
- Header: `Authorization: Bearer [access_token]`
- Content-Type: `application/json`

```json
{
  "status": "CONTACTED"
}
```

Response:
```json
{
  "id": 1,
  "phoneNumber": "+250788123456",
  "serviceId": 1,
  "serviceName": "Notary Service",
  "status": "CONTACTED",
  "createdAt": "2023-06-15T10:00:00Z",
  "updatedAt": "2023-06-15T13:00:00Z"
}
```

### Delete Booking (Admin)

```
DELETE /api/bookings/{id}
```

Request:
- Header: `Authorization: Bearer [access_token]`

Response:
```json
{
  "message": "Booking successfully deleted"
}
```

## Reports

### Export Bookings (CSV/PDF)

```
GET /api/reports/bookings/export
```

Query Parameters:
- `format`: csv or pdf
- `startDate` (optional): Filter start date (YYYY-MM-DD)
- `endDate` (optional): Filter end date (YYYY-MM-DD)
- `status` (optional): Filter by status

Request:
- Header: `Authorization: Bearer [access_token]`
- Header: `Accept: text/csv` or `Accept: application/pdf`

Response:
- CSV or PDF file download

### Dashboard Statistics

```
GET /api/reports/dashboard
```

Request:
- Header: `Authorization: Bearer [access_token]`

Response:
```json
{
  "totalBookings": 100,
  "pendingBookings": 25,
  "contactedBookings": 35,
  "completedBookings": 40,
  "recentBookings": [
    {
      "id": 1,
      "phoneNumber": "+250788123456",
      "serviceId": 1,
      "serviceName": "Notary Service",
      "status": "PENDING",
      "createdAt": "2023-06-15T10:00:00Z"
    },
    {
      "id": 2,
      "phoneNumber": "+250788789012",
      "serviceId": 2,
      "serviceName": "Legal Consultancy",
      "status": "CONTACTED",
      "createdAt": "2023-06-15T11:00:00Z"
    }
  ],
  "bookingsByService": [
    {
      "serviceId": 1,
      "serviceName": "Notary Service",
      "count": 60
    },
    {
      "serviceId": 2,
      "serviceName": "Legal Consultancy",
      "count": 40
    }
  ],
  "bookingsTrend": [
    {
      "date": "2023-06-10",
      "count": 5
    },
    {
      "date": "2023-06-11",
      "count": 7
    },
    {
      "date": "2023-06-12",
      "count": 3
    }
  ]
}
```

## Notifications

### Send SMS Confirmation

```
POST /api/notifications/sms
```

Request:
- Header: `Authorization: Bearer [access_token]`
- Content-Type: `application/json`

```json
{
  "phoneNumber": "+250788123456",
  "message": "Thank you for booking with PSN Rwanda. Your appointment has been confirmed."
}
```

Response:
```json
{
  "success": true,
  "message": "SMS notification sent successfully",
  "messageId": "SM123456"
}
```

### Send Email Notification

```
POST /api/notifications/email
```

Request:
- Header: `Authorization: Bearer [access_token]`
- Content-Type: `application/json`

```json
{
  "email": "admin@psnrwanda.com",
  "subject": "New Booking Notification",
  "message": "A new booking has been created. Please check the admin panel."
}
```

Response:
```json
{
  "success": true,
  "message": "Email notification sent successfully"
}
```

## Company Information

### Get Company Information

```
GET /api/company
```

Response:
```json
{
  "name": "PSN RWANDA Ltd",
  "description": "Legal Activities (Notary Services), Consultancy, Leasing, Real Estate, R&D",
  "address": "Nyamabuye, Muhanga, Southern Province, Rwanda",
  "phone": "+250 788 859 612",
  "email": "casybizy@gmail.com",
  "companyCode": "121058604",
  "incorporatedOn": "2023-01-23"
}
```

### Update Company Information (Admin)

```
PUT /api/company
```

Request:
- Header: `Authorization: Bearer [access_token]`
- Content-Type: `application/json`

```json
{
  "name": "PSN RWANDA Ltd",
  "description": "Legal Activities (Notary Services), Consultancy, Leasing, Real Estate, R&D",
  "address": "Nyamabuye, Muhanga, Southern Province, Rwanda",
  "phone": "+250 788 859 612",
  "email": "casybizy@gmail.com",
  "companyCode": "121058604",
  "incorporatedOn": "2023-01-23"
}
```

Response:
```json
{
  "name": "PSN RWANDA Ltd",
  "description": "Legal Activities (Notary Services), Consultancy, Leasing, Real Estate, R&D",
  "address": "Nyamabuye, Muhanga, Southern Province, Rwanda",
  "phone": "+250 788 859 612",
  "email": "casybizy@gmail.com",
  "companyCode": "121058604",
  "incorporatedOn": "2023-01-23",
  "updatedAt": "2023-06-15T14:00:00Z"
}
```

## Error Responses

All API endpoints return appropriate HTTP status codes:

- `200 OK`: Request succeeded
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request parameters
- `401 Unauthorized`: Missing or invalid authentication
- `403 Forbidden`: Authentication succeeded but user lacks permission
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

Example error response:

```json
{
  "timestamp": "2023-06-15T15:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid phone number format",
  "path": "/api/bookings"
}
``` 