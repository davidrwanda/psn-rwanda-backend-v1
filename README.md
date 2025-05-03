# PSN Rwanda API Collection

This repository contains a Postman collection for the PSN RWANDA Ltd website API. The collection is designed to help developers test and interact with the API endpoints for both the public website and admin panel.

## About PSN RWANDA Ltd

- **Company Name**: PSN RWANDA Ltd
- **Incorporated On**: 23/01/2023
- **Company Code**: 121058604
- **Primary Business**: Legal Activities (Notary Services), Consultancy, Leasing, Real Estate, R&D
- **Location**: Nyamabuye, Muhanga, Southern Province, Rwanda
- **Contact**: +250 788 859 612 | casybizy@gmail.com

## Collection Overview

The Postman collection includes the following endpoint categories:

1. **Authentication** - Admin login, logout, and token refresh
2. **Services** - Managing service offerings (CRUD operations)
3. **Bookings** - User booking management 
4. **Reports** - Data export and dashboard statistics
5. **Notifications** - SMS and email notification endpoints
6. **Company Information** - Company profile management

## Getting Started

### Prerequisites

- [Postman](https://www.postman.com/downloads/) installed on your machine
- Access to the PSN Rwanda API server

### Installation

1. Download the Postman collection file (`PSN_Rwanda_API_Collection.json`)
2. Open Postman
3. Click on "Import" button
4. Select the downloaded JSON file
5. The collection will be imported into Postman

### Configuration

1. Set up environment variables:
   - Click on "Environments" in Postman
   - Create a new environment (e.g., "PSN Rwanda Dev")
   - Add the following variables:
     - `baseUrl`: The base URL of your API (default: http://localhost:4040)
     - `authToken`: Will be automatically set after successful login
     - `refreshToken`: Will be automatically set after successful login

2. Select the environment from the dropdown menu in the top-right corner of Postman

## Usage Examples

### Authentication

1. Use the "Login" request with valid admin credentials
2. Upon successful login, the auth token will be automatically set in your environment variables
3. All subsequent requests requiring authentication will use this token

### Managing Services

1. Create a new service using the "Create Service" endpoint
2. View all services using the "Get All Services" endpoint
3. Update or delete services as needed

### Handling Bookings

1. Create a booking using the "Create Booking" endpoint
2. Admin can view all bookings through the "Get All Bookings" endpoint
3. Update booking status using the "Update Booking Status" endpoint

## API Reference

### Authentication Endpoints

- `POST /api/auth/login` - Admin login
- `POST /api/auth/logout` - Admin logout
- `POST /api/auth/refresh` - Refresh authentication token

### Service Management Endpoints

- `GET /api/services` - Get all services
- `GET /api/services/{id}` - Get service by ID
- `POST /api/services` - Create new service
- `PUT /api/services/{id}` - Update existing service
- `DELETE /api/services/{id}` - Delete service
- `PATCH /api/services/{id}/toggle` - Toggle service visibility

### Booking Management Endpoints

- `GET /api/bookings` - Get all bookings (Admin)
- `GET /api/bookings/{id}` - Get booking by ID (Admin)
- `POST /api/bookings` - Create new booking (Public)
- `PATCH /api/bookings/{id}/status` - Update booking status (Admin)
- `DELETE /api/bookings/{id}` - Delete booking (Admin)

### Report Generation Endpoints

- `GET /api/reports/bookings/export` - Export bookings (CSV/PDF)
- `GET /api/reports/dashboard` - Get dashboard statistics

### Notification Endpoints

- `POST /api/notifications/sms` - Send SMS confirmation
- `POST /api/notifications/email` - Send email notification

### Company Information Endpoints

- `GET /api/company` - Get company information
- `PUT /api/company` - Update company information (Admin)

## Contributing

If you'd like to contribute to this collection or have suggestions for improvement, please feel free to submit a pull request or open an issue.

## License

This project is licensed under the MIT License.

## Email Templates

The application includes professional HTML email templates for booking notifications with the following features:

- Modern responsive design that works well on desktop and mobile
- Consistent branding with PSN Rwanda Ltd's colors (blue #1B5A7D and orange #F48E41)
- Tracking number display for booking confirmation
- Status-specific formatting for booking status updates
- Detailed booking information display

### Adding the Logo

To add the PSN Rwanda logo to email templates:

1. Place your logo file named `logo.png` in the `src/main/resources/static/images/` directory
2. The logo will be automatically included in all email templates
3. For optimal display, the recommended logo size is 180px width (height will be scaled proportionally)

## Configuration

To enable email notifications:

1. Open `application.properties`
2. Set `spring.mail.enabled=true`
3. Configure your SMTP settings:
   ```
   spring.mail.host=your-smtp-server
   spring.mail.port=587
   spring.mail.username=your-email@example.com
   spring.mail.password=your-email-password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```
4. Set `app.url` to your application's base URL for correct tracking links in emails:
   ```
   app.url=https://your-domain.com
   ```

## Developer Notes

* Email templates are located in `src/main/resources/templates/email/`
* The templates use Thymeleaf for dynamic content insertion
* The logo is referenced using `cid:company-logo` in the HTML templates

# Email Configuration Options

## Mail Configuration

The application is configured to use Infomaniak mail server for all email communications:

```
Mail Server: mail.infomaniak.com
Port: 465 (SSL)
Username: info@oneclic.vet
```

This configuration provides reliable email delivery with proper authentication.

## Testing Email Delivery

The application includes a test script to verify email functionality:

```bash
# Test with default configuration
./test-emails.sh

# Test with a specific email address
./test-emails.sh your.email@example.com
```

### Troubleshooting Email Delivery

If emails are sent successfully but not being received, check:

1. **Email Configuration API**: Visit `/api/v1/test/email/configuration` to view current email settings
2. **Spam/Junk Folders**: Check all folders including spam
3. **DNS Settings**: Verify SPF, DKIM, and DMARC are properly configured for oneclic.vet domain:
   ```
   SPF Record: v=spf1 include:spf.infomaniak.ch ~all
   ```
4. **Email Content**: Avoid spam trigger words
5. **Test With Attachment**: Try the email test with attachment option which may bypass spam filters

See the detailed `email-deliverability-guide.md` for comprehensive troubleshooting steps.

# Docker and CI/CD Setup for PSN Rwanda Backend

## Docker Setup

This repository includes a complete Docker setup for building and running the PSN Rwanda backend application in both development and production environments.

### Prerequisites

- Docker
- Docker Compose

### Building the Docker Image Locally

To build the Docker image locally:

```bash
docker build -t psn-rwanda-backend:latest .
```

### Running with Docker Compose

For local development:

```bash
docker-compose up -d
```

For production:

```bash
docker-compose -f docker-compose.prod.yml up -d
```

## CI/CD Pipeline

This project uses GitHub Actions for continuous integration and deployment.

### GitHub Secrets Configuration

For security reasons, sensitive credentials are stored as GitHub repository secrets. You need to configure the following secrets in your GitHub repository:

1. Go to your GitHub repository
2. Navigate to "Settings" > "Secrets and variables" > "Actions"
3. Add the following secrets:
   - `DOCKER_HUB_PAT`: Your Docker Hub Personal Access Token
   - `SERVER_PASSWORD`: The password for the deployment server

### Workflow

The CI/CD pipeline consists of the following steps:

1. **Build**: Compiles the application using Maven
2. **Test**: Runs unit and integration tests
3. **Build Docker Image**: Creates a Docker image for the application
4. **Push to Docker Hub**: Pushes the image to Docker Hub using the `DOCKER_HUB_PAT` secret
5. **Deploy**: Deploys the application to the production server using the `SERVER_PASSWORD` secret

### Deployment

The application is automatically deployed to the production server when changes are pushed to the main branch.

### Manual Deployment

To manually trigger a deployment:

1. Go to the GitHub repository
2. Navigate to Actions
3. Select the "PSN Rwanda Backend CI/CD" workflow
4. Click "Run workflow" and select the branch to deploy

## Server Setup

The production server is configured with Docker and Docker Compose.

### Directory Structure

```
/opt/psn_rwanda/
├── backend/
│   ├── docker-compose.prod.yml
│   └── uploads/
└── frontend/
    └── docker-compose.prod.yml
```

### Accessing the Application

The backend application is running on port 4040 on the production server.

## Environment Variables

The application uses the following environment variables in production:

- `SPRING_PROFILES_ACTIVE`: The active Spring profile (prod)
- `SPRING_DATASOURCE_URL`: JDBC URL for the database
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_MAIL_HOST`: SMTP server host
- `SPRING_MAIL_PORT`: SMTP server port
- `SPRING_MAIL_USERNAME`: SMTP server username
- `SPRING_MAIL_PASSWORD`: SMTP server password
- `APP_NOTIFICATION_PROVIDER_EMAIL`: Email address that receives booking notifications
- `APP_FILE_UPLOAD_DIR`: Directory for file uploads
- `APP_URL`: Public URL of the application
- `JWT_SECRET`: Secret key for JWT authentication 