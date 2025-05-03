#!/bin/bash

# Script to test PSN Rwanda email templates with Infomaniak SMTP

BASE_URL="http://localhost:8080"
DEFAULT_EMAIL="daveleo250@gmail.com"

# Parse command line arguments for email only
if [ $# -eq 1 ]; then
    TEST_EMAIL=$1
    echo "Using custom email address: $TEST_EMAIL"
else
    TEST_EMAIL=$DEFAULT_EMAIL
    echo "Using default email address: $TEST_EMAIL"
fi

echo "PSN Rwanda Email Template Test Script"
echo "===================================="
echo "Using email address: $TEST_EMAIL"
echo "Using Infomaniak mail configuration with sender: PSN RWANDA <info@oneclic.vet>"
echo ""

# Test checking configuration first
echo "0. Checking Email Configuration..."
curl -X GET "${BASE_URL}/api/v1/test/email/configuration" | grep -E "mailUsername|fromEmail|providerEmail"
echo -e "\n"

# Test simple plain text email first
echo "1. Testing Simple Plain Text Email (no templates)..."
curl -X GET "${BASE_URL}/api/v1/test/email/simple?to=${TEST_EMAIL}"
echo -e "\n"

# Wait a bit between requests
sleep 2

# Test email with attachment (better for spam detection bypass)
echo "2. Testing Email with Attachment..."
curl -X GET "${BASE_URL}/api/v1/test/email/test-with-attachment?to=${TEST_EMAIL}"
echo -e "\n"

# Wait a bit between requests
sleep 2

# Test booking confirmation email
echo "3. Testing Booking Confirmation Email (client only)..."
curl -X GET "${BASE_URL}/api/v1/test/email/confirmation?to=${TEST_EMAIL}"
echo -e "\n"

# Wait a bit between requests
sleep 2

# Test booking with provider notification
echo "4. Testing Booking Confirmation with Provider Notification..."
curl -X GET "${BASE_URL}/api/v1/test/email/booking-with-provider?to=${TEST_EMAIL}"
echo -e "\n"

# Wait a bit between requests
sleep 2

# Test booking status update email
echo "5. Testing Booking Status Update Email..."
curl -X GET "${BASE_URL}/api/v1/test/email/status-update?to=${TEST_EMAIL}"
echo -e "\n"

# Wait a bit between requests
sleep 2

# Test admin notification email
echo "6. Testing Admin Notification Email..."
curl -X GET "${BASE_URL}/api/v1/test/email/admin-notification?to=${TEST_EMAIL}"
echo -e "\n"

echo "All test emails have been triggered. Please check ${TEST_EMAIL} inbox."
echo "Note: If emails are not received, please verify:"
echo "  - Email configuration in application.properties"
echo "  - Check your spam/junk folder carefully (emails often go there)"
echo "  - Try with a different email provider (Gmail, Yahoo, Outlook, etc.)"
echo "  - Network connectivity to mail.infomaniak.com"
echo ""

# Ask if we should stop the Spring Boot application
read -p "Stop the Spring Boot application? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Stopping Spring Boot application..."
    kill $APP_PID
    echo "Done."
fi 