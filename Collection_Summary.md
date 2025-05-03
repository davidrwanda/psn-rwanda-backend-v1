# PSN Rwanda Postman Collection - Summary

## Files Included

| File | Description |
|------|-------------|
| **PSN_Rwanda_API_Collection.json** | The main Postman collection file with all API endpoints |
| **PSN_Rwanda_API_Environment.json** | Environment variables file for Postman |
| **README.md** | Overview and instructions for using the collection |
| **API_Documentation.md** | Detailed API documentation with request/response examples |
| **Token_Setup_Guide.md** | Guide for setting up automatic token capture in Postman |
| **Collection_Summary.md** | This file - a summary of all included files |

## Collection Overview

The Postman collection for PSN RWANDA Ltd's website includes **30+ API endpoints** covering all aspects of the application:

### 1. Authentication (3 endpoints)
- Admin login
- Admin logout
- Token refresh

### 2. Services Management (6 endpoints)
- Get all services
- Get service by ID
- Create service
- Update service
- Delete service
- Toggle service visibility

### 3. Booking Management (5 endpoints)
- Get all bookings
- Get booking by ID
- Create booking
- Update booking status
- Delete booking

### 4. Reports (3 endpoints)
- Export bookings as CSV
- Export bookings as PDF
- Dashboard statistics

### 5. Notifications (2 endpoints)
- Send SMS confirmation
- Send email notification

### 6. Company Information (2 endpoints)
- Get company information
- Update company information

## Features of the Collection

1. **Environment Variables**
   - Configured for easy switching between development and production
   - Token management system built-in

2. **Test Scripts**
   - Automatic token capture from login response
   - Environment variable setup

3. **Documentation**
   - Detailed API documentation
   - Request/response examples
   - Error handling information

4. **Organized Structure**
   - Endpoints grouped by functionality
   - Clear naming conventions
   - Descriptive comments

## How to Use

1. Import the collection and environment files into Postman
2. Set up the environment variables
3. Follow the automatic token setup guide
4. Test the API endpoints in the recommended sequence:
   - Authentication
   - Service Management
   - Booking Management
   - Reports & Notifications

## Technical Specifications

- **API Format**: RESTful JSON
- **Authentication**: JWT Bearer Tokens
- **Development Base URL**: http://localhost:8080
- **Production Base URL**: To be configured

---

For more details, please refer to the README.md and API_Documentation.md files. 